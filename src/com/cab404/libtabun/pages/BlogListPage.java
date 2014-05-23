package com.cab404.libtabun.pages;

import com.cab404.libtabun.data.Blog;
import com.cab404.libtabun.modules.BlogLabelModule;
import com.cab404.moonlight.framework.ModularBlockParser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cab404
 */
public class BlogListPage extends TabunPage {
    public int page = 1;
    public List<Blog> blogs;

    public BlogListPage() {
        blogs = new ArrayList<>();
    }

    @Override public String getURL() {
        return "/blogs/page" + page;
    }

    @Override protected void bindParsers(ModularBlockParser base) {
        super.bindParsers(base);
        base.bind(new BlogLabelModule(), BLOCK_BLOG_INFO);
    }

    @Override public void handle(Object object, int key) {
        switch (key) {
            case BLOCK_BLOG_INFO:
                blogs.add((Blog) object);
                break;
            default:
                super.handle(object, key);
        }
    }

}
