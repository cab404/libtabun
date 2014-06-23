package com.cab404.libtabun.data;

import java.io.Serializable;
import java.util.Calendar;

/**
 * PaWPoL с рейтингом, описанием и читателями.
 *
 * @author cab404
 */
public class Blog implements Serializable {
    private static final long serialVersionUID = 0L;

    public String
            name,
            url_name,
            about,
            icon;
    public int
            id,
            readers;
    public Calendar
            creation_date;
    public float
            rating = 0;

    public boolean
            restricted = false;

    public Blog() {}
    public Blog(String url) {this.url_name = url;}

}
