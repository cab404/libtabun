package com.cab404.libtabun.requests;

import com.cab404.moonlight.util.modular.AccessProfile;
import org.json.simple.JSONObject;

import java.util.HashMap;

/**
 * @author cab404
 */
public class CommentRequest extends LSRequest {

    private final int reply, post;
    private final String text;

    public CommentRequest(int post, int reply, String text) {
        this.post = post;
        this.text = text;
        this.reply = reply;
    }

    @Override public String getURL(AccessProfile profile) {
        return "/blog/ajaxaddcomment/";
    }

    @Override public void getData(HashMap<String, String> data) {
        data.put("reply", reply + "");
        data.put("comment_text", text + "");
        data.put("cmt_target_id", post + "");
    }
    @Override public void handle(JSONObject object) {

    }

}
