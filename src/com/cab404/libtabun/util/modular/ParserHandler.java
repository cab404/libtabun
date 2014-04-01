package com.cab404.libtabun.util.modular;

/**
 * @author cab404
 */
public interface ParserHandler<T extends HandledParser> {
    public void handle(T parser);
}
