package com.cab404.libtabun.tests;

import com.cab404.libtabun.pages.TabunPage;
import com.cab404.libtabun.requests.LoginRequest;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.tests.Test;

/**
 * @author cab404
 */
public class LoginTest extends Test {

    @Override public void test(AccessProfile profile) {
        TabunPage page = new TabunPage();
        page.fetch(profile);

        String login, password;
        login = requestString("Login");
        password = requestPassword("Password");

        assertEquals("Logged in", true, new LoginRequest(login, password).exec(profile, page).success());
    }

}
