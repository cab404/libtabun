package com.cab404.libph.tests;

import com.cab404.libph.data.Profile;
import com.cab404.libph.pages.ProfilePage;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.util.tests.Test;

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

        assertEquals("Телефон", info.get(Profile.ContactType.PHONE).get(0), "0000000");
        assertEquals("E-mail", info.get(Profile.ContactType.EMAIL).get(0), "exa@mp.le");
        assertEquals("Twitter", info.get(Profile.ContactType.TWITTER).get(0), "example");
        assertEquals("ВКонтакте", info.get(Profile.ContactType.VKONTAKTE).get(0), "example");
        assertEquals("Одноклассники", info.get(Profile.ContactType.ODNOKLASSNIKI).get(0), "example");
        assertEquals("Сайт", info.get(Profile.ContactType.SITE).get(0), "example.com");

        assertEquals("День рождения", info.get(Profile.UserInfoType.BIRTHDAY), "6 марта 2014");
        assertEquals("Пол", info.get(Profile.UserInfoType.GENDER), "женский");

        assertEquals("Фото", info.photo, "http://tabun.everypony.ru/templates/skin/synio/images/user_photo_female.png");
    }

}
