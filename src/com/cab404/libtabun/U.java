package com.cab404.libtabun;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpRequestBase;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * U is for the Utils
 * Куча статичных методов и переменных (по идее).
 *
 * @author cab404
 */
public class U {
    public static final String tabun = "tabun.everypony.ru";

    /**
     * Делает абсолютно то же, что и Log.v("Luna Log", obj.toString()),
     * плюс проверяет на null.
     */
    public static void v(Object obj) {
        try {
            System.out.println(obj == null ? null : obj.toString());
            System.out.flush();
        } catch (NullPointerException e) {
            w(e);
        }
    }

    /**
     * Делает абсолютно то же, что и Log.w("Luna Log", obj.toString()),
     * плюс проверяет на null.
     */
    public static void w(Object obj) {
        System.err.println(obj == null ? null : obj.toString());
        System.err.flush();
    }

    /**
     * Делает абсолютно то же, что и Log.w("Luna Log", obj.toString()),
     * плюс проверяет на null.
     */
    public static void w(Throwable obj) {
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer);
        if (obj != null) obj.printStackTrace(out);
        System.err.println(writer.toString());
        System.err.flush();
    }

    public static void v(HttpRequestBase request) {
        v(request.getRequestLine());
        for (Header header : request.getAllHeaders())
            v(header.getName() + ": " + header.getValue());
        v("");
    }

    /**
     * Делает абсолютно то же, что и Log.wtf("Luna Log", obj.toString()),
     * плюс проверяет на null.
     */
    public static void wtf(Object obj) {
        System.err.println("WTF? " + (obj == null ? null : obj.toString()));
        System.err.flush();
    }

    /**
     * Достаёт рандомный элемент из массива. И всё :D
     */
    public static <T> T getRandomEntry(T[] values) {
        return values[(int) Math.floor(Math.random() * values.length)];
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
            w(e);
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
            w(e);
            return null;
        }
    }

    public static String join(String[] strings, String delimeter) {
        String out = "";
        for (String string : strings) out += string + delimeter;
        return out;
    }

    public static Calendar convertDatetime(String datetime) {
        String timezone = datetime.substring(18);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(timezone));

        int year = Integer.parseInt(datetime.substring(0, 4));
        int month = Integer.parseInt(datetime.substring(5, 7)) - 1;
        int day = Integer.parseInt(datetime.substring(8, 10));

        int hour = Integer.parseInt(datetime.substring(11, 13));
        int minute = Integer.parseInt(datetime.substring(14, 16));
        int second = Integer.parseInt(datetime.substring(17, 19));

        calendar.set(year, month, day, hour, minute, second);
        return calendar;
    }

    public static String removeAllTags(String toProcess) {
        return toProcess.replaceAll("<.*?>", "");
    }


}
