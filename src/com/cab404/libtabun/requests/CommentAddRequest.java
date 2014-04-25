package com.cab404.libtabun.requests;

import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.EntrySet;

/**
 * @author cab404
 */
public class CommentAddRequest extends LSRequest {

    private final int reply, post;
    private final String text;
    private final Type type;

    public static enum Type {
        TALK, TOPIC
    }

    public CommentAddRequest(int post, int reply, String text) {
        this.post = post;
        this.text = text;
        this.reply = reply;
        type = Type.TALK;
    }

    public CommentAddRequest(Type type, int post, int reply, String text) {
        this.post = post;
        this.text = text;
        this.reply = reply;
        this.type = type;
    }


    @Override protected String getURL(AccessProfile profile) {
        String prefix;
        switch (type) {
            case TALK:
                prefix = "/talk";
                break;
            case TOPIC:
                prefix = "/blog";
                break;
            default:
                throw new RuntimeException("Неподдерживаемый тип!");
        }

        return prefix + "/ajaxaddcomment/";
    }

    @Override protected void getData(EntrySet<String, String> data) {
        data.put("reply", reply + "");
        data.put("comment_text", text + "");
        data.put("cmt_target_id", post + "");
    }

}
