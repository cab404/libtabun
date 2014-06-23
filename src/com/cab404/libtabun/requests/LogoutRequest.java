package com.cab404.libtabun.requests;

import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.Request;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * Logout.
 *
 * @author cab404
 */
public class LogoutRequest extends Request {

    private boolean success = false;

    @Override protected HttpRequestBase getRequest(AccessProfile accessProfile) {
        if (accessProfile.cookies.containsKey("key")) {
            success = true;
            return new HttpGet("/login/exit?security_ls_key=" + accessProfile.cookies.get("key"));
        }
        return new HttpGet("/");
    }

    public boolean success() {
        return success;
    }

    @Override public void finished() {}

    public void exec(AccessProfile profile) {
        super.fetch(profile);
    }
}
