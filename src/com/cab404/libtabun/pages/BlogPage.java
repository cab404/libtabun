package com.cab404.libtabun.pages;

import com.cab404.libtabun.data.Blog;
import com.cab404.libtabun.modules.BlogModule;
import com.cab404.libtabun.modules.TopicModule;
import com.cab404.moonlight.framework.ModularBlockParser;

/**
 * @author cab404
 */
public class BlogPage extends MainPage {

    Blog blog;

    public BlogPage(Blog blog) {
        this.blog = blog;
    }

    @Override public String getURL() {
        return "/blog/" + blog.url_name;
    }

    @Override protected void bindParsers(ModularBlockParser base) {
        super.bindParsers(base);
        base.bind(new TopicModule(TopicModule.Mode.LIST), BLOCK_TOPIC_HEADER);
        base.bind(new BlogModule(), BLOCK_BLOG_INFO);

    }

    @Override public void handle(Object object, int key) {
        super.handle(object, key);
        switch (key) {

        }
    }

}
