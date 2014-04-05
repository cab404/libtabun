package com.cab404.libtabun.parts;

import com.cab404.libtabun.util.SU;
import com.cab404.libtabun.facility.ResponseFactory;

/**
 * Загружает и проверяет строку за строкой в поисках ключа Livestreet. Заодно узнаёт имя пользователя.
 */
public class KeyFetcher implements ResponseFactory.Parser {
    int part = 0;
    public LivestreetKey key;

    @Override
    public boolean line(String line) {
        switch (part) {
            case 0:
                if (line.contains("var LIVESTREET_SECURITY_KEY")) {
                    key = new LivestreetKey("/", SU.sub(
                            line,
                            "var LIVESTREET_SECURITY_KEY = '",
                            "';"
                    ));
                    part++;
                    return false;
                }
        }
        return true;
    }
    @Override public void finished() {

    }
}