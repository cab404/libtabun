package com.cab404.libtabun.tests;

import com.cab404.libtabun.data.TabunError;
import com.cab404.libtabun.pages.TopicPage;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.util.tests.Test;

/**
 * @author cab404
 */
public class ErrorTest extends Test {


    TabunError error_hit;

    @Override
    public void test(AccessProfile profile) {

        new TopicPage(-1000) {
            @Override
            public void handle(Object object, int key) {
                super.handle(object, key);
                if (key == BLOCK_ERROR)
                    error_hit = (TabunError) object;
            }
        }.fetch(profile);

        assertEquals("Страница не найдена (Пост id:-1000)", TabunError.NOT_FOUND, error_hit);

        new TopicPage(104992) {
            @Override
            public void handle(Object object, int key) {
                super.handle(object, key);
                if (key == BLOCK_ERROR)
                    error_hit = (TabunError) object;
            }
        }.fetch(profile);

        assertEquals("Няшная антрота из 'На грани'", TabunError.ACCESS_DENIED, error_hit);

    }

}
