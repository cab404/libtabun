package com.cab404.libtabun.pages;

import com.cab404.libtabun.data.UserInfo;
import com.cab404.libtabun.modules.UserInfoModule;
import com.cab404.libtabun.util.html_parser.HTMLTree;

/**
 * @author cab404
 */
public class UserdataPage extends MainPage {
    public final String username;
    public UserInfo user_info;

    public UserdataPage(String username) {
        this.username = username;
    }

    @Override public String getURL() {
        return "/profile/" + username;
    }
    @Override protected void parse(HTMLTree page) {
        super.parse(page);
        user_info = new UserInfoModule().extractData(page, getURL());
    }
}
