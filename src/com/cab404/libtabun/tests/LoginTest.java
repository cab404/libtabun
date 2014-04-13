package com.cab404.libtabun.tests;

import com.cab404.libtabun.pages.TabunPage;
import com.cab404.libtabun.requests.LoginRequest;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.tests.Test;
import com.cab404.moonlight.util.U;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author cab404
 */
public class LoginTest extends Test {

    @Override public void test(AccessProfile profile) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        TabunPage page = new TabunPage();
        page.fetch(profile);

        String login, password;
        try {
            U.v("Login:");
            login = reader.readLine();
            U.v("Password:");
            password = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertEquals("Logged in", true, new LoginRequest(login, password).exec(profile, page).success());
    }

}
