package com.cab404.libph.tests;

import com.cab404.libph.pages.TabunPage;
import com.cab404.libph.requests.LoginRequest;
import com.cab404.libph.util.PHAccessProfile;
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
        assertEquals("Logged in (short form)", true, new PHAccessProfile().login(login, password));


        AccessProfile copy = PHAccessProfile.parseString(profile.serialize());
	    TabunPage test = new TabunPage();
        test.fetch(copy);

        assertNonNull("Copied account usage", test.c_inf);
        assertEquals("Username from header", login, test.c_inf.username);

    }



}
