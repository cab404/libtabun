package com.cab404.libtabun.parts;

import com.cab404.libtabun.data.Part;
import com.cab404.libtabun.util.html_parser.HTMLTree;
import com.cab404.libtabun.facility.MessageFactory;
import com.cab404.libtabun.facility.RequestFactory;
import com.cab404.libtabun.facility.ResponseFactory;
import com.cab404.libtabun.util.SU;
import com.cab404.libtabun.util.U;
import com.cab404.libtabun.util.html_parser.Tag;
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
    public boolean MODERASTIA = false;

    public boolean is_new = false;

    public Comment() {
        type = "Comment";
    }

    /**
     * Парсит комментарии с <s>1823 года</s> тега section
     */
    public static class CommentParser extends U.TextPartParser {
        public Comment comment = new Comment();

        @Override public void process(StringBuilder out) {
            HTMLTree parser = new HTMLTree(out.toString());

            comment.id = U.parseInt(parser.get(0).get("id").replace("comment_id_", ""));

            try {
                comment.body = parser.getContents(parser.xPathFirstTag("section/div/div&class=*text*"));
            } catch (Exception ex) {
                comment.MODERASTIA = true;
            }

            HTMLTree info =
                    parser.getTree(parser.xPathFirstTag("ul&class=comment-info"));

            Tag parent = info.xPathFirstTag("li&class=*parent*/a");
            if (parent == null)
                comment.parent = 0;
            else
                comment.parent = U.parseInt(SU.bsub(parent.get("onclick"), ",", ");"));

            comment.author = SU.bsub(info.xPathFirstTag("li/a").get("href"), "profile/", "/");
            comment.is_new = parser.get(0).get("class").contains("comment-new");
            comment.time = info.xPathFirstTag("li/time").get("datetime");
            comment.avatar = info.xPathFirstTag("li/a/img").get("src");
            comment.votes = U.parseInt(info.getContents(info.xPathFirstTag("li/span&class=vote-count")));

        }
        @Override public void finished() {
        }
        
        @Override public boolean isStart(String line) {
            return line.contains("<section id=\"comment_id");
        }
        @Override public boolean isEnd(String line) {
            return line.contains("</section>");
        }
    }

    public String edit(User user, Part post, String text) {
        String body = "";
        body += "commentId=" + id;
        body += "&text=" + SU.rl(text);
        body += "&security_ls_key=" + post.key;

        String request = ResponseFactory.read(
                user.execute(
                        RequestFactory.post("/ec_ajax/savecomment/")
                                .addReferer(post.key.address)
                                .setBody(body)
                                .XMLRequest()
                                .build()
                )
        );

        JSONObject object = MessageFactory.processJSONwithMessage(request);


        boolean err = (boolean) object.get("bStateError");

        if (!err)
            this.body = SU.drl((String) object.get("sText"));

        return err ? null : this.body;
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
                                .build()
                )
        );

        JSONObject object = MessageFactory.processJSONwithMessage(request);

        return (boolean) object.get("bStateError");
    }

    public boolean addToFavourites(User user) {
        return favourites(user, 1);
    }

    public static int getPostNum(User user, int comment_id) {
        return Integer.parseInt(
                SU.bsub(
                        user.execute(RequestFactory.get("/comments/" + comment_id).build(), false)
                                .getFirstHeader("Location")
                                .getValue(),
                        "/",
                        ".html"
                )
        );
    }

    public static Comment getByID(User user, int comment_id) {
        Topic post = new Topic(user, Comment.getPostNum(user, comment_id));
        post.fetchNewComments(user, comment_id - 1);
        return post.getCommentByID(comment_id);
    }

    public boolean removeFromFavourites(User user) {
        return favourites(user, 0);
    }
}
