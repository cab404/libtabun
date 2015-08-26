package com.cab404.libtabun.requests;

import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.EntrySet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cab404
 */
public class UserAutocompleteRequest extends LSRequest {

    private String value;
    public List<String> names;

    public UserAutocompleteRequest(String value) {
        this.value = value;
    }

    @Override
    protected void getData(EntrySet<String, String> data) {
        data.put("value", value);
    }

    @Override
    protected String getURL(AccessProfile profile) {
        return "/ajax/autocompleter/user/";
    }

    @Override
    protected void handle(JSONObject object) {
        super.handle(object);
        names = new ArrayList<>();
        for (Object obj : (JSONArray) object.get("aItems"))
            names.add(obj.toString());
    }
}
