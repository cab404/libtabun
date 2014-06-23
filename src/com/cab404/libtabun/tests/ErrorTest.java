package com.cab404.libtabun.tests;

import com.cab404.libtabun.pages.TopicPage;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.util.tests.Test;

/**
 * @author cab404
 */
public class ErrorTest extends Test {


    boolean error_hit = false;
    @Override public void test(AccessProfile profile) {

        new TopicPage(-1000) {
            @Override public void handle(Object object, int key) {
                super.handle(object, key);
                if (key == BLOCK_ERROR)
                    error_hit = true;
            }
        }.fetch(profile);

        assertEquals("Error hit", true, error_hit);

    }

}
