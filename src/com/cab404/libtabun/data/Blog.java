package com.cab404.libtabun.data;

import com.cab404.libtabun.util.JSONable;

import java.util.Calendar;

/**
 * PaWPoL с рейтингом, описанием и читателями.
 *
 * @author cab404
 */
public class Blog extends JSONable {
    private static final long serialVersionUID = 0L;

    @JSONField
    public String
            name,
            url_name,
            about,
            icon;
    @JSONField
    public int
            id,
            readers;
    @JSONField
    public Calendar
            creation_date;
    @JSONField
    public float
            rating = 0;

    @JSONField
    public boolean
            restricted = false;

    public Blog() {
    }

    public Blog(String url) {
        this.url_name = url;
    }

    public String resolveURL() {
        return "/blog/" + url_name + "/";
    }

}
