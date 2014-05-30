package com.cab404.libtabun.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author cab404
 */
public class Letter implements Serializable {
    private static final long serialVersionUID = 0L;

    public String title;
    public String text;
    public List<String> recipients;
    public Profile starter;
    public Calendar date;
    public int id;

    public int comments = 0, comments_new = 0;

    public Letter() {
        recipients = new ArrayList<>();
        starter = new Profile();
    }
}
