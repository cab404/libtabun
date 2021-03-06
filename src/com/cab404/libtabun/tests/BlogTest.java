package com.cab404.libtabun.tests;

import com.cab404.libtabun.data.Blog;
import com.cab404.libtabun.pages.BlogPage;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.util.tests.Test;

/**
 * @author cab404
 */
public class BlogTest extends Test {

    @Override
    public void test(AccessProfile profile) {
        BlogPage blogPage = new BlogPage(new Blog("test_blog"));
        blogPage.fetch(profile);

        assertEquals("Название", "Test Blog", blogPage.blog.name);
        assertEquals("Описание", "Made with love and ponies for testing.", blogPage.blog.about);
        assertEquals("URL", "test_blog", blogPage.blog.url_name);
        assertEquals("Закрытость", true, blogPage.blog.restricted);
        assertEquals("ID", 17856, blogPage.blog.id);

    }

}
