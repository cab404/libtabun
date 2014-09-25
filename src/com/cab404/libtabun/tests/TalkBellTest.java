package com.cab404.libtabun.tests;

import com.cab404.libtabun.pages.TabunPage;
import com.cab404.libtabun.requests.TalkBellRequest;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.util.tests.Test;

/**
 * @author cab404
 */
public class TalkBellTest extends Test {

    @Override public void test(AccessProfile profile) {
        TabunPage page = new TabunPage();
        page.fetch(profile);

        TalkBellRequest request = new TalkBellRequest();
        request.exec(profile);

    }

}
