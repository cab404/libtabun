package com.cab404.libtabun.tests;

import com.cab404.libtabun.pages.TopicPage;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.tests.Test;

/**
 * @author cab404
 */
public class TopicTest extends Test {

    @Override public void test(AccessProfile profile) {
        TopicPage topicPage = new TopicPage(92027);
        topicPage.fetch(profile);

        assertEquals(topicPage.comments.size(), topicPage.header.comments);
    }

}
