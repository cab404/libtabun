package com.cab404.libtabun.requests;

import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.EntrySet;

/**
 * @author cab404
 */
public class RefreshCommentsRequest extends LSRequest {

    private String type;
    private final int id;
    private final int last_comment_id;

    public RefreshCommentsRequest(String type, int id, int last_comment_id) {
        this.type = type;
        this.id = id;
        this.last_comment_id = last_comment_id;
    }

    @Override protected void getData(EntrySet<String, String> data) {
        data.put("idTarget", id + "");
        data.put("typeTarget", type);
        data.put("idCommentLast", last_comment_id + "");
    }

    @Override protected String getURL(AccessProfile profile) {
        return null;
    }

}
