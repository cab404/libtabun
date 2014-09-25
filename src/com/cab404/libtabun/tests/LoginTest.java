package com.cab404.libtabun.tests;

import com.cab404.libtabun.pages.TabunPage;
import com.cab404.libtabun.requests.LoginRequest;
import com.cab404.libtabun.util.TabunAccessProfile;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.util.tests.Test;

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

        assertEquals("Logged in (long form)", true, new LoginRequest(login, password).exec(profile, page).success());
        assertEquals("Logged in (short form)", true, new TabunAccessProfile().login(login, password));


        AccessProfile copy = TabunAccessProfile.parseString(profile.serialize());
	    TabunPage test = new TabunPage();
        test.fetch(copy);

        assertNonNull("Copied account usage", test.c_inf);
        assertEquals("Username from header", login, test.c_inf.username);

    }



}
