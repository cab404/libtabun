package com.cab404.libtabun.modules;

import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.ModuleImpl;
import com.cab404.moonlight.parser.HTMLTree;
import com.cab404.moonlight.parser.Tag;
import com.cab404.moonlight.parser.TagMatcher;
import com.cab404.moonlight.util.SU;

/**
 * @author cab404
 */
public class BlogIdModule extends ModuleImpl<Integer> {

    private TagMatcher matcher = new TagMatcher("span&id=blog_user_count_*");

    @Override
    public Integer extractData(HTMLTree htmlTree, AccessProfile accessProfile) {
        finish();
        return Integer.valueOf(SU.bsub(htmlTree.get(0).get("id"), "_", ""));
    }

    @Override
    public boolean doYouLikeIt(Tag tag) {
        return matcher.matches(tag);
    }

}
