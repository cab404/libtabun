package com.cab404.libtabun.data;

import com.cab404.libtabun.util.JSONable;

import java.util.Calendar;

/**
 * Ну тут всё ясно.
 *
 * @author cab404
 */
public class Comment extends JSONable {
    @JSONField
    public String text = "";
    @JSONField
    public Profile author;
    @JSONField
    public int votes, parent = 0;
    @JSONField
    public boolean deleted = false;

    @JSONField
    public boolean is_new = false;
    @JSONField
    public boolean in_favs = false;

    @JSONField
    public Calendar date;
    @JSONField
    public int id;

    public Comment() {
        author = new Profile();
    }

}
