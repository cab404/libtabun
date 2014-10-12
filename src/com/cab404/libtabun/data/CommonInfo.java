package com.cab404.libtabun.data;

import com.cab404.libtabun.util.JSONable;

/**
 * @author cab404
 */
public class CommonInfo extends JSONable {
	@JSONField
	public String username, avatar;
	@JSONField
	public float rating, strength;
	@JSONField
	public int new_messages;
}
