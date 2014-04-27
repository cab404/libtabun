package com.cab404.libtabun.tests;

import com.cab404.libtabun.pages.TopicPage;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.util.tests.Test;

/**
 * @author cab404
 */
public class TopicTest extends Test {

    @Override public void test(AccessProfile profile) {
        TopicPage topicPage;

        topicPage = new TopicPage(85919);
        topicPage.fetch(profile);

        assertLessOrEquals("Все комментарии загружены", topicPage.header.comments, topicPage.comments.size());
        assertEquals("Имя блога", topicPage.header.blog.name, "Блог им. cab404");
        assertEquals("URL-имя блога", topicPage.header.blog.url_name, null);
        assertEquals("Название поста", topicPage.header.title, "...");
        assertEquals("Рейтинг поста", topicPage.header.votes, "+65");

        topicPage = new TopicPage(91428);
        topicPage.fetch(profile);

        assertLessOrEquals("Все комментарии загружены", topicPage.header.comments, topicPage.comments.size());
        assertEquals("Имя блога", topicPage.header.blog.name, "/dev/tabun");
        assertEquals("URL-имя блога", topicPage.header.blog.url_name, "technical");
    }

}
