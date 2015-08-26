package com.cab404.libtabun.data;

import com.cab404.libtabun.util.JSONable;

/**
 * @author cab404
 */
public class LivestreetKey extends JSONable {

    @JSONField
    public String key;

    @Deprecated
    public LivestreetKey(String address, String key) {
        this.key = key;
    }

    public LivestreetKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }

}
