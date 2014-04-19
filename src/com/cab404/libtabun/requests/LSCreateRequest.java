package com.cab404.libtabun.requests;

import com.cab404.moonlight.framework.EntrySet;
import com.cab404.moonlight.util.U;
import org.json.simple.JSONObject;

/**
 * @author cab404
 */
public abstract class LSCreateRequest extends LSRequest {
    @Override protected boolean isLong() {
        return true;
    }

    @Override public void getData(EntrySet<String, String> data) {

    }

    @Override public void handleResponse(String response) {
        U.v(response);
    }

    @Override public void handle(JSONObject object) {}
}
