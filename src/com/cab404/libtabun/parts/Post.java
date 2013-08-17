package com.cab404.libtabun.parts;

import com.cab404.libtabun.*;
import com.cab404.libtabun.facility.MessageFactory;
import com.cab404.libtabun.facility.RequestFactory;
import com.cab404.libtabun.facility.ResponseFactory;
import javolution.util.FastList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Post extends Part {
    public String tags, name, author, time, body, votes;
    public FastList<Comment> comments;
    private int max_comment_id = 0;
    public Blog blog;

    /**
     * Создаёт пустой пост ниоткуда. Заполнять самим.
     */
    public Post() {
        comments = new FastList<>();
        blog = new Blog();
        name = author = time = body = votes = "";
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
                new PostParser()
        );
    }

    /**
     * Выдаёт новый парсер для чтения поста в данный объект
     */
    public ResponseFactory.Parser getParser() {
        return new PostParser();
    }

    /**
     * Парсер заголовка поста
     */
    private class PostParser implements ResponseFactory.Parser {
        private int part = 0;
        ActiveCommentListParser comment_parser = new ActiveCommentListParser();

        @Override
        public boolean line(String line) {
            switch (part) {
                case 0:
                    // Находим заголовок и ID.
                    if (line.contains("rss/comments")) {
                        name = U.sub(line, "title=\"", "\"");
                        id = Integer.parseInt(U.sub(line, "rss/comments/", "/"));
                        part++;
                    }
                    break;
                case 1:
                    // Находим ключ
                    if (line.contains("LIVESTREET_SECURITY_KEY")) {
                        key = new LivestreetKey(getRelativeAddress(), U.sub(line, "'", "'"));
                        part++;
                    }
                    break;
                case 2:
                    if (line.contains("<h3><a href=\"http://tabun.everypony.ru/blog/")) {
                        blog.url_name = U.sub(line, "blog/", "/");
                        blog.name = U.sub(line, "\">", "<");
                        part++;
                    } else if (line.contains("vote-item")) part++;
                    break;
                case 3:
                    // Читаем заголовок количества голосов.
                    if (line.contains("<span id=\"vote_total_topic")) {
                        part++;
                    }
                    break;
                case 4:
                    // Читаем количество голосов.
                    if (line.contains("</span>")) {
                        part++;
                    } else {
                        votes = line.trim();
                        try {
                            Integer.parseInt(votes);
                        } catch (NumberFormatException e) {
                            votes = "?";
                        }
                    }
                    break;
                case 5:
                    // Читаем автора.
                    if (line.contains("http://tabun.everypony.ru/profile/")) {
                        author = U.sub(line, "profile/", "/");
                        part++;
                    }
                    break;
                case 6:
                    // Пропускаем первый </header>
                    if (line.contains("<div class=\"topic-content text\">")) {
                        part++;
                    }
                    break;
                case 7:
                    // Читаем тело до первого </div>
                    if (line.trim().equals("</div>")) {
                        part++;
                    } else if (!line.trim().isEmpty())
                        body += line + "\n";
                    break;
                case 8:
                    if (line.contains("rel=\"tag\"")) {
                        tags = U.removeAllTags(line);
                        part++;
                    }
                case 9:
                    // Читаем время написания
                    if (line.contains("time datetime")) {
                        time = U.sub(line, "datetime=\"", "\"");
                        part++;
                        return false;
                    }
                    break;

            }

            return true;
        }
    }

    /**
     * Парсер комментариев в списках - комментарии пользователя, посты, письма.
     * Но для писем и постов удобнее и быстрее загружать через JSON.
     */
    public static class ActiveCommentListParser implements ResponseFactory.Parser {
        int part = 0;
        public FastList<Comment> comments = new FastList<>();
        private int count_comments_dec = -1;
        private int count_comments = -1;
        private Comment.CommentParser parser = new Comment.CommentParser();

        @Override
        public boolean line(String line) {
            if (part == 0)
                // Находим заголовок с количеством комментариев.
                if (line.contains("<span id=\"count-comments\">")) {
                    count_comments_dec = Integer.parseInt(U.sub(line, "\">", "<"));
                    count_comments = count_comments_dec;
                    part++;
                } else ;
            else {
                if (count_comments_dec != 0) {
                    if (!parser.line(line)) {
                        comments.add(parser.comment);
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
        body += "&comment_text=" + U.rl(text);
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

        return (boolean) status.get("bStateError");
    }

    /**
     * Голосует за пост
     */
    public boolean voteForPost(User user, int vote) {

        U.v(key);

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

        return (boolean) status.get("bStateError");
    }

    /**
     * Загружает новые комментарии, и говорит, сколько вышло.
     */
    public int fetchNewComments(User user, int max_comment_id) {

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

        JSONObject status = MessageFactory.processJSONwithMessage(response);
        for (Object obj : ((JSONArray) status.get("aComments")).toArray()) {
            Comment.CommentParser comment_parser = new Comment.CommentParser();
            String html = (String) ((JSONObject) obj).get("html");

            for (String line : html.split("\n")) {
                comment_parser.line(line);
            }

            comment_parser.comment.key = key;
            comments.add(comment_parser.comment);
        }

        this.max_comment_id = Integer.parseInt(String.valueOf(status.get("iMaxIdComment")));
        return ((JSONArray) status.get("aComments")).toArray().length;
    }

    public int fetchNewComments(User user) {
        return fetchNewComments(user, max_comment_id);
    }

    public Comment getCommentByID(int id) {
        for (Comment comment : comments) {
            if (comment.id == id) return comment;
        }
        return null;
    }
}
