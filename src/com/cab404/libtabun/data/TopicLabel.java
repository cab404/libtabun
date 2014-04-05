package com.cab404.libtabun.data;

import com.cab404.libtabun.parts.Blog;

/**
 * @author cab404
 */
public class TopicLabel extends Part {
    public String name, votes;
    public Profile author;
    public String content, time;
    public String[] tags;
    public Blog blog;

    public boolean vote_enabled = false, voted = false;
    public boolean isInFavs = false;
    public int your_vote = 0;

    public int comments = 0, comments_new = 0;


    public TopicLabel() {
        name = votes = time = content = "";
    }
}
