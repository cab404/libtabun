package com.cab404.libtabun.pages;

import com.cab404.libtabun.modules.QuoteModule;
import com.cab404.libtabun.util.U;
import com.cab404.libtabun.util.html_parser.HTMLTree;

/**
 * @author cab404
 */
public class MainPage extends TabunPage {
    public String quote;

    @Override public String getURL() {
        return "/";
    }

    @Override protected void parse(HTMLTree page) {
        super.parse(page);
        U.v(page);
        quote = new QuoteModule().extractData(page, getURL());
    }
}
