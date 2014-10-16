package com.cab404.libph.util;

import com.cab404.moonlight.util.SU;

import java.util.*;

import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

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


	private static String copyChars(char ch, int num) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < num; i++) builder.append(ch);
		return builder.toString();
	}

	private static String fillZeroes(int fill, int len) {
		String data = String.valueOf(fill);
		return copyChars('0', len - data.length()) + data;
	}

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
		List<String> in_day = SU.split(split.get(1).substring(0, 8), ":");
		String in_timezone = split.get(1).substring(8);

		calendar.set(
				Integer.parseInt(in_year.get(0)),
				Integer.parseInt(in_year.get(1)) - 1,
				Integer.parseInt(in_year.get(2)),
				Integer.parseInt(in_day.get(0)),
				Integer.parseInt(in_day.get(1)),
				Integer.parseInt(in_day.get(2))
		);
		calendar.setTimeZone(TimeZone.getTimeZone("GMT" + in_timezone));
		return calendar;
	}


	public static String toSQLDate(Calendar calendar) {
		int rawOffset = calendar.getTimeZone().getRawOffset();
		return "" +
				calendar.get(Calendar.YEAR) + "-" +
				fillZeroes((calendar.get(Calendar.MONTH) + 1), 2) + "-" +
				fillZeroes(calendar.get(Calendar.DAY_OF_MONTH), 2) + "T" +
				fillZeroes(calendar.get(Calendar.HOUR_OF_DAY), 2) + ":" +
				fillZeroes(calendar.get(Calendar.MINUTE), 2) + ":" +
				fillZeroes(calendar.get(Calendar.SECOND), 2) +
				(rawOffset > 0 ? "+" : "-") +
				fillZeroes((int) MILLISECONDS.toHours(rawOffset), 2) + ":" +
				fillZeroes((int) MILLISECONDS.toMinutes(rawOffset % HOURS.toMillis(1)), 2);

	}
}
