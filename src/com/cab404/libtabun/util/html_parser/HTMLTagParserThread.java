package com.cab404.libtabun.util.html_parser;

import com.cab404.libtabun.facility.ResponseFactory;
import com.cab404.libtabun.util.U;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Загружает страницу и парсит теги в отдельном потоке.
 *
 * @author cab404
 */
public class HTMLTagParserThread extends Thread implements ResponseFactory.Parser {
    private final Object working_lock;
    private CopyOnWriteArrayList<String> queue;
    private TagParser parser;
    private HTMLAnalyzerThread bonded_analyzer;

    public TagParser getTagParser() {
        U.Timer timer = new U.Timer();
        synchronized (working_lock) {
            timer.log("Waited parser for :time:");
            return parser;
        }
    }

    public void bondWithAnalyzer(HTMLAnalyzerThread bonded_analyzer) {
        this.bonded_analyzer = bonded_analyzer;
    }

    public HTMLTagParserThread(TagParser.TagHandler handler) {
        this.queue = new CopyOnWriteArrayList<>();
        working_lock = new Object();
        parser = new TagParser(handler);
        setDaemon(true);
    }

    @Override public void run() {
        synchronized (working_lock) {

            while (true) {

                if (!queue.isEmpty()) {
                    String line;

                    line = queue.remove(0);

                    if (line == null)
                        break;

                    parser.process(line + "\n");


                }
            }

        }

        if (bonded_analyzer != null)
            bonded_analyzer.finished();

    }

    @Override public boolean line(String line) {
        if (!this.isAlive())
            start();

        queue.add(line);

        return true;
    }

    @Override public void finished() {
        queue.add(null);
    }
}
