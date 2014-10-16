package com.cab404.libph.data;

import com.cab404.libph.util.JSONable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author cab404
 */
public class Letter extends JSONable {
	@JSONField
	public String title;
	@JSONField
	public String text;
	@JSONField
	public List<String> recipients;
	@JSONField
	public Profile starter;
	@JSONField
	public Calendar date;
	@JSONField
	public int id;

	@JSONField
	public int comments = 0, comments_new = 0;

	public Letter() {
		recipients = new ArrayList<>();
		starter = new Profile();
	}
}
