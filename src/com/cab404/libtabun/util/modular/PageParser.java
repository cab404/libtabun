package com.cab404.libtabun.util.modular;

import com.cab404.libtabun.facility.ResponseFactory;
import com.cab404.libtabun.util.html_parser.HTMLTree;

/**
 * @author cab404
 */
public class PageParser implements ResponseFactory.Parser {
    private StringBuilder data;

    public PageParser() {
        data = new StringBuilder();
    }

    @Override public boolean line(String line) {
        data.append(line).append("\n");
        return true;
    }


    public HTMLTree getPage() {
        return new HTMLTree(data.toString());
    }

}
