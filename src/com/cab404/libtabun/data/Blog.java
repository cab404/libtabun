package com.cab404.libtabun.data;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * PaWPoL с рейтингом, описанием и читателями.
 *
 * @author cab404
 */
public class Blog {
    public String name, url_name, about;
    public ArrayList<Topic> posts;
    public int votes = 0;

    public Calendar date;
    public int id;

    public Blog() {}

    public Blog(String url) {
        this.url_name = url;
    }

}
