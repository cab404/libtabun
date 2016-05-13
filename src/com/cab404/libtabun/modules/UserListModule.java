package com.cab404.libtabun.modules;

import com.cab404.libtabun.data.Profile;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.Module;
import com.cab404.moonlight.framework.ModuleImpl;
import com.cab404.moonlight.parser.HTMLTree;
import com.cab404.moonlight.parser.Tag;
import com.cab404.moonlight.util.SU;

import java.util.ArrayList;
import java.util.List;

/**
 * Sorry for no comments!
 * Created at 12:13 on 13/05/16
 *
 * @author cab404
 */
public class UserListModule extends ModuleImpl<List<Profile>> {
    @Override
    public List<Profile> extractData(HTMLTree htmlTree, AccessProfile accessProfile) {
        ArrayList<Profile> profiles = new ArrayList<>();
        List<Tag> tags = htmlTree.xPath("table/tbody/tr");
        for (int i = 1; i < tags.size(); i++) {
            HTMLTree tr = htmlTree.getTree(tags.get(i));
            Profile profile = new Profile();
            profile.login = tr.xPathStr("tr/td/div/p&class=*username*/a");
            profile.mid_icon = tr.xPathUnique("img&class=avatar").get("src");
            profile.name = tr.xPathStr("tr/td/div/p&class=*realname*");
            if (profile.name != null)
                profile.name = profile.name.trim();
            profile.strength = Float.valueOf(SU.removeAllTags(tr.xPathStr("tr/td&class=*cell-skill*")).trim());
            profile.votes = Float.valueOf(SU.removeAllTags(tr.xPathStr("tr/td&class=*cell-rating*")).trim());
            profile.fillImages();
            profiles.add(profile);
        }
        return profiles;
    }

    @Override
    public boolean doYouLikeIt(Tag tag) {
        return "table".equals(tag.name) && tag.get("class").contains("table-users");
    }
}
