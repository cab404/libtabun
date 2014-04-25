package com.cab404.libtabun.util;

import com.cab404.moonlight.util.SU;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * @author cab404
 */
public class Tabun {


    public static final List<String> months =
            Collections.unmodifiableList(Arrays.asList(
                    "декабря",
                    "января",
                    "февраля",
                    "марта",
                    "апреля",
                    "мая",
                    "июня",
                    "июля",
                    "августа",
                    "сентября",
                    "октября",
                    "ноября"
            ));


    @SuppressWarnings("MagicConstant")
    public static Calendar parseDate(String str) {
        List<String> split = SU.charSplit(str, ' ');
        boolean is_long = split.get(2).endsWith(",");

        Calendar calendar = Calendar.getInstance();
        calendar.clear();

        int day = Integer.parseInt(split.get(0));
        int month = months.indexOf(split.get(1));
        int year = Integer.parseInt(split.get(2).substring(0, 4));
        int hour = 0;
        int minute = 0;

        if (is_long) {
            List<String> time = SU.charSplit(split.get(3), ':');
            hour = Integer.parseInt(time.get(0));
            minute = Integer.parseInt(time.get(1));
        }
        calendar.set(year, month, day, hour, minute);

        return calendar;
    }

    // 2014-02-12T21:20:13+04:00
    @SuppressWarnings("MagicConstant")
    public static Calendar parseSQLDate(String date) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        List<String> split = SU.split(date, "T");
        List<String> in_year = SU.split(split.get(0), "-");
        List<String> in_day = SU.split(SU.split(split.get(1), "+").get(0), ":");

        calendar.set(
                Integer.parseInt(in_year.get(0)),
                Integer.parseInt(in_year.get(1)) - 1,
                Integer.parseInt(in_year.get(2)),
                Integer.parseInt(in_day.get(0)),
                Integer.parseInt(in_day.get(1)),
                Integer.parseInt(in_day.get(2))
        );

        return calendar;
    }

}
