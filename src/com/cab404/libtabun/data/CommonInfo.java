package com.cab404.libtabun.data;

import java.io.Serializable;

/**
 * @author cab404
 */
public class CommonInfo implements Serializable {
    private static final long serialVersionUID = 0L;

    public String username, avatar;
    public float rating, strength;
    public int new_messages;
}
