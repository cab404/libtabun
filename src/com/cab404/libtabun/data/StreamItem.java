package com.cab404.libtabun.data;

/**
 * @author cab404
 */
public class StreamItem {
    public static enum Type {
        VOTE_COMMENT,
        ADD_COMMENT,
        VOTE_TOPIC,
        ADD_TOPIC,
        VOTE_USER,
        JOIN_BLOG,
        VOTE_BLOG,
    }

    public Type type;
    public Profile user;

    public String date;
    public String link, data;

}
