package com.cab404.libtabun.tests;

import com.cab404.libtabun.data.Blog;
import com.cab404.libtabun.pages.BlogPage;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.tests.Test;

/**
 * @author cab404
 */
public class BlogTest extends Test {

    @Override public void test(AccessProfile profile) {
        BlogPage blogPage = new BlogPage(new Blog("test_blog"));
        blogPage.fetch(profile);
    }

}
