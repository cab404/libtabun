package com.cab404.libtabun.modules;

import com.cab404.moonlight.util.html_parser.HTMLTree;
import com.cab404.moonlight.util.html_parser.Tag;
import com.cab404.moonlight.util.modular.AccessProfile;

/**
 * @author cab404
 */
public class QuoteModule extends ModuleImpl<String> {

    @Override public String extractData(HTMLTree page, AccessProfile profile) {
        finish();
        return page.xPathStr("html/body/" +
                "div&id=container/" +
                "div&id=wrapper/" +
                "aside&id=sidebar/" +
                "section&class=block block-type-tags/" +
                "div&class=quote");
    }

    @Override public boolean doYouLikeIt(Tag tag) {
        return "div".equals(tag.name) && "quote".equals(tag.props.get("class"));
    }

}
