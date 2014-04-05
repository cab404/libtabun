package com.cab404.libtabun.util.html_parser;

import com.cab404.libtabun.facility.ResponseFactory;

import java.util.LinkedList;

/**
 * @author cab404
 */
public class ParallelHTMLParser extends Thread implements ResponseFactory.Parser {
    private final Object buffer_lock;
    private LinkedList<String> buffer;
    private TagParser tags;

    public ParallelHTMLParser() {
        this.buffer = new LinkedList<>();
        buffer_lock = new Object();
    }

    @Override public void run() {
        while (true) {

            if (!buffer.isEmpty()) {
                String line;
                synchronized (buffer_lock) {
                    line = buffer.pollLast();
                }
                if (line == null)
                    break;


            }


        }

    }

    @Override public boolean line(String line) {

        return false;
    }
}
