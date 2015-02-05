package com.cab404.libtabun.tests;

import com.cab404.libtabun.modules.CommentModule;
import com.cab404.libtabun.pages.TabunPage;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.ModularBlockParser;
import com.cab404.moonlight.util.tests.Test;

/**
 * @author cab404
 */
public class CommentListTest extends Test {

    @Override
    public void test(AccessProfile profile) {

        TabunPage page = new TabunPage() {
            @Override
            protected void bindParsers(ModularBlockParser base) {
                super.bindParsers(base);
                base.bind(new CommentModule(CommentModule.Mode.LIST), BLOCK_COMMENT);
            }

            @Override
            public String getURL() {
                return "/profile/cab404/created/comments";
            }
        };
        page.fetch(profile);


    }

}
