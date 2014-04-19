package com.cab404.libtabun.requests;

import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.EntrySet;
import org.json.simple.JSONObject;

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

    @Override public void getData(EntrySet<String, String> data) {
        data.put("reply", reply + "");
        data.put("comment_text", text + "");
        data.put("cmt_target_id", post + "");
    }
    @Override public void handle(JSONObject object) {

    }

}
