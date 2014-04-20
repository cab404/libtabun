package com.cab404.libtabun.requests;

import org.apache.http.HttpResponse;

/**
 * LSRequest, который использует multipart/form-data и не отдаёт json.
 *
 * @author cab404
 */
public abstract class LSCreateRequest extends LSRequest {
    @Override protected boolean isLong() {
        return true;
    }

    protected void onSuccess(String url) {}
    protected void onFailure(HttpResponse response) {}

    @Override public boolean line(String line) {
        return false;
    }

    @Override protected void onResponseGain(HttpResponse response) {
        super.onResponseGain(response);

        if (response.getStatusLine().getStatusCode() == 301) {
            onSuccess(response.getFirstHeader("Location").getValue());
            success = true;
        } else {
            onFailure(response);
            success = false;
        }
    }

    @Override protected void handleResponse(String response) {}

}
