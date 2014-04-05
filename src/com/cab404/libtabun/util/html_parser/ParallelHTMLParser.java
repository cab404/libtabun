package com.cab404.libtabun.util.html_parser;

import com.cab404.libtabun.facility.ResponseFactory;
import com.cab404.libtabun.util.U;

import java.util.LinkedList;

/**
 * @author cab404
 */
public class ParallelHTMLParser extends Thread implements ResponseFactory.Parser {
    private final Object buffer_lock, working_lock;
    private LinkedList<String> queue;
    private TagParser parser;

    public TagParser getParser() {
        synchronized (working_lock) {
            return parser;
        }
    }

    public ParallelHTMLParser() {
        this.queue = new LinkedList<>();
        buffer_lock = new Object();
        working_lock = new Object();
        parser = new TagParser();
    }

    @Override public void run() {
        synchronized (working_lock) {

            while (true) {

                if (!queue.isEmpty()) {
                    String line;

                    synchronized (buffer_lock) {
                        line = queue.pollFirst();
                    }

                    if (line == null)
                        break;

                    U.v(line);
                    parser.process(line);

                }

            }

        }

    }

    @Override public boolean line(String line) {
        if (!this.isAlive())
            start();

        synchronized (buffer_lock) {
            queue.add(line);
        }

        return true;
    }

    @Override public void finished() {
        queue.add(null);
    }
}
