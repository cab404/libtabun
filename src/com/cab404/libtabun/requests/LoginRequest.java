package com.cab404.libtabun.requests;

import com.cab404.libtabun.facility.MessageFactory;
import com.cab404.libtabun.facility.RequestFactory;
import com.cab404.libtabun.parts.LivestreetKey;
import com.cab404.libtabun.util.SU;
import com.cab404.libtabun.util.loaders.ShortRequest;
import org.apache.http.client.methods.HttpRequestBase;
import org.json.simple.JSONObject;

/**
 * @author cab404
 */
public class LoginRequest extends ShortRequest {

    private final String login;
    private final String password;
    private LivestreetKey key;
    private boolean isLoggedIn;

    public LoginRequest(String login, String password, LivestreetKey key) {
        this.login = login;
        this.password = password;
        this.key = key;
    }

    @Override public HttpRequestBase getRequest() {
        String packet = "";
        packet += "&login=" + SU.rl(login);
        packet += "&password=" + SU.rl(password);
        packet += "&security_ls_key=" + key;
        packet += "&remember=on";
        packet += "&return-path=/";

        return RequestFactory
                .post("/login/ajax-login/")
                .addReferer(key.address)
                .setBody(packet)
                .XMLRequest()
                .build();
    }

    @Override public void handleResponse(String response) {
        response = SU.drl(response);
        JSONObject parsed = MessageFactory.processJSONwithMessage(response);

        isLoggedIn = !(boolean) parsed.get("bStateError");
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }
}
