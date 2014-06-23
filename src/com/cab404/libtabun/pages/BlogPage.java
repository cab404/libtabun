package com.cab404.libtabun.pages;

import com.cab404.libtabun.data.Blog;
import com.cab404.libtabun.data.Topic;
import com.cab404.libtabun.modules.BlogModule;
import com.cab404.libtabun.modules.TopicModule;
import com.cab404.moonlight.framework.ModularBlockParser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cab404
 */
public class BlogPage extends MainPage {

    public Blog blog;
    public List<Topic> topics;
    public int page = 1;

    public BlogPage(Blog blog) {
        this.blog = blog;
        topics = new ArrayList<>();
    }

    public BlogPage(Blog blog, int page) {
        this(blog);
        this.page = page;
    }

    @Override public String getURL() {
        return "/blog/" + blog.url_name + "/page" + page;
    }

    @Override protected void bindParsers(ModularBlockParser base) {
        super.bindParsers(base);
        base.bind(new TopicModule(TopicModule.Mode.LIST), BLOCK_TOPIC_HEADER);
        base.bind(new BlogModule(), BLOCK_BLOG_INFO);
    }

    @Override public void handle(Object object, int key) {
        switch (key) {
            case BLOCK_BLOG_INFO:
                blog = (Blog) object;
                break;
            case BLOCK_TOPIC_HEADER:
                topics.add((Topic) object);
                break;
            default:
                super.handle(object, key);
        }
    }

}
