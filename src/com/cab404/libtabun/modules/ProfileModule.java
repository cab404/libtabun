package com.cab404.libtabun.modules;

import com.cab404.libtabun.data.Profile;
import com.cab404.libtabun.util.SU;
import com.cab404.libtabun.util.U;
import com.cab404.libtabun.util.html_parser.HTMLTree;
import com.cab404.libtabun.util.html_parser.Tag;
import com.cab404.libtabun.util.modular.AccessProfile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Profile page module.
 *
 * @author cab404
 */
public class ProfileModule extends ModuleImpl<Profile> {

    @Override public Profile extractData(HTMLTree page, AccessProfile profile) {
        Profile data = new Profile();
//        page = page.getTree(page.xPathFirstTag("body"));

        try {

            data.id = U.parseInt(SU.bsub(page.xPathFirstTag("div&class=profile/div&class=vote-profile/div&id=*user_*").get("id"), "_", ""));
            data.votes = U.parseFloat(page.xPathStr("div&class=profile/div&class=vote-profile/div/div&class=*count/span"));
            data.strength = U.parseFloat(page.xPathStr("div&class=profile/div&class=strength/div&class=count"));
            data.nick = page.xPathStr("div&class=profile/h2&itemprop=nickname");

            data.name = page.xPathStr("div&class=profile/p&itemprop=name");
            data.name = data.name == null ? "" : SU.deEntity(data.name);

        } catch (Exception e) {
            throw new RuntimeException("Пользователя не существует, или произошло незнамо что.", e);
        }

        {
            data.about = page.xPathStr("div&class=*about/div&class=text");
            data.big_icon = page.xPathFirstTag("div&class=*about/a&class=avatar/img").get("src");
            data.fillImages();
        }

        {
            List<Tag> keys = page.xPath("div&class=wrapper/div&class=*left/ul/li/span");
            List<Tag> values = page.xPath("div&class=wrapper/div&class=*left/ul/li/strong");
            for (int i = 0; i < keys.size(); i++) {
                String key = SU.sub(page.getContents(keys.get(i)), "", ":");
                String value = page.getContents(values.get(i));

                data.personal.add(new Profile.Userdata(key, value));
            }

            List<Tag> friends = page.xPath("div&class=wrapper/div&class=*left/ul&class=user-list-avatar/li/a");
            data.partial_friend_list = new ArrayList<>();
            for (Tag tag : friends) {
                data.partial_friend_list.add(SU.sub(tag.get("href"), "profile/", "/"));
            }
            data.partial_friend_list = Collections.unmodifiableList(data.partial_friend_list);
        }

        {
            List<Tag> liList = page.xPath("div&class=wrapper/div&class=*right/ul/li");
            for (Tag tag : liList) {
                String foo = page.getContents(tag);
                // Да, довольно криво, но так быстрее.
                String key = SU.sub(foo, "title=\"", "\"");
                String value = SU.removeAllTags(foo);
                data.contacts.add(new Profile.Contact(key, value));
            }
        }
        finish();
        return data;
    }

    @Override public boolean doYouLikeIt(Tag tag) {
        return "body".equals(tag.name);
    }

}
