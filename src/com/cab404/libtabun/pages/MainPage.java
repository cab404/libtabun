package com.cab404.libtabun.pages;

import com.cab404.libtabun.data.CommonInfo;
import com.cab404.libtabun.modules.CommonInfoModule;
import com.cab404.libtabun.util.html_parser.HTMLTree;
import com.cab404.libtabun.util.modular.Page;

/**
 * @author cab404
 */
public class MainPage extends Page {

    public CommonInfo c_inf;

    @Override
    public String getURL() {
        return "/";
    }

    @Override
    protected void parse(HTMLTree page) {
        c_inf = new CommonInfoModule().extractData(page, getURL());
    }

}
