package com.cab404.libtabun.pages;

import com.cab404.libtabun.data.Profile;
import com.cab404.libtabun.modules.ProfileModule;
import com.cab404.libtabun.util.SU;
import com.cab404.libtabun.util.html_parser.HTMLTree;
import com.cab404.libtabun.util.modular.AccessProfile;

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
        return "/profile/" + SU.rl(username);
    }
    @Override protected void parse(HTMLTree page, AccessProfile profile) {
        super.parse(page, profile);
        user_info = new ProfileModule().extractData(page, profile);
    }
}
