package com.cab404.libtabun.modules;

import com.cab404.libtabun.data.Blog;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.ModuleImpl;
import com.cab404.moonlight.parser.HTMLTree;
import com.cab404.moonlight.parser.Tag;

/**
 * @author cab404
 */
public class BlogModule extends ModuleImpl<Blog> {

    @Override public Blog extractData(HTMLTree page, AccessProfile profile) {
        Blog blog = new Blog();
        blog.about = page.xPathStr("div/div/div&class=blog-description").trim();
        return blog;
    }

    @Override public boolean doYouLikeIt(Tag tag) {
        return "div".equals(tag.name) && "blog".equals(tag.get("id"));
    }

}
