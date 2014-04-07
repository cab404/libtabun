package com.cab404.libtabun.pages;

import com.cab404.libtabun.modules.QuoteModule;
import com.cab404.libtabun.util.html_parser.HTMLTree;
import com.cab404.libtabun.util.modular.AccessProfile;

/**
 * @author cab404
 */
public class MainPage extends TabunPage {
    public String quote;

    @Override public String getURL() {
        return "/";
    }

    @Override protected void parse(HTMLTree page, AccessProfile profile) {
        super.parse(page, profile);
        quote = new QuoteModule().extractData(page, profile);
    }
}
