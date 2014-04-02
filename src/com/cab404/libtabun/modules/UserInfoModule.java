package com.cab404.libtabun.modules;

import com.cab404.libtabun.parts.UserInfo;
import com.cab404.libtabun.util.SU;
import com.cab404.libtabun.util.U;
import com.cab404.libtabun.util.html_parser.HTMLParser;
import com.cab404.libtabun.util.html_parser.Tag;
import com.cab404.libtabun.util.modular.Module;

import java.util.List;

/**
 * UserInfo page module.
 *
 * @author cab404
 */
public class UserInfoModule implements Module<UserInfo> {

    @Override public UserInfo extractData(HTMLParser parser) {
        UserInfo data = new UserInfo();

        try {

            data.id = U.parseInt(SU.bsub(parser.xPathFirstTag("div&class=profile/div&class=vote-profile/div&id=*user_*").get("id"), "_", ""));
            data.votes = U.parseFloat(parser.xPathStr("div&class=profile/div&class=vote-profile/div/div&class=*count/span"));
            data.strength = U.parseFloat(parser.xPathStr("div&class=profile/div&class=strength/div&class=count"));
            data.nick = parser.xPathStr("div&class=profile/h2&itemprop=nickname");

            data.name = parser.xPathStr("div&class=profile/p&itemprop=name");
            data.name = data.name == null ? "" : data.name;

        } catch (Exception e) {
            throw new RuntimeException("Пользователя не существует, или произошло незнамо что.\n" + parser.html, e);
        }

        {
            data.about = parser.xPathStr("div&class=*about/p&class=text");
            data.big_icon = parser.xPathFirstTag("div&class=*about/a&class=avatar/img").get("src");
            data.fillImages();
        }

        {
            List<Tag> keys = parser.xPath("div&class=wrapper/div&class=*left/ul/li/span");
            List<Tag> values = parser.xPath("div&class=wrapper/div&class=*left/ul/li/strong");
            for (int i = 0; i < keys.size(); i++) {
                String key = SU.sub(parser.getContents(keys.get(i)), "", ":");
                String value = parser.getContents(values.get(i));

                data.personal.add(new UserInfo.Userdata(key, value));
            }

        }

        {
            List<Tag> liList = parser.xPath("div&class=wrapper/div&class=*right/ul/li");
            for (Tag tag : liList) {
                String foo = parser.getContents(tag);
                // Да, довольно криво, но так быстрее.
                String key = SU.sub(foo, "title=\"", "\"");
                String value = SU.removeAllTags(foo);
                data.contacts.add(new UserInfo.Contact(key, value));
            }
        }

        return data;
    }

}
