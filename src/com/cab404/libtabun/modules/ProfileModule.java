package com.cab404.libtabun.modules;

import com.cab404.libtabun.data.Profile;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.ModuleImpl;
import com.cab404.moonlight.parser.HTMLTree;
import com.cab404.moonlight.parser.Tag;
import com.cab404.moonlight.util.SU;
import com.cab404.moonlight.util.U;

import java.util.AbstractMap;
import java.util.List;

/**
 * Profile page module.
 *
 * @author cab404
 */
public class ProfileModule extends ModuleImpl<Profile> {
    Profile data = new Profile();

    @Override
    public Profile extractData(HTMLTree page, AccessProfile profile) {

        if ("profile-photo-wrapper".equals(page.get(0).get("class"))) {
            data.photo = page.getTagByID("foto-img").get("src");
            return null;
        }


        try {

            data.id = U.parseInt(SU.bsub(page.xPathFirstTag("div&class=profile/div&class=vote-profile/div&id=*user_*").get("id"), "_", ""));
            data.votes = U.parseFloat(page.xPathStr("div&class=profile/div&class=vote-profile/div/div&class=*count/span"));
            data.strength = U.parseFloat(page.xPathStr("div&class=profile/div&class=strength/div&class=count"));
            data.login = page.xPathStr("div&class=profile/h2&itemprop=nickname");

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

                data.personal.add(
                        new AbstractMap.SimpleEntry<>(
                                Profile.getDataType(key),
                                SU.removeRecurringChars(
                                        value.replace('\t', ' ').replace('\n', ' ').trim(),
                                        ' '
                                ).toString()
                        )
                );
            }

            List<Tag> friends = page.xPath("div&class=wrapper/div&class=*left/ul&class=user-list-avatar/li/a");
            for (Tag tag : friends) {
                Profile friend = new Profile();
                friend.login = SU.sub(tag.get("href"), "profile/", "/");
                // Иконка идёт следующей, так что достанем её мануально. Это быстрее с любой точки зрения.
                friend.mid_icon = page.get(tag.index - page.offset() + 1).get("src");
                friend.fillImages();
                data.partial_friend_list.add(friend);
            }
        }

        {
            List<Tag> liList = page.xPath("div&class=wrapper/div&class=*right/ul/li");
            for (Tag tag : liList) {
                String foo = page.getContents(tag);
                // Да, довольно криво, но так быстрее.
                String key = SU.sub(foo, "title=\"", "\"");
                String value = SU.removeAllTags(foo);
                data.contacts.add(
                        new AbstractMap.SimpleEntry<>(
                                Profile.getContactType(key),
                                SU.removeRecurringChars(
                                        value.replace('\t', ' ').replace('\n', ' ').trim(),
                                        ' '
                                ).toString()
                        )
                );
            }
        }
        finish();
        return data;
    }

    @Override
    public boolean doYouLikeIt(Tag tag) {
        return ("content".equals(tag.get("id"))) || ("profile-photo-wrapper".equals(tag.get("class")));
    }

}
