package com.cab404.libtabun.data;

import java.io.Serializable;

/**
 * @author cab404
 */
public class LivestreetKey implements Serializable {
    private static final long serialVersionUID = 0L;

    public String address, key;


    public LivestreetKey(String address, String key) {
        this.address = address;
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }

}
