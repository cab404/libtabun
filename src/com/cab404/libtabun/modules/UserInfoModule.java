package com.cab404.libtabun.modules;

import com.cab404.libtabun.data.UserInfo;
import com.cab404.libtabun.util.SU;
import com.cab404.libtabun.util.U;
import com.cab404.libtabun.util.html_parser.HTMLTree;
import com.cab404.libtabun.util.html_parser.Tag;
import com.cab404.libtabun.util.modular.Module;

import java.util.List;

/**
 * UserInfo page module.
 *
 * @author cab404
 */
public class UserInfoModule implements Module<UserInfo> {

    @Override public UserInfo extractData(HTMLTree tree, String url) {
        UserInfo data = new UserInfo();
        tree = tree.getTree(tree.xPathFirstTag("body"));

        try {

            data.id = U.parseInt(SU.bsub(tree.xPathFirstTag("div&class=profile/div&class=vote-profile/div&id=*user_*").get("id"), "_", ""));
            data.votes = U.parseFloat(tree.xPathStr("div&class=profile/div&class=vote-profile/div/div&class=*count/span"));
            data.strength = U.parseFloat(tree.xPathStr("div&class=profile/div&class=strength/div&class=count"));
            data.nick = tree.xPathStr("div&class=profile/h2&itemprop=nickname");

            data.name = tree.xPathStr("div&class=profile/p&itemprop=name");
            data.name = data.name == null ? "" : data.name;

        } catch (Exception e) {
            throw new RuntimeException("Пользователя не существует, или произошло незнамо что.\n" + tree.html, e);
        }

        {
            data.about = tree.xPathStr("div&class=*about/p&class=text");
            data.big_icon = tree.xPathFirstTag("div&class=*about/a&class=avatar/img").get("src");
            data.fillImages();
        }

        {
            List<Tag> keys = tree.xPath("div&class=wrapper/div&class=*left/ul/li/span");
            List<Tag> values = tree.xPath("div&class=wrapper/div&class=*left/ul/li/strong");
            for (int i = 0; i < keys.size(); i++) {
                String key = SU.sub(tree.getContents(keys.get(i)), "", ":");
                String value = tree.getContents(values.get(i));

                data.personal.add(new UserInfo.Userdata(key, value));
            }

        }

        {
            List<Tag> liList = tree.xPath("div&class=wrapper/div&class=*right/ul/li");
            for (Tag tag : liList) {
                String foo = tree.getContents(tag);
                // Да, довольно криво, но так быстрее.
                String key = SU.sub(foo, "title=\"", "\"");
                String value = SU.removeAllTags(foo);
                data.contacts.add(new UserInfo.Contact(key, value));
            }
        }

        return data;
    }

}
