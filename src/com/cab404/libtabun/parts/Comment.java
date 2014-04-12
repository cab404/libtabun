package com.cab404.libtabun.parts;

import com.cab404.libtabun.data.Part;
import com.cab404.libtabun.data.Profile;
import com.cab404.libtabun.facility.MessageFactory;
import com.cab404.libtabun.facility.RequestFactory;
import com.cab404.libtabun.facility.ResponseFactory;
import com.cab404.libtabun.util.SU;
import org.json.simple.JSONObject;

/**
 * Ну тут всё ясно.
 *
 * @author cab404
 */
public class Comment extends Part {
    public String text = "", time = "";
    public Profile author;
    public int votes, parent = 0;
    public boolean deleted = false;

    public boolean is_new = false;

    public Comment() {
        type = "Comment";
        author = new Profile();
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
            this.text = SU.drl((String) object.get("sText"));

        return err ? null : this.text;
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

}
