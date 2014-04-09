package com.cab404.libtabun.requests;

import com.cab404.libtabun.facility.MessageFactory;
import com.cab404.libtabun.facility.RequestFactory;
import com.cab404.libtabun.facility.ResponseFactory;
import com.cab404.libtabun.pages.TabunPage;
import com.cab404.libtabun.parts.LivestreetKey;
import com.cab404.libtabun.util.SU;
import com.cab404.libtabun.util.loaders.ShortRequest;
import com.cab404.libtabun.util.modular.AccessProfile;
import org.apache.http.client.methods.HttpRequestBase;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cab404
 */
public abstract class LSRequest extends ShortRequest {


    @Override public void handleResponse(String response) {
        JSONObject jsonObject = MessageFactory.processJSONwithMessage(response);
        handle(jsonObject);
    }

    private LivestreetKey key;
    @Override public HttpRequestBase getRequest(AccessProfile profile) {

        HashMap<String, String> data = new HashMap<>();
        getData(data);
        data.put("security_ls_key", key.key);

        StringBuilder request_body = new StringBuilder();
        for (Map.Entry<String, String> e : data.entrySet())
            request_body
                    .append('&')
                    .append(e.getKey())
                    .append('=')
                    .append(SU.rl(e.getValue()));

        return RequestFactory
                .post(getURL(profile), profile)
                .addReferer(key.address)
                .setBody(request_body.toString())
                .XMLRequest()
                .build();
    }

    public abstract String getURL(AccessProfile profile);

    public abstract void getData(HashMap<String, String> data);

    public abstract void handle(JSONObject object);

    public void exec(AccessProfile profile, LivestreetKey key) {
        this.key = key;
        super.fetch(profile);
    }

    public <T extends LSRequest> T exec(AccessProfile profile, TabunPage page) {
        this.key = page.key;
        super.fetch(profile);
        return ((T) this);
    }

    /**
     * Вместо этого используй exec.
     */
    @Override public void fetch(AccessProfile accessProfile) {
        super.fetch(accessProfile);
    }

    /**
     * Вместо этого используй exec.
     */
    @Override public void fetch(AccessProfile profile, ResponseFactory.StatusListener statusListener) {
        super.fetch(profile, statusListener);
    }
}
