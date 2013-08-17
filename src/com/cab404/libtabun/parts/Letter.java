package com.cab404.libtabun.parts;

import com.cab404.libtabun.facility.ResponseFactory;

/**
 * Переписка.
 *
 * @author cab404
 */
class Letter extends Part {
    public Letter() {

    }

    public static class LetterList {

        public class LetterLabel {
            String name;
            int id;


        }

        public class LetterListParser implements ResponseFactory.Parser {

            @Override
            public boolean line(String line) {
                return false;
            }
        }
    }

}
