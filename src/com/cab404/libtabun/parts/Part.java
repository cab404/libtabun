package com.cab404.libtabun.parts;

import java.util.Calendar;

/**
 * @author cab404
 */
abstract class Part {
    LivestreetKey key;
    String type;
    Calendar date;

    boolean vote_enabled = false, voted = false;
    int your_vote = 0;


    public int id;
}
