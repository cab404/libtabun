package com.cab404.libtabun.parts;

import com.cab404.libtabun.*;
import com.cab404.libtabun.facility.MessageFactory;
import com.cab404.libtabun.facility.RequestFactory;
import com.cab404.libtabun.facility.ResponseFactory;
import org.json.simple.JSONObject;

/**
 * Ну тут всё ясно.
 *
 * @author cab404
 */
public class Comment extends Part {
    public String author = "", body = "", time = "";
    public int votes, parent = 0;
    public String avatar;

    public Comment() {
        type = "Comment";
    }

    /**
     * Парсит комментарии с <s>1823 года</s> тега section
     */
    public static class CommentParser implements ResponseFactory.Parser {
        public Comment comment = new Comment();
        int part = 0;

        @Override
        public boolean line(String line) {
            switch (part) {
                case 0:
                    // Находим заголовок
                    if (line.contains("<section id=\"comment_id")) {
                        comment.id = U.parseInt(U.sub(line, "_id_", "\""));
                        part++;
                    }
                    break;
                case 1:
                    // Находим текст
                    if (line.contains("<div class=\" text\">")) {
                        part++;
                    }
                    break;
                case 2:
                    // Записываем текст
                    // Изменить эту фигню на что-нибудь более правдоподобное. А то ведь и табуляцию сменить могут.
                    if (line.equals("\t\t\t</div>")) part++;
                    else comment.body += line.replace("\t", "");
                    break;
                case 3:
                    // Находим автора
                    if (line.contains("http://tabun.everypony.ru/profile/")) {
                        comment.author = U.sub(line, "http://tabun.everypony.ru/profile/", "/");
                        comment.avatar = U.sub(line, "img src=\"", "\"");
                        part++;
                    }
                    break;
                case 4:
                    // Находим дату публикации
                    if (line.contains("<time datetime")) {
                        comment.time = U.sub(line, "datetime=\"", "\"");
                        part++;
                    }
                    break;
                case 5:
                    // Находим рейтинг
                    if (line.contains("vote_total_comment")) {
                        comment.votes = U.parseInt(U.sub(line, ">", "<"));
                        part++;
                    }
                    break;
                case 6:
                    // Пытаемся найти родительский комментарий
                    if (line.contains("goToParentComment"))
                        comment.parent = U.parseInt(U.sub(line, ",", ");"));
                    if (line.contains("</section>"))
                        return false;
                    break;

            }

            return true;
        }
    }

    public boolean edit(User user, String text) {
        String body = "";
        body += "commentId=" + id;
        body += "&text=" + U.rl(text);
        body += "&security_ls_key=" + key.key;

        String request = ResponseFactory.read(
                user.execute(
                        RequestFactory.post("/role_ajax/savecomment/")
                                .addReferer(key.address)
                                .setBody(body)
                                .XMLRequest()
                                .build()));

        JSONObject object = MessageFactory.processJSONwithMessage(request);

        return (boolean) object.get("bStateError");
    }

    private boolean favourites(User user, int type) {
        String body = "";
        body += "&type=" + type;
        body += "&idComment=" + id;
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

    public boolean addToFavourites(User user) {
        return favourites(user, 1);
    }

    public static int getPostNum(User user, int comment_id) {
        return Integer.parseInt(
                U.bsub(
                        user.execute(RequestFactory.get("/comments/" + comment_id).build(), false)
                                .getFirstHeader("Location")
                                .getValue(),
                        "/",
                        ".html")
        );
    }

    public static Comment getByID(User user, int comment_id){
        Post post = new Post(user, Comment.getPostNum(user, comment_id));
        post.fetchNewComments(user, comment_id - 1);
        return post.getCommentByID(comment_id);
    }

    public boolean removeFromFavourites(User user) {
        return favourites(user, 0);
    }

}
