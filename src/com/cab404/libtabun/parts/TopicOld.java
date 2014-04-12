package com.cab404.libtabun.parts;

import com.cab404.libtabun.data.Blog;
import com.cab404.libtabun.data.Topic;
import com.cab404.libtabun.facility.MessageFactory;
import com.cab404.libtabun.facility.RequestFactory;
import com.cab404.libtabun.facility.ResponseFactory;
import com.cab404.libtabun.util.SU;
import com.cab404.libtabun.util.U;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class TopicOld extends Topic {
    public static enum Type {
        QUIZ, SIMPLE
    }

    public ArrayList<Comment> comment_list;
    public Type topic_type;
    private int max_comment_id = 0;

    /**
     * Создаёт пустой пост ниоткуда. Заполнять самим.
     */
    public TopicOld() {
        comment_list = new ArrayList<>();
        blog = new Blog();
        title = time = text = votes = "";
        type = "Topic";
    }

    /**
     * Загружает заголовок поста со всеми пряниками. Для загрузки комментариев пните fetchNewComments
     */
    public TopicOld(User user, int num) {
        this();
        key = user.key;
        id = num;
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
//
//        if (status == null) return 0;
//
//        for (Object obj : ((JSONArray) status.get("aComments")).toArray()) {
//            Comment.CommentParser comment_parser = new Comment.CommentParser();
//            String html = (String) ((JSONObject) obj).get("html");
//
//            for (String line : html.split("\n")) {
//                comment_parser.line(line);
//            }
//
//            comment_parser.comment.key = key;
//            comment_list.add(comment_parser.comment);
//            if (cl != null) cl.onCommentLoad(comment_parser.comment);
//        }

        this.max_comment_id = Math.max(Integer.parseInt(String.valueOf(status.get("iMaxIdComment"))), max_comment_id);
        return ((JSONArray) status.get("aComments")).toArray().length;
    }

    /**
     * Загружает и комментарии и заголовок сразу со странички.
     */
//    public void initialFetch(User user, LoadingEventListener l) {
//        ResponseFactory.read(
//                user.execute(RequestFactory.get("/blog/" + id + ".html").build()),
//                new PostParser(l)
//        );
//    }

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
