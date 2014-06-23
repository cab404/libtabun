package com.cab404.libtabun.tests;

import com.cab404.libtabun.util.TabunAccessProfile;
import com.cab404.moonlight.util.tests.Test;
import com.cab404.moonlight.util.tests.TestLauncher;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * @author cab404
 */
public class Tests {

    public static void main(String[] args)
    throws FileNotFoundException {
        TestLauncher launcher = new TestLauncher(new TabunAccessProfile());

        ArrayList<Class<? extends Test>> tests = new ArrayList<>();

        if (args.length == 0) {
            tests.add(LoginTest.class);
            tests.add(LetterTest.class);
            tests.add(TalkBellTest.class);
        }
        tests.add(ProfileTest.class);
        tests.add(TopicTest.class);
        tests.add(ErrorTest.class);
        tests.add(BlogTest.class);
        tests.add(StreamTest.class);
        tests.add(MainPageTest.class);
        tests.add(UserAutocompleteTest.class);
        tests.add(BlogListTest.class);

        launcher.launch(tests);

    }

}
