package com.cab404.libph.requests;

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

	@Override protected void onRedirect(String to) {
		/* Не должно быть никаких редиректов. И всё тут. */
		cancel();
		super.onRedirect(to);
	}

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
