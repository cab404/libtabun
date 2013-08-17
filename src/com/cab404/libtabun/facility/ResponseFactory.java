package com.cab404.libtabun.facility;

import com.cab404.libtabun.U;
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
        BufferedReader reader = new BufferedReader(new InputStreamReader(getRightInputStream(response)));
        String line;
        try {
            while ((line = reader.readLine()) != null && listener.line(line)) ;
        } catch (IOException e) {
            U.w(e);
        }
    }

    /**
     * @see String read(HttpResponse, Parser)
     */
    public static String read(HttpResponse response) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(getRightInputStream(response)));
        String line, page = "";
        try {
            while ((line = reader.readLine()) != null) {
                page += line + "\n";
            }

        } catch (IOException e) {
            U.w(e);
        }
        return page;
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
            e.printStackTrace();
        }

        return null;
    }


}
