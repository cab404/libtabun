package com.cab404.libtabun.tests;

import com.cab404.libtabun.pages.UserListPage;
import com.cab404.libtabun.util.TabunAccessProfile;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.util.tests.Test;

/**
 * Sorry for no comments!
 * Created at 22:15 on 13/05/16
 *
 * @author cab404
 */
public class UserListTest extends Test {

    @Override
    public void test(AccessProfile accessProfile) {
        UserListPage page = new UserListPage("Ponyhawks");
        for (int i = 0; i < 2; i++) {
            page.fetch(accessProfile);
            assertEquals("Blog userlist is not empty", false, page.users.isEmpty());
            page.page++;
        }
        page = new UserListPage();
        for (int i = 0; i < 2; i++) {
            page.fetch(accessProfile);
            assertEquals("Userlist is not empty", false, page.users.isEmpty());
            page.page++;
        }

    }
}
