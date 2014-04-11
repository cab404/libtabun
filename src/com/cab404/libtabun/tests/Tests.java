package com.cab404.libtabun.tests;

import com.cab404.libtabun.util.SU;
import com.cab404.libtabun.util.U;
import com.cab404.libtabun.util.modular.AccessProfile;

import java.util.ArrayList;

/**
 * @author cab404
 */
public class Tests {

    public static void main(String[] args) {
        U.v(SU.removeRedundantSpaces("   test    sa  s      bdr"));

        TestLauncher launcher = new TestLauncher(new AccessProfile("tabun.everypony.ru"));

        ArrayList<Class<? extends Test>> tests = new ArrayList<>();

        tests.add(UserInfoTest.class);

        launcher.launch(tests);
    }

}
