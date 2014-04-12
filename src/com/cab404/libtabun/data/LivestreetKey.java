package com.cab404.libtabun.data;

/**
 * @author cab404
 */
public class LivestreetKey {
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
