package com.cab404.libtabun.util.modular;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles and stores cookies.
 *
 * @author cab404
 */
public class Cookies {
    public HashMap<String, String> cookies;

    public Cookies() {
        this.cookies = new HashMap<>();
    }

    public Header getCookies() {
        String out = "";

        for (Map.Entry<String, String> cookie : cookies.entrySet())
            out += cookie.getKey() + "=" + cookie.getValue() + "; ";

        return new BasicHeader("Cookie", out);
    }

    public void handleCookies(Header[] headers) {
        for (Header header : headers)
            addCookies(header.getValue());
    }

    void addCookies(String input) {
        String cookie = input.split("; ")[0];
        String[] split = cookie.split("=");
        if (split.length == 2)
            cookies.put(split[0], split[1]);
    }

    @Override public String toString() {
        return getCookies().getValue();
    }

    public static Cookies parseString(String cookies) {
        Cookies _return = new Cookies();
        _return.addCookies(cookies);
        return _return;
    }
}
