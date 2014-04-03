package com.cab404.libtabun.util;

import com.cab404.libtabun.util.modular.Cookies;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import java.io.IOException;

/**
 * Request utils.
 *
 * @author cab404
 */
public class RU {

    public static HttpResponse exec(HttpRequestBase request, Cookies cookies, boolean follow, int timeout) {
        try {
            HttpClient client = new DefaultHttpClient();

            client.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
            client.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
            client.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, follow);

            request.addHeader(cookies.getCookies());

            HttpResponse response = client.execute(U.host, request);

            cookies.handleCookies(response.getHeaders("Set-Cookie"));

            return response;
        } catch (IOException e) {
            U.w(e);
            return null;
        }
    }

    public static HttpResponse exec(HttpRequestBase request, Cookies cookies, boolean follow) {
        return exec(request, cookies, follow, 10000);
    }

    public static HttpResponse exec(HttpRequestBase request, Cookies cookies) {
        return exec(request, cookies, true);
    }

    public static HttpResponse exec(HttpRequestBase request) {
        return exec(request, null);
    }
}
