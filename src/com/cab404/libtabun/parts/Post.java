package com.cab404.libtabun.parts;

import com.cab404.libtabun.facility.MessageFactory;
import com.cab404.libtabun.facility.RequestFactory;
import com.cab404.libtabun.facility.ResponseFactory;
import com.cab404.libtabun.util.html_parser.HTMLParser;
import com.cab404.libtabun.util.html_parser.Tag;
import com.cab404.libtabun.util.SU;
import com.cab404.libtabun.util.U;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Post extends PaWPoL.PostLabel {
    public static enum Type {
        QUIZ, SIMPLE
    }

    public ArrayList<Comment> comment_list;
    public Type topic_type;
    private int max_comment_id = 0;

    /**
     * Создаёт пустой пост ниоткуда. Заполнять самим.
     */
    public Post() {
        comment_list = new ArrayList<>();
        blog = new Blog();
        name = time = content = votes = "";
        type = "Topic";
    }

    /**
     * Загружает заголовок поста со всеми пряниками. Для загрузки комментариев пните fetchNewComments
     */
    public Post(User user, int num) {
        this();
        key = user.key;
        id = num;
    }

    public void fetchHeader(User user) {
        ResponseFactory.read(
                user.execute(RequestFactory.get("/blog/" + id + ".html").build()),
                new PostHeaderParser()
        );
    }

    /**
     * Выдаёт новый парсер для чтения поста в данный объект
     */
    public ResponseFactory.Parser getParser() {
        return new PostHeaderParser();
    }

    /**
     * Парсер заголовка поста
     */
    private class PostHeaderParser implements ResponseFactory.Parser {
        private boolean reading = false;
        private StringBuilder text = new StringBuilder();


        @Override
        public boolean line(String line) {
            if (!reading) {
                if (line.trim().equals("<article class=\"topic topic-type-topic js-topic\">")) {
                    topic_type = Type.SIMPLE;
                    reading = true;
                }

                if (line.trim().equals("<article class=\"topic topic-type-question js-topic\">")) {
                    topic_type = Type.QUIZ;
                    reading = true;
                }

                if (line.contains("var LIVESTREET_SECURITY_KEY")) {
                    key = new LivestreetKey("/", SU.sub(
                            line,
                            "var LIVESTREET_SECURITY_KEY = '",
                            "';"
                    ));
                }
            } else if (line.trim().equals("</article> <!-- /.topic -->")) {
                text.append(line).append("\n");
                HTMLParser raw = new HTMLParser(text.toString());

                id = U.parseInt(SU.sub(raw.getTagByProperty("class", "vote-item vote-up").props.get("onclick"), "(", ","));
                content = raw.getContents(raw.getTagByProperty("class", "topic-content text")).replace("\t", "").trim();
                name = raw.getContents(raw.getTagByProperty("class", "topic-title word-wrap")).trim();

                String vote_info = raw.getTagByProperty("id", "vote_area_topic_" + id).props.get("class").replace("\t", "");
                vote_enabled = vote_info.contains("vote-not-self") && vote_info.contains("not-voted") && vote_info.contains("vote-not-expired");

                if (!vote_enabled) {
                    if (vote_info.contains("voted-up"))
                        your_vote = 1;
                    if (vote_info.contains("voted-down"))
                        your_vote = -1;
                    if (vote_info.contains("voted-zero"))
                        your_vote = 0;
                }

                isInFavs = raw.getTagByProperty("id", "fav_topic_" + id).props.get("class").equals("favourite active");

                int blog_tag;
                try {
                    blog_tag = raw.getTagIndexByProperty("class", "topic-blog");
                } catch (Exception e) {
                    blog_tag = raw.getTagIndexByProperty("class", "topic-blog private-blog");
                }
                blog = new Blog();
                blog.name = raw.getContents(blog_tag);
                blog.url_name = SU.bsub(raw.tags.get(blog_tag).props.get("href"), "/blog/", "/");

                int time_tag = raw.getTagIndexForName("time");
                time = raw.getContents(time_tag).trim();
                date = U.convertDatetime(raw.tags.get(time_tag).props.get("datetime"));
                votes = raw.getContents(raw.getTagIndexByProperty("id", "vote_total_topic_" + id)).trim();
                try {
                    U.parseInt(votes);
                } catch (Exception e) {
                    votes = "±?";
                }
                List<Tag> raw_tags = raw.getAllTagsByProperty("rel", "tag");
                tags = new String[raw_tags.size()];
                for (int i = 0; i != raw_tags.size(); i++) {
                    tags[i] = raw.getContents(raw_tags.get(i));
                }

                author = new UserInfo();
                author.nick = raw.getContents(raw.getTagIndexByProperty("rel", "author"));
                author.small_icon = raw.getTagByProperty("alt", "avatar").props.get("src");
                author.fillImages();
                return false;
            }
            if (reading) text.append(line).append("\n");
            return true;
        }
    }

    /**
     * Парсер комментариев в списках - комментарии пользователя, посты, письма.
     * <s>Но для писем и постов удобнее и быстрее загружать через JSON.</s>
     * Нифига не быстрее.
     */
    public class ActiveCommentListParser implements ResponseFactory.Parser {
        int part = 0;
        private int count_comments_dec = -1;
        private int count_comments = -1;
        private Comment.CommentParser parser = new Comment.CommentParser();

        @Override
        public boolean line(String line) {
            if (part == 0)
                // Находим заголовок с количеством комментариев.
                if (line.contains("<span id=\"count-comments\">")) {
                    count_comments_dec = Integer.parseInt(SU.sub(line, "\">", "<"));
                    count_comments = count_comments_dec;
                    part++;
                } else ;
            else {
                if (count_comments_dec != 0) {
                    if (!parser.line(line)) {
                        comment_list.add(parser.comment);
                        onCommentLoad(parser.comment, count_comments_dec, count_comments);
                        parser = new Comment.CommentParser();
                        count_comments_dec--;
                    }
                } else {
                    return false;
                }
            }


            return true;
        }

        public void onCommentLoad(Comment comment, int left, int total) {
        }
    }

    public static enum PartType {
        COMMENT, HEADER
    }

    public static interface LoadingEventListener {
        public void onLoadingEvent(PartType type, Object part);
    }

    public class PostParser implements ResponseFactory.Parser {
        int part = 0;
        LoadingEventListener listener;

        public PostParser(LoadingEventListener listener) {
            this.listener = listener;
        }

        PostHeaderParser head = new PostHeaderParser();
        ActiveCommentListParser comments = new ActiveCommentListParser() {
            @Override public void onCommentLoad(Comment comment, int left, int total) {
                listener.onLoadingEvent(PartType.COMMENT, comment);
                max_comment_id = Math.max(comment.id, max_comment_id);
            }
        };

        @Override public boolean line(String line) {
            switch (part) {
                case 0:
                    if (!head.line(line)) {
                        listener.onLoadingEvent(PartType.HEADER, null);
                        part++;
                    }
                    break;
                case 1:
                    if (!comments.line(line)) {
                        part++;
                    }
                    break;
                case 2:
                    return false;
            }
            return true;
        }
    }

    /**
     * Возвращает адрес блога
     */
    private String getRelativeAddress() {
        return "/blog/" + id + ".html";
    }


    private boolean favourites(User user, int type) {
        String body = "";
        body += "&type=" + type;
        body += "&idTopic=" + id;
        body += "&security_ls_key=" + key.key;

        String request = ResponseFactory.read(
                user.execute(
                        RequestFactory.post("/ajax/favourite/comment/")
                                .addReferer(key.address)
                                .setBody(body)
                                .XMLRequest()
                                .build()));

        JSONObject object = MessageFactory.processJSONwithMessage(request);

        return (boolean) object.get("bStateError");
    }

    /**
     * Оставляет комментарий посту
     */
    public boolean comment(User user, String text) {
        return comment(user, 0, text);
    }

    /**
     * Оставляет комментарий на комментарий в посте
     *
     * @param parent ID коментария, на который оставляешь комментарий
     */
    public boolean comment(User user, int parent, String text) {
        String body = "";
        body += "&comment_text=" + SU.rl(text);
        body += "&reply=" + parent;
        body += "&cmt_target_id=" + id;
        body += "&security_ls_key=" + key;


        String response = ResponseFactory.read(user.execute(
                RequestFactory
                        .post("/blog/ajaxaddcomment/")
                        .addReferer(getRelativeAddress())
                        .setBody(body)
                        .XMLRequest()
                        .build()
        ));

        JSONObject status = MessageFactory.processJSONwithMessage(response);

        return (boolean) status.get("bStateError");
    }

    /**
     * Голосует за комментарий в посте.
     */
    public boolean vote(User user, int comment, int vote) {

        String body = "";
        body += "&value=" + vote;
        body += "&idComment=" + comment;
        body += "&security_ls_key=" + key;

        String response = ResponseFactory.read(user.execute(
                RequestFactory
                        .post("/ajax/vote/comment/")
                        .addReferer(getRelativeAddress())
                        .setBody(body)
                        .XMLRequest()
                        .build()
        ));

        JSONObject status = MessageFactory.processJSONwithMessage(response);

        boolean err = (boolean) status.get("bStateError");

        if (!err) {
            getCommentByID(comment).votes = U.parseInt(String.valueOf(status.get("iRating")));
        }

        return err;
    }

    /**
     * Голосует за пост
     */
    public boolean voteForPost(User user, int vote) {

        String body = "";
        body += "&value=" + vote;
        body += "&idTopic=" + id;
        body += "&security_ls_key=" + key;

        String response = ResponseFactory.read(user.execute(
                RequestFactory
                        .post("/ajax/vote/topic/")
                        .addReferer(getRelativeAddress())
                        .setBody(body)
                        .XMLRequest()
                        .build()
        ));

        JSONObject status = MessageFactory.processJSONwithMessage(response);
        boolean err = (boolean) status.get("bStateError");
        if (!err) {
            votes = String.valueOf(status.get("iRating"));
            if (!votes.startsWith("-"))
                votes = "+" + votes;
            your_vote = vote;
            vote_enabled = false;
        }
        return err;
    }

    /**
     * Загружает новые комментарии, и говорит, сколько вышло.
     */
    public int fetchNewComments(User user, int max_comment_id, CommentListener cl) {

        String body = "&idCommentLast=" + max_comment_id;
        body += "&idTarget=" + id;
        body += "&typeTarget=topic&security_ls_key=" + key;

        String response = ResponseFactory.read(user.execute(
                RequestFactory
                        .post("/blog/ajaxresponsecomment/")
                        .addReferer(getRelativeAddress())
                        .setBody(body)
                        .XMLRequest()
                        .build()
        ));
        JSONObject status;
        try {
            status = MessageFactory.processJSONwithMessage(response);
        } catch (Throwable ex) {
            return 0;
        }

        if (status == null) return 0;

        for (Object obj : ((JSONArray) status.get("aComments")).toArray()) {
            Comment.CommentParser comment_parser = new Comment.CommentParser();
            String html = (String) ((JSONObject) obj).get("html");

            for (String line : html.split("\n")) {
                comment_parser.line(line);
            }

            comment_parser.comment.key = key;
            comment_list.add(comment_parser.comment);
            if (cl != null) cl.onCommentLoad(comment_parser.comment);
        }

        this.max_comment_id = Math.max(Integer.parseInt(String.valueOf(status.get("iMaxIdComment"))), max_comment_id);
        return ((JSONArray) status.get("aComments")).toArray().length;
    }

    /**
     * Загружает и комментарии и заголовок сразу со странички.
     */
    public void initialFetch(User user, LoadingEventListener l) {
        ResponseFactory.read(
                user.execute(RequestFactory.get("/blog/" + id + ".html").build()),
                new PostParser(l)
        );
    }

    public int fetchNewComments(User user, CommentListener cl) {
        return fetchNewComments(user, max_comment_id, cl);
    }

    public int fetchNewComments(User user, int max_comment_id) {
        return fetchNewComments(user, max_comment_id, null);
    }

    public int fetchNewComments(User user) {
        return fetchNewComments(user, max_comment_id, null);
    }

    public Comment getCommentByID(int id) {
        for (Comment comment : comment_list) {
            if (comment.id == id) return comment;
        }
        return null;
    }

    public interface CommentListener {
        public void onCommentLoad(Comment comment);
    }
}
