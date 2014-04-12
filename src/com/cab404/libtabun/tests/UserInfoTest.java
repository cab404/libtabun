package com.cab404.libtabun.tests;

import com.cab404.libtabun.data.Profile;
import com.cab404.libtabun.pages.ProfilePage;
import com.cab404.libtabun.tests.base.Test;
import com.cab404.libtabun.util.modular.AccessProfile;

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

        assertEquals(info.contacts.get(Profile.ContactType.PHONE), "0000000");
        assertEquals(info.contacts.get(Profile.ContactType.EMAIL), "exa@mp.le");
        assertEquals(info.contacts.get(Profile.ContactType.TWITTER), "example");
        assertEquals(info.contacts.get(Profile.ContactType.VKONTAKTE), "example");
        assertEquals(info.contacts.get(Profile.ContactType.ODNOKLASSNIKI), "example");
        assertEquals(info.contacts.get(Profile.ContactType.SITE), "example.com");

        assertEquals(info.personal.get(Profile.UserInfoType.BIRTHDAY), "6 марта 2014");
        assertEquals(info.personal.get(Profile.UserInfoType.GENDER), "женский");
    }

    @Override public CharSequence title() {
        return "Profile test";
    }

}
