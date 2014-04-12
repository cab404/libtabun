package com.cab404.libtabun.modules;

import com.cab404.libtabun.util.U;
import com.cab404.libtabun.util.html_parser.HTMLTree;
import com.cab404.libtabun.util.html_parser.Tag;
import com.cab404.libtabun.util.modular.AccessProfile;

/**
 * @author cab404
 */
public class CommentNumModule extends ModuleImpl<Integer> {

    @Override public Integer extractData(HTMLTree page, AccessProfile profile) {
        return U.parseInt(page.getContents(0));
    }

    @Override public boolean doYouLikeIt(Tag tag) {
        return "span".equals(tag.name) && "count-comments".equals(tag.get("id"));
    }

}
