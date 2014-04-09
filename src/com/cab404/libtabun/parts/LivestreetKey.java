package com.cab404.libtabun.parts;

import com.cab404.libtabun.util.SU;
import com.cab404.libtabun.facility.RequestFactory;
import com.cab404.libtabun.facility.ResponseFactory;

/**
 * @author cab404
 */
public class LivestreetKey {
    public String address, key;


    public LivestreetKey(String address, String key) {
        this.address = address;
        this.key = key;
    }

    public LivestreetKey(User user, String address) {
        ResponseFactory.read(
                user.execute(
                        RequestFactory.get(address).build()
                ),
                new KeyParser()
        );
    }

    @Override
    public String toString() {
        return key;
    }

    private class KeyParser implements ResponseFactory.Parser {

        @Override
        public boolean line(String line) {
            if (line.contains("var LIVESTREET_SECURITY_KEY")) {
                key = SU.sub(
                        line,
                        "var LIVESTREET_SECURITY_KEY = '",
                        "';"
                );
                return false;
            }
            return true;
        }

        @Override public void finished() {

        }
    }
}
