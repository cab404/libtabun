package com.cab404.libtabun.data;

import com.cab404.libtabun.parts.LivestreetKey;

import java.util.Calendar;

/**
 * @author cab404
 */
public abstract class Part {
    public LivestreetKey key;
    public String type;
    public Calendar date;

    public int id;
}