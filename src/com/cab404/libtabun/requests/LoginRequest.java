package com.cab404.libtabun.requests;

import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.EntrySet;
import org.json.simple.JSONObject;

/**
 * @author cab404
 */
public class LoginRequest extends LSRequest {

    private final String login;
    private final String password;
    private boolean isLoggedIn;

    public LoginRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Override protected String getURL(AccessProfile profile) {
        return "/login/ajax-login/";
    }

    @Override protected void getData(EntrySet<String, String> data) {
        data.put("password", password);
        data.put("login", login);
        data.put("return-path", "/");
        data.put("remember", "on");
    }

    @Override protected void handle(JSONObject object) {
        isLoggedIn = !(boolean) object.get("bStateError");
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }
}
