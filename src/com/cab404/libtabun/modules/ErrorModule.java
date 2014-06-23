package com.cab404.libtabun.modules;

import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.ModuleImpl;
import com.cab404.moonlight.parser.HTMLTree;
import com.cab404.moonlight.parser.Tag;

/**
 * @author cab404
 */
public class ErrorModule extends ModuleImpl<Object> {

    @Override public Object extractData(HTMLTree page, AccessProfile profile) {
        return new Object();
    }

    @Override public boolean doYouLikeIt(Tag tag) {
        return tag.get("class").equals("content-error");
    }

}
