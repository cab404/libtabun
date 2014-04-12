package com.cab404.libtabun.data;

import com.cab404.libtabun.parts.PaWPoL;

import java.util.ArrayList;

/**
 * PaWPoL с рейтингом, описанием и читателями.
 *
 * @author cab404
 */
public class Blog extends PaWPoL {
    public String name, url_name;
    public ArrayList<Topic> posts;
    public int numpages = 0;
    public int curpage = 0;
}
