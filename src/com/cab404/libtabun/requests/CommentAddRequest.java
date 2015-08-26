package com.cab404.libtabun.requests;

import com.cab404.libtabun.data.Type;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.EntrySet;
import org.json.simple.JSONObject;

/**
 * @author cab404
 */
public class CommentAddRequest extends LSRequest {

    private final int reply, post;
    private final String text;
    private final Type type;
    public int id;

    /**
     * @param type Константа из {@link com.cab404.libtabun.data.Type}
     *             Для постов - {@link com.cab404.libtabun.data.Type#BLOG}
     *             Для писем - {@link com.cab404.libtabun.data.Type#TALK}
     */
    public CommentAddRequest(Type type, int post, int reply, String text) {
        this.post = post;
        this.text = text;
        this.reply = reply;
        this.type = type;
    }


    @Override
    protected void handle(JSONObject object) {
        super.handle(object);
        if (success)
            id = Integer.parseInt(object.get("sCommentId") + "");
    }

    @Override
    protected String getURL(AccessProfile profile) {
        return "/" + type.name.toLowerCase() + "/ajaxaddcomment/";
    }

    @Override
    protected void getData(EntrySet<String, String> data) {
        data.put("reply", reply + "");
        data.put("comment_text", text + "");
        data.put("cmt_target_id", post + "");
    }

}
