package com.cab404.libtabun.modules;

import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.ModuleImpl;
import com.cab404.moonlight.parser.HTMLTree;
import com.cab404.moonlight.parser.Tag;
import com.cab404.moonlight.parser.TagMatcher;
import com.cab404.moonlight.util.SU;

import java.util.HashMap;

/**
 * Sorry for no comments!
 * Created at 12:01 on 31/01/16
 *
 * @author cab404
 */
public class CommentTreeModule extends ModuleImpl<HashMap<Integer, Integer>> {

    protected static final TagMatcher MATCHER = new TagMatcher("div&class=comment-wrapper");

    public HashMap<Integer, Integer> parents = new HashMap<>();

    @Override
    public HashMap<Integer, Integer> extractData(HTMLTree page, AccessProfile profile) {
        Tag head = page.get(0);
        int id = Integer.parseInt(SU.bsub(head.get("id"), "id_", ""));
        for (Tag tag : page.xPath("div&class=comment-wrapper")) {
            if (tag != head && MATCHER.matches(tag)){
                int child_id = Integer.parseInt(SU.bsub(tag.get("id"), "id_", ""));
                parents.put(child_id, id);
            }
        }
        return parents;
    }

    @Override
    public boolean doYouLikeIt(Tag tag) {
        return MATCHER.matches(tag);
    }
}
