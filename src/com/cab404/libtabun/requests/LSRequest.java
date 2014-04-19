package com.cab404.libtabun.requests;

import com.cab404.libtabun.data.LivestreetKey;
import com.cab404.libtabun.pages.TabunPage;
import com.cab404.moonlight.facility.MessageFactory;
import com.cab404.moonlight.facility.RequestFactory;
import com.cab404.moonlight.facility.ResponseFactory;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.EntrySet;
import com.cab404.moonlight.framework.ShortRequest;
import com.cab404.moonlight.util.SU;
import org.apache.http.client.methods.HttpRequestBase;
import org.json.simple.JSONObject;

import java.util.Map;

/**
 * @author cab404
 */
public abstract class LSRequest extends ShortRequest {
    private boolean success = false;

    @Override public void handleResponse(String response) {
        JSONObject jsonObject = MessageFactory.processJSONwithMessage(response);
        success = !(boolean) jsonObject.get("bStateError");
        handle(jsonObject);
    }

    protected boolean isLong() {return false;}
    protected boolean isChunked() {
        return false;
    }

    private LivestreetKey key;
    @Override public HttpRequestBase getRequest(AccessProfile profile) {

        EntrySet<String, String> data = new EntrySet<>();
        data.put("security_ls_key", key.key);
        getData(data);

        String url = getURL(profile);

        RequestFactory request = RequestFactory
                .post(url, profile)
                .addReferer(url);

        if (isLong()) {

            request.MultipartRequest(data);

        } else {

            StringBuilder request_body = new StringBuilder();
            for (Map.Entry<String, String> e : data)
                request_body
                        .append('&')
                        .append(e.getKey())
                        .append('=')
                        .append(SU.rl(e.getValue()));

            request
                    .XMLRequest()
                    .setBody(request_body.toString(), isChunked());
        }

        return request.build();
    }

    public abstract String getURL(AccessProfile profile);

    public abstract void getData(EntrySet<String, String> data);

    public abstract void handle(JSONObject object);

    public boolean success() {
        return success;
    }

    public <T extends LSRequest> T exec(AccessProfile profile, LivestreetKey key) {
        this.key = key;
        super.fetch(profile);
        return ((T) this);
    }

    public <T extends LSRequest> T exec(AccessProfile profile, TabunPage page) {
        return exec(profile, page.key);
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
