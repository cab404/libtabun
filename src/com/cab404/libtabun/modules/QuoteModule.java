package com.cab404.libtabun.modules;

import com.cab404.libtabun.util.html_parser.HTMLTree;
import com.cab404.libtabun.util.modular.Module;

/**
 * @author cab404
 */
public class QuoteModule implements Module<String> {

    @Override public String extractData(HTMLTree page, String url) {
        return page.xPathStr("html/body/" +
                "div&id=container/" +
                "div&id=wrapper/" +
                "aside&id=sidebar/" +
                "section&class=block block-type-tags/" +
                "div&class=quote");
    }

}
