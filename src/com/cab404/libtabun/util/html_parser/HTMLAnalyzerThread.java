package com.cab404.libtabun.util.html_parser;

import com.cab404.libtabun.util.U;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Поток обработки древа тегов.
 *
 * @author cab404
 */
public class HTMLAnalyzerThread extends Thread implements TagParser.TagHandler {
    private CopyOnWriteArrayList<Tag> queue;
    private LevelAnalyzer analyzer;
    private final Object working_lock;
    public boolean started = false;


    public HTMLAnalyzerThread() {
        queue = new CopyOnWriteArrayList<>();
        working_lock = new Object();
        this.analyzer = new LevelAnalyzer();
        setDaemon(true);
    }

    public LevelAnalyzer getLevelAnalyzer() {
        U.Timer timer = new U.Timer();
        if (started)
            synchronized (working_lock) {
                timer.log("Waited analyzer for :time:");
                return analyzer;
            }
        else
            return null;
    }

    @Override public void run() {
        synchronized (working_lock) {
            started = true;

            while (true) {

                if (!queue.isEmpty()) {
                    Tag tag = queue.remove(0);
                    if (tag == null)
                        break;
                    analyzer.add(tag);
                }

            }

        }
    }

    @Override public void handle(Tag tag) {
        queue.add(tag);
    }

    public void finished() {
        queue.add(null);
    }

}
