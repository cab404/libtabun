package com.cab404.libtabun.data;

import com.cab404.libtabun.util.JSONable;

/**
 * @author cab404
 */
public class StreamItem extends JSONable {
	public static enum Type {
		VOTE_COMMENT,
		ADD_COMMENT,
		VOTE_TOPIC,
		ADD_TOPIC,
		VOTE_USER,
		ADD_BLOG,
		JOIN_BLOG,
		VOTE_BLOG,
	}

	@JSONField
	public Type type;
	@JSONField
	public Profile user;

	@JSONField
	public String date;

	@JSONField
	public String link, data;

}
