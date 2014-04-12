package com.cab404.libtabun.tests;

import com.cab404.libtabun.pages.TopicPage;
import com.cab404.moonlight.tests.Test;
import com.cab404.moonlight.framework.AccessProfile;

/**
 * @author cab404
 */
public class TopicTest extends Test {

    @Override public void test(AccessProfile profile) {
        TopicPage topicPage = new TopicPage(83183);
        topicPage.fetch(profile);
    }

}
