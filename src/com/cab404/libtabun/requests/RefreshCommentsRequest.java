package com.cab404.libtabun.requests;

import com.cab404.libtabun.data.Comment;
import com.cab404.libtabun.data.Type;
import com.cab404.libtabun.modules.CommentModule;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.EntrySet;
import com.cab404.moonlight.parser.HTMLTree;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cab404
 */
public class RefreshCommentsRequest extends LSRequest {

    private Type type;
    public List<Comment> comments = new ArrayList<>();
    private final int id;
    private final int last_comment_id;

    @Override
    protected void onRedirect(String to) {
        /* Не должно быть никаких редиректов. И всё тут. */
        cancel();
        super.onRedirect(to);
    }

    public RefreshCommentsRequest(Type type, int id, int last_comment_id) {
        this.type = type;
        this.id = id;
        this.last_comment_id = last_comment_id;
    }

    @Override
    protected void getData(EntrySet<String, String> data) {
        data.put("idTarget", id + "");
        data.put("typeTarget", type.name);
        data.put("idCommentLast", last_comment_id + "");
    }

    @Override
    protected void handle(JSONObject object) {
        super.handle(object);

        CommentModule module =
                new CommentModule(type.equals(Type.TOPIC) ? CommentModule.Mode.TOPIC : CommentModule.Mode.LETTER);

        for (Object obj : (JSONArray) object.get("aComments")) {
            HTMLTree c_data = new HTMLTree((String) ((JSONObject) obj).get("html"));
            comments.add(module.extractData(c_data, null));
        }
    }

    @Override
    protected String getURL(AccessProfile profile) {
        return "/" + (type.equals(Type.TOPIC) ? "blog" : type.name.toLowerCase()) + "/ajaxresponsecomment/";
    }

}
