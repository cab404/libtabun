package com.cab404.libtabun.pages;

import com.cab404.libtabun.data.TopicLabel;
import com.cab404.libtabun.util.html_parser.HTMLTree;
import com.cab404.libtabun.util.loaders.Page;

import java.io.FileWriter;
import java.io.IOException;

/**
 * @author cab404
 */
public class TopicPage extends Page {

    private int id;

    TopicLabel header;

    public TopicPage(int id) {
        this.id = id;
    }

    @Override public String getURL() {
        return "/blog/" + id + ".html";
    }

    @Override protected void parse(HTMLTree page) {
        try {
            FileWriter writer = new FileWriter("post");
            writer.write(page.toString());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
