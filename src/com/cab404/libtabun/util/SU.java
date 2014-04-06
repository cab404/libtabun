package com.cab404.libtabun.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * String utils.
 *
 * @author cab404
 */
public class SU {
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
    public static boolean contains(char[] check, char ch) {
        for (char curr : check)
            if (curr == ch) return true;
        return false;
    }

    /**
     * Splits string using any of chars from "chars"
     * For instance, call<br/>
     * charSplit("test test, test.test", " .,") <br/>
     * will return you [test, test, test, test]
     */
    public static List<String> charSplit(String source, char... chars) {
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

    /**
     * Splits string using any of chars from "chars"
     * For instance, call<br/>
     * charSplit("test test, test.test", 2, " .,") <br/>
     * will return you ["test", "test, test.test"]
     */
    public static List<String> charSplit(String source, int limit, char... chars) {
        ArrayList<String> out = new ArrayList<>();
        int last = 0;

        for (int i = 0; i < source.length(); i++) {
            if (contains(chars, source.charAt(i))) {
                out.add(source.substring(last, i));
                last = i + 1;

                if (out.size() + 1 == limit)
                    break;

            }
        }

        out.add(source.substring(last));
        return out;
    }

    /**
     * String.substring, только с выбором начала и конца строки в виде строк
     */
    public static String sub(String source, String start, String end) {

        int sIndex = source.indexOf(start);
        if (sIndex == -1) {
            U.w("Error while parsing string " + source + ", no start position found.");
            return null;
        }
        sIndex += start.length();

        int eIndex = source.indexOf(end, sIndex);
        if (eIndex == -1) {
            U.w("Error while parsing string " + source + ", no end position found.");
            return null;
        }
        return source.substring(sIndex, eIndex);
    }
    /**
     * Backwards sub, делает то же, что и sub, только с конца строки.
     */
    public static String bsub(String source, String end, String start) {
        int sIndex = source.lastIndexOf(start);
        if (sIndex == -1) {
            U.w("Error while parsing string " + source + ", no start position found.");
            return null;
        }

        int eIndex = source.lastIndexOf(end, sIndex);
        if (eIndex == -1) {
            U.w("Error while parsing string " + source + ", no end position found.");
            return null;
        }
        return source.substring(eIndex + end.length(), sIndex);
    }
    /**
     * Всего лишь сокращение URLEncoder.encode()
     */
    public static String rl(String toConvert) {
        try {
            return URLEncoder.encode(toConvert, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            U.w(e);
            return null;
        }
    }
    /**
     * Всего лишь сокращение URLDecoder.decode()
     */
    public static String drl(String toConvert) {
        try {
            return URLDecoder.decode(toConvert, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            U.w(e);
            return null;
        }
    }
    public static String join(String[] strings, String delimeter) {
        String out = "";
        for (int i = 0; i < strings.length - 1; i++) out += strings[i] + delimeter;
        out += strings[strings.length - 1];
        return out;
    }
    /**
     * Фух. Эта штука меняет все HTML 4.0 и 2.0 entity на нормальный текст.
     */
    public static String deEntity(String in) {
        return in
                .replaceAll("&quot;", "\"").replaceAll("&rlm;", " ‏")
                .replaceAll("&amp;", "&").replaceAll("&ndash;", "–")
                .replaceAll("&lt;", "<").replaceAll("&mdash;", "—")
                .replaceAll("&gt;", ">").replaceAll("&lsquo;", "‘")
                .replaceAll("&OElig;", "Œ").replaceAll("&rsquo;", "’")
                .replaceAll("&oelig;", "œ").replaceAll("&sbquo;", "‚")
                .replaceAll("&Scaron;", "Š").replaceAll("&ldquo;", "“")
                .replaceAll("&scaron;", "š").replaceAll("&rdquo;", "”")
                .replaceAll("&Yuml;", "Ÿ").replaceAll("&bdquo;", "„")
                .replaceAll("&circ;", "ˆ").replaceAll("&dagger;", "†")
                .replaceAll("&tilde;", "˜").replaceAll("&Dagger;", "‡")
                .replaceAll("&ensp;", " ").replaceAll("&permil;", "‰")
                .replaceAll("&emsp;", " ").replaceAll("&lsaquo;", "‹")
                .replaceAll("&thinsp;", " ").replaceAll("&rsaquo;", "›")
                .replaceAll("&zwnj;", " ").replaceAll("&euro;", "€")
                .replaceAll("&zwj;", " ").replaceAll("&lrm;", " ")
                .replaceAll("&#039;", "'")
                ;
    }
    /**
     * Фух. Эта штука меняет все HTML 4.0 и 2.0 entity на нормальный текст.
     */
    public static String reEntity(String in) {
        return in
                .replaceAll("&quot;", "\"").replaceAll("&rlm;", " ‏")
                .replaceAll("&amp;", "&").replaceAll("&ndash;", "–")
                .replaceAll("&lt;", "<").replaceAll("&mdash;", "—")
                .replaceAll("&gt;", ">").replaceAll("&lsquo;", "‘")
                .replaceAll("&OElig;", "Œ").replaceAll("&rsquo;", "’")
                .replaceAll("&oelig;", "œ").replaceAll("&sbquo;", "‚")
                .replaceAll("&Scaron;", "Š").replaceAll("&ldquo;", "“")
                .replaceAll("&scaron;", "š").replaceAll("&rdquo;", "”")
                .replaceAll("&Yuml;", "Ÿ").replaceAll("&bdquo;", "„")
                .replaceAll("&circ;", "ˆ").replaceAll("&dagger;", "†")
                .replaceAll("&tilde;", "˜").replaceAll("&Dagger;", "‡")
                .replaceAll("&ensp;", " ").replaceAll("&permil;", "‰")
                .replaceAll("&emsp;", " ").replaceAll("&lsaquo;", "‹")
                .replaceAll("&thinsp;", " ").replaceAll("&rsaquo;", "›")
                .replaceAll("&zwnj;", " ").replaceAll("&euro;", "€")
                .replaceAll("&zwj;", " ").replaceAll("&lrm;", " ")
                .replaceAll("&#039;", "'")
                ;
    }

    /**
     * Works with patterns like asd*g, also is pretty fast.
     */
    public static boolean fast_match(String regex, String data) {
        List<String> strings = charSplit(regex, '*');

        if (strings.size() == 1)
            return data.equals(regex);

        if (!(data.startsWith(strings.get(0)) && data.endsWith(strings.get(strings.size() - 1))))
            return false;

        int s, f;

        for (String str : strings) {
            s = data.indexOf(str, 0);
            f = s + str.length();

            if (s == -1)
                return false;

            data = data.substring(0, s) + data.substring(f);
        }

        return true;
    }

    public static String removeAllTags(String toProcess) {
        int s;
        while ((s = toProcess.indexOf('<')) != -1) {
            int f = toProcess.indexOf('>', s);
            if (f == -1) break;
            toProcess = toProcess.substring(0, s) + toProcess.substring(f + 1);
        }
        return toProcess;
    }

    public static String tabs(int num) {
        StringBuilder tabs = new StringBuilder();
        for (int i = 0; i < num; i++) tabs.append("\t");
        return tabs.toString();
    }


    public static enum FillType {
        RIGHT, LEFT, CENTER
    }
    public static String fillSpaces(String fill, int num, int offset, FillType type) {
        int left = 0, right = 0;
        num -= offset * 2;

        switch (type) {
            case RIGHT:
                right = offset;
                left = offset + num - fill.length();
                break;
            case LEFT:
                left = offset;
                right = offset + num - fill.length();
                break;
            case CENTER:
                right = (int) Math.floor((float) num / 2) + offset;
                left = (int) Math.ceil((float) num / 2) + offset;
                break;
        }

        return spaces(left) + fill + spaces(right);

    }

    public static String spaces(int num) {
        StringBuilder spaces = new StringBuilder();
        for (int i = 0; i < num; i++)
            spaces.append(" ");
        return spaces.toString();
    }
}
