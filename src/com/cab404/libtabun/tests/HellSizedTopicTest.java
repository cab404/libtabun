package com.cab404.libtabun.tests;

import com.cab404.libtabun.pages.TopicPage;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.util.tests.Test;

/**
 * Загружаем срачик Ликсиса™ :3
 *
 * @author cab404
 */
public class HellSizedTopicTest extends Test {
    @Override
    public void test(AccessProfile profile) {

        TopicPage topicPage;

        topicPage = new TopicPage(74547);
        topicPage.fetch(profile);
    }
}
