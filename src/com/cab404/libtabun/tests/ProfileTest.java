package com.cab404.libtabun.tests;

import com.cab404.libtabun.data.Profile;
import com.cab404.libtabun.pages.ProfilePage;
import com.cab404.moonlight.util.tests.Test;
import com.cab404.moonlight.framework.AccessProfile;

/**
 * @author cab404
 */
public class ProfileTest extends Test {

    @Override public void test(AccessProfile profile) {
        ProfilePage page = new ProfilePage("test_pony_n1");
        page.fetch(profile);

        Profile info = page.user_info;

        assertEquals("About", info.about, "cab404.r(63)");
        assertEquals("ID", info.id, 17188);
        assertEquals("Name", info.name, "example");

        assertEquals("Телефон", info.contacts.get(Profile.ContactType.PHONE), "0000000");
        assertEquals("E-mail", info.contacts.get(Profile.ContactType.EMAIL), "exa@mp.le");
        assertEquals("Twitter", info.contacts.get(Profile.ContactType.TWITTER), "example");
        assertEquals("ВКонтакте", info.contacts.get(Profile.ContactType.VKONTAKTE), "example");
        assertEquals("Одноклассники", info.contacts.get(Profile.ContactType.ODNOKLASSNIKI), "example");
        assertEquals("Сайт", info.contacts.get(Profile.ContactType.SITE), "example.com");

        assertEquals("День рождения", info.personal.get(Profile.UserInfoType.BIRTHDAY), "6 марта 2014");
        assertEquals("Пол", info.personal.get(Profile.UserInfoType.GENDER), "женский");
    }

}
