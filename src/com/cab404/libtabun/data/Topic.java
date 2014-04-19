package com.cab404.libtabun.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * @author cab404
 */
public class Topic {
    public CharSequence title, votes;
    public Profile author;
    public Blog blog;

    public CharSequence text;
    public List<CharSequence> tags;

    public Calendar date;
    public int id;

    public boolean vote_enabled = false;
    public boolean in_favourites = false;
    public boolean voted = false;

    public int your_vote = 0;

    public int comments = 0, comments_new = 0;

    public Topic() {
        tags = new ArrayList<>();
        blog = new Blog();
        author = new Profile();
    }
}
