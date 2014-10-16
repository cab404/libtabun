package com.cab404.libph.tests;

import com.cab404.libph.pages.StreamPage;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.util.tests.Test;

/**
 * @author cab404
 */
public class StreamTest extends Test {

    @Override public void test(AccessProfile profile) {
        StreamPage page = new StreamPage();
        page.fetch(profile);
        assertLess("Что-то есть в потоке", 0, page.stream.size());

    }

}
