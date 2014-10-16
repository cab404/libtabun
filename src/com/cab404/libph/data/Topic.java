package com.cab404.libph.data;

import com.cab404.libph.util.JSONable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * @author cab404
 */
public class Topic extends JSONable {
	private static final long serialVersionUID = 0L;

	@JSONField
	public String title, votes;
	@JSONField
	public Profile author;
	@JSONField
	public Blog blog;

	@JSONField
	public String text;
	@JSONField
	public List<String> tags;

	@JSONField
	public Calendar date;
	@JSONField
	public int id;

	@JSONField
	public boolean vote_enabled = false;
	@JSONField
	public boolean in_favourites = false;

	@JSONField
	public int your_vote = 0;

	@JSONField
	public int comments = 0, comments_new = 0;

	public Topic() {
		tags = new ArrayList<>();
		blog = new Blog();
		author = new Profile();
	}
}
