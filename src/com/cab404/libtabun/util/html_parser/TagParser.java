package com.cab404.libtabun.util.html_parser;

import com.cab404.libtabun.util.SU;

import java.util.ArrayList;
import java.util.List;

/**
 * +20% cooler; +4k% faster;
 *
 * @author cab404
 */
public class TagParser {
//    public static final boolean logging = false;
//
//    @SuppressWarnings("ConstantConditions")
//    public static void v(Object obj) {
//        if (logging)
//            U.v(obj);
//    }

    private static final String
            COMM_START = "!--",
            COMM_END = "-->";

    public static ArrayList<Tag> parse(String toParse) {
//        v(" == Started parsing, HTMLParser2 == ");
        ArrayList<Tag> out = new ArrayList<>();
        int i, j = 0;
        while (true) {

//            v(toParse);
            i = toParse.indexOf('<', j);
            j = toParse.indexOf('>', i);

            if (i == -1 || j == -1) break;


            Tag tag = new Tag();
            tag.start = i;
            tag.end = j + 1;
            tag.text = toParse.substring(i, j + 1);


            String inner = toParse.substring(i + 1, j);
            int l = inner.length() - 1;


            if (inner.startsWith(COMM_START)) {
                tag.isComment = true;
                tag.isStandalone = true;
                tag.name = COMM_START;
                j = toParse.indexOf(COMM_END, i) + 3;
                tag.text = toParse.substring(i, j);
//                v(tag);
                out.add(tag);
                continue;
            }


            if (inner.charAt(0) == '/') {
                tag.isClosing = true;
                inner = toParse.substring(i + 2, j);
            } else if (inner.charAt(l) == '/') {
                tag.isStandalone = true;
                inner = toParse.substring(i + 1, j - 1);
            }


            List<String> name_and_everything_else = SU.charSplit(inner, 2, ' ');
            tag.name = name_and_everything_else.get(0);

            if (tag.name.charAt(0) == '!')
                tag.isStandalone = true; // Handling !doctype.

//            v(tag);
            if (name_and_everything_else.size() == 2) { // Parsing properties.
                List<String> props = SU.charSplit(name_and_everything_else.get(1), '\"', '\'');

                for (int ind = 0; ind + 1 < props.size(); ind += 2) {

                    String key = props.get(ind).trim();
                    key = key.substring(0, key.length() - 1);

                    String value = props.get(ind + 1);

//                    v(key + ": " + value);

                    tag.props.put(key, value);
                }
            }
//            v("\n");


            out.add(tag);

        }

//        v(String.format(" == Finished parsing, HTMLParser2, found %d tags == ", out.size()));
        return out;
    }

}
