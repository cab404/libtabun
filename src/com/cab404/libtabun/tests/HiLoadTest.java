package com.cab404.libtabun.tests;

import com.cab404.libtabun.data.Blog;
import com.cab404.libtabun.data.Topic;
import com.cab404.libtabun.pages.BlogPage;
import com.cab404.libtabun.pages.TopicPage;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.util.logging.Log;
import com.cab404.moonlight.util.tests.Test;

/**
 * @author cab404
 */
public class HiLoadTest extends Test {

    @Override public void test(AccessProfile profile) {
        Blog blog = new Blog("ponyhawks");

        for (int i = 1; ; i++) {
            Log.v("Страница " + i);
            BlogPage page = new BlogPage(blog, i);
            page.fetch(profile);
            if (page.topics.isEmpty()) break;

            for (Topic label : page.topics) {

                Log.v("Топик " + label.id);
                TopicPage topic = new TopicPage(label.id);
                topic.fetch(profile);

            }

        }

    }

}
