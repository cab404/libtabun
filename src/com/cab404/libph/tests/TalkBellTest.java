package com.cab404.libph.tests;

import com.cab404.libph.pages.TabunPage;
import com.cab404.libph.requests.TalkBellRequest;
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
