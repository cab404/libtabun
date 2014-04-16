package com.cab404.libtabun.data;

import java.util.Calendar;

/**
 * PaWPoL с рейтингом, описанием и читателями.
 *
 * @author cab404
 */
public class Blog {
    public String
            name,
            url_name,
            about,
            icon;
    public int
            id;
    public Calendar
            creation_date;
    public float
            rating = 0;

    public boolean
            restricted = false;

    public Blog() {}
    public Blog(String url) {this.url_name = url;}

}
