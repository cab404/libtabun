package com.cab404.libtabun.tests;

import com.cab404.libtabun.pages.TabunPage;
import com.cab404.libtabun.requests.UserAutocompleteRequest;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.util.tests.Test;

/**
 * @author cab404
 */
public class UserAutocompleteTest extends Test {


    @Override public void test(AccessProfile profile) {
        TabunPage tabunPage = new TabunPage();
        tabunPage.fetch(profile);
        UserAutocompleteRequest req = new UserAutocompleteRequest("Orhide").exec(profile);

        assertEquals("Результаты поиска", req.names.toArray(), new String[]{"orhidea", "Orhideous"});

    }

}
