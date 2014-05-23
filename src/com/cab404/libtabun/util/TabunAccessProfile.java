package com.cab404.libtabun.util;

import com.cab404.libtabun.pages.TabunPage;
import com.cab404.libtabun.requests.LoginRequest;
import com.cab404.moonlight.framework.AccessProfile;

/**
 * @author cab404
 */
public class TabunAccessProfile extends AccessProfile {
    public TabunAccessProfile() {
        super("tabun.everypony.ru", 80);
    }

    public boolean login(String name, String password) {
        TabunPage page = new TabunPage();
        page.fetch(this);
        return new LoginRequest(name, password).exec(this, page).success();
    }

    public static TabunAccessProfile parseString(String s) {
        TabunAccessProfile _return = new TabunAccessProfile();
        _return.setUpFromString(s);
        return _return;
    }

    @Override public String userAgentName() {
        return "sweetieBot";
    }
}
