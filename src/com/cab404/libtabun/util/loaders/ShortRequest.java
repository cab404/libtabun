package com.cab404.libtabun.util.loaders;

import com.cab404.libtabun.facility.ResponseFactory;

/**
 * Реквест на какую-нибудь мелочь, которая не требует параллельного парсинга.
 *
 * @author cab404
 */
public abstract class ShortRequest extends Request {

    public abstract void handleResponse(String response);


    @Override public void finished(ResponseFactory.Parser parser) {
        handleResponse(((Stringifyer) parser).text.toString());
    }

    @Override public ResponseFactory.Parser getParser() {
        return new Stringifyer();
    }

    private class Stringifyer implements ResponseFactory.Parser {
        StringBuilder text;

        private Stringifyer() {
            text = new StringBuilder();
        }

        @Override public boolean line(String line) {
            text.append(line).append("\n");
            return true;
        }

        @Override public void finished() {}
    }

}
