package com.cab404.libtabun.pages;

import com.cab404.libtabun.data.Profile;
import com.cab404.libtabun.modules.UserInfoModule;
import com.cab404.libtabun.util.html_parser.HTMLTree;

/**
 * @author cab404
 */
public class ProfilePage extends TabunPage {
    public final String username;
    public Profile user_info;

    public ProfilePage(String username) {
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
