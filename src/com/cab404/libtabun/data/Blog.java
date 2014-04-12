package com.cab404.libtabun.data;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * PaWPoL с рейтингом, описанием и читателями.
 *
 * @author cab404
 */
public class Blog {
    public String name, url_name;
    public ArrayList<Topic> posts;
    public int numpages = 0;
    public int curpage = 0;

    public Calendar date;
    public int id;
}
