package com.cab404.libtabun.tests;

import com.cab404.libtabun.pages.StreamPage;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.tests.Test;

/**
 * @author cab404
 */
public class StreamTest extends Test {

    @Override public void test(AccessProfile profile) {
        StreamPage page = new StreamPage();
        page.fetch(profile);
//        U.Timer timer = new U.Timer();
//        StreamItem item = new StreamItem();
//
//        while (true) {
//            timer.set();
//            StreamPage streamPage = new StreamPage();
//            streamPage.fetch(profile);
//
//            for (StreamItem str : streamPage.stream)
//                if (!str.link.equals(item.link))
//                    U.v(SU.table(15, str.type + "", 20, str.user.login + "", 80, str.link + "", str.data + ""));
//                else
//                    break;
//            item = streamPage.stream.get(0);
//
//
//            timer.log(":time:");
//
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }

    }

}
