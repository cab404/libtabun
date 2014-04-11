package com.cab404.libtabun.tests;

import com.cab404.libtabun.data.Profile;
import com.cab404.libtabun.pages.ProfilePage;
import com.cab404.libtabun.util.U;
import com.cab404.libtabun.util.modular.AccessProfile;

import java.util.Map;

/**
 * @author cab404
 */
public class UserInfoTest extends Test {

    @Override public void test(AccessProfile profile) {
        ProfilePage page = new ProfilePage("test_pony_n1");
        page.fetch(profile);

        Profile info = page.user_info;

        assertEquals(info.about, "cab404.r(63)");
        assertEquals(info.id, 17188);
        assertEquals(info.name, "example");

        for (Map.Entry<Profile.ContactType, String> contact : info.contacts.entrySet())
            U.v(contact.getKey() + ": " + contact.getValue());

        for (Map.Entry<Profile.UserInfoType, String> data : info.personal.entrySet())
            U.v(data.getKey() + ": " + data.getValue());
    }

    @Override public CharSequence title() {
        return "profile test";
    }

}
