package com.cab404.libtabun.requests;

import com.cab404.moonlight.util.SU;
import com.cab404.moonlight.util.modular.AccessProfile;
import org.json.simple.JSONObject;

import java.util.HashMap;

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

    @Override public String getURL(AccessProfile profile) {
        return "/login/ajax-login/";
    }

    @Override public void getData(HashMap<String, String> data) {
        data.put("password", SU.rl(password));
        data.put("login", SU.rl(login));
        data.put("return-path", "/");
        data.put("remember", "on");
    }

    @Override public void handle(JSONObject object) {
        isLoggedIn = !(boolean) object.get("bStateError");
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }
}
