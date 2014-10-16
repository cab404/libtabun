package com.cab404.libph.data;

import com.cab404.libph.util.JSONable;

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
