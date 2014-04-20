package com.cab404.libtabun.requests;

import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.EntrySet;

/**
 * @author cab404
 */
public class CommentAddRequest extends LSRequest {

    private final int reply, post;
    private final String text;

    public CommentAddRequest(int post, int reply, String text) {
        this.post = post;
        this.text = text;
        this.reply = reply;
    }

    @Override protected String getURL(AccessProfile profile) {
        return "/blog/ajaxaddcomment/";
    }

    @Override protected void getData(EntrySet<String, String> data) {
        data.put("reply", reply + "");
        data.put("comment_text", text + "");
        data.put("cmt_target_id", post + "");
    }

}
