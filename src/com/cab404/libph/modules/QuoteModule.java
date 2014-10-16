package com.cab404.libph.modules;

import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.ModuleImpl;
import com.cab404.moonlight.parser.HTMLTree;
import com.cab404.moonlight.parser.Tag;

/**
 * @author cab404
 */
public class QuoteModule extends ModuleImpl<String> {

    @Override public String extractData(HTMLTree page, AccessProfile profile) {
        finish();
        return page.getContents(0);
    }

    @Override public boolean doYouLikeIt(Tag tag) {
        return "div".equals(tag.name) && "quote".equals(tag.props.get("class"));
    }

}
