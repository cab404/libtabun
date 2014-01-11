package com.cab404.libtabun.facility;

import com.cab404.libtabun.U;

import java.util.ArrayList;
import java.util.List;

/**
 * +20% cooler; +4k% faster;
 *
 * @author cab404
 */
public class HTMLParser2 {
    public static final boolean logging = false;

    @SuppressWarnings("ConstantConditions")
    public static void v(Object obj) {
        if (logging)
            U.v(obj);
    }

    /**
     * Splits string without reqexes
     */
    public static List<String> split(String source, String not_a_regex) {
        ArrayList<String> out = new ArrayList<>();
        int i = 0;
        while (true) {
            int j = source.indexOf(not_a_regex, i);

            if (j == -1) {
                out.add(source.substring(i));
                break;
            }
            out.add(source.substring(i, j));
            i = j + not_a_regex.length();
        }
        return out;
    }

    /**
     * Splits string without reqexes
     */
    public static List<String> split(String source, String not_a_regex, int limit) {
        ArrayList<String> out = new ArrayList<>();
        int i = 0;
        while (true) {
            int j = source.indexOf(not_a_regex, i);

            if (j == -1 || out.size() + 1 == limit) {
                out.add(source.substring(i));
                break;
            }
            out.add(source.substring(i, j));
            i = j + not_a_regex.length();
        }
        return out;
    }

    /**
     * Returns true if check contains symbol ch
     */
    public static boolean contains(CharSequence check, char ch) {
        for (int i = 0; i < check.length(); i++) {
            if (check.charAt(i) == ch) return true;
        }
        return false;
    }

    /**
     * Splits string using any of chars from "chars"
     * For instance, call<br/>
     * charSplit("test test, test.test", " .,") <br/>
     * will return you [test, test, test, test]
     */
    public static List<String> charSplit(String source, CharSequence chars) {
        ArrayList<String> out = new ArrayList<>();
        int last = 0;

        for (int i = 0; i < source.length(); i++) {
            if (contains(chars, source.charAt(i))) {
                out.add(source.substring(last, i));
                last = i + 1;
            }
        }

        out.add(source.substring(last));
        return out;
    }

    private static final String
            COMM_START = "!--",
            COMM_END = "-->";

    public static ArrayList<HTMLParser.Tag> parse(String toParse) {
        v(" == Started parsing, HTMLParser2 == ");
        ArrayList<HTMLParser.Tag> out = new ArrayList<>();
        int i, j = 0;
        while (true) {

            i = toParse.indexOf("<", j);
            j = toParse.indexOf(">", i);
            if (i == -1 || j == -1) break;


            HTMLParser.Tag tag = new HTMLParser.Tag();
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
                v(tag);
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


            List<String> name_and_everything_else = split(inner, " ", 2);
            tag.name = name_and_everything_else.get(0);
            if (tag.name.startsWith("!")) tag.isStandalone = true;

            v(tag);
            if (name_and_everything_else.size() == 2) {
                List<String> props = charSplit(name_and_everything_else.get(1), "\"");

                for (int ind = 0; ind + 1 < props.size(); ind += 2) {

                    String key = props.get(ind).trim();
                    key = key.substring(0, key.length() - 1);

                    String value = props.get(ind + 1);

                    v(key + ": " + value);

                    tag.props.put(key, value);
                }
            }
            v("\n");


            out.add(tag);

        }
        v(String.format(" == Finished parsing, HTMLParser2, found %d tags == ", out.size()));
        return out;
    }

}
