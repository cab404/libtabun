package com.cab404.libtabun.tests;

import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.tests.Test;
import com.cab404.moonlight.tests.TestLauncher;

import java.util.ArrayList;

/**
 * @author cab404
 */
public class Tests {

    public static void main(String[] args) {
        TestLauncher launcher = new TestLauncher(new AccessProfile("tabun.everypony.ru"));

        ArrayList<Class<? extends Test>> tests = new ArrayList<>();

        tests.add(LoginTest.class);
        tests.add(ProfileTest.class);
        tests.add(TopicTest.class);
        tests.add(BlogTest.class);
        tests.add(StreamTest.class);

        launcher.launch(tests);
    }

}
