package com.cab404.libtabun.modules;

import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.ModuleImpl;
import com.cab404.moonlight.parser.HTMLTree;
import com.cab404.moonlight.parser.Tag;
import com.cab404.moonlight.util.U;

/**
 * @author cab404
 */
public class CommentNumModule extends ModuleImpl<Integer> {

    @Override public Integer extractData(HTMLTree page, AccessProfile profile) {
        finish();
        return U.parseInt(page.getContents(0));
    }

    @Override public boolean doYouLikeIt(Tag tag) {
        return "span".equals(tag.name) && "count-comments".equals(tag.get("id"));
    }

}
