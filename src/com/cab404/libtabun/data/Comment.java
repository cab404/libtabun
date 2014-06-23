package com.cab404.libtabun.data;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Ну тут всё ясно.
 *
 * @author cab404
 */
public class Comment implements Serializable {
    private static final long serialVersionUID = 0L;

    public String text = "";
    public Profile author;
    public int votes, parent = 0;
    public boolean deleted = false;

    public boolean is_new = false;

    public Calendar date;
    public int id;

    public Comment() {
        author = new Profile();
    }

}
