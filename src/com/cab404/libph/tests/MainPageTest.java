package com.cab404.libph.tests;

import com.cab404.libph.pages.MainPage;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.util.tests.Test;

/**
 * @author cab404
 */
public class MainPageTest extends Test {

    @Override public void test(AccessProfile profile) {
        MainPage page = new MainPage();
        page.fetch(profile);

        assertNonNull("Разум Табуна", page.quote);
    }

}
