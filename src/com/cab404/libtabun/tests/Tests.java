package com.cab404.libtabun.tests;

import com.cab404.libtabun.tests.base.Test;
import com.cab404.libtabun.tests.base.TestLauncher;
import com.cab404.libtabun.util.modular.AccessProfile;

import java.util.ArrayList;

/**
 * @author cab404
 */
public class Tests {

    public static void main(String[] args) {
        TestLauncher launcher = new TestLauncher(new AccessProfile("tabun.everypony.ru"));

        ArrayList<Class<? extends Test>> tests = new ArrayList<>();

        tests.add(UserInfoTest.class);
        tests.add(TopicTest.class);

        launcher.launch(tests);
    }

}
