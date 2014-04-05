package com.cab404.libtabun.facility;

import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.DeflaterInputStream;
import java.util.zip.GZIPInputStream;

/**
 * @author cab404
 */
public class ResponseFactory {

    public static interface Parser {
        public boolean line(String line);
    }

    /**
     * Каждую новую полученную строку пропускает через listener
     *
     * @param listener Parser, слушающий каждую новую строку
     * @param response Сбснна, откуда слушать.
     */
    public static void read(HttpResponse response, Parser listener) {
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(getRightInputStream(response)));
            String line;
            while ((line = reader.readLine()) != null && listener.line(line)) ;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Каждую новую полученную строку пропускает через listener
     *
     * @param listener Parser, слушающий каждую новую строку
     * @param response Сбснна, откуда слушать.
     * @param status   Статус загрузки ответа.
     */
    public static void read(HttpResponse response, Parser listener, StatusListener status) {
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(getRightInputStream(response)));
            String line;

            long length = response.getEntity().getContentLength();
            if (length == -1) length = 1;
            long loaded = 0;

            while ((line = reader.readLine()) != null) {
                listener.line(line);
                loaded += line.length();
                status.onProgressChange(loaded, length);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see String read(HttpResponse, Parser)
     */
    public static String read(HttpResponse response, StatusListener status) {
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(getRightInputStream(response)));
            String line, page = "";

            long length = response.getEntity().getContentLength();
            if (length == -1) length = 1;
            long loaded = 0;

            while ((line = reader.readLine()) != null) {
                page += line + "\n";
                loaded += line.length() + 1;
                status.onProgressChange(loaded, length);
            }

            return page;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see String read(HttpResponse, Parser)
     */
    public static String read(HttpResponse response) {
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(getRightInputStream(response)));
            String line, page = "";

            while ((line = reader.readLine()) != null)
                page += line + "\n";

            return page;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Слушалка всяких там событий.
     */
    public static abstract class StatusListener {

        public void onResponseStart() {}
        public void onResponseFail(Throwable t) {
            throw new RuntimeException(t);
        }
        public void onResponseFinished() {}

        public void onLoadingStarted() {}
        public void onLoadingFail(Throwable t) {
            throw new RuntimeException(t);
        }
        public void onProgressChange(long loaded, long length) {}
        public void onLoadingFinished() {}

        public void onParseStarted() {}
        public void onParseFail(Throwable t) {
            throw new RuntimeException(t);
        }
        public void onParseFinished() {}

        public void onFinish() {}

    }

    /**
     * Достаёт из ответа поток, и завёртывает его в нужный разпаковывающий поток (если нужно).
     */
    private static InputStream getRightInputStream(HttpResponse response) {
        try {
            if (response.containsHeader("Content-Encoding")) {
                String encoding = response.getFirstHeader("Content-Encoding").getValue();
                if ("gzip".equals(encoding))
                    return new GZIPInputStream(response.getEntity().getContent());
                if ("deflate".equals(encoding))
                    return new DeflaterInputStream(response.getEntity().getContent());
            } else
                return response.getEntity().getContent();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }


}
