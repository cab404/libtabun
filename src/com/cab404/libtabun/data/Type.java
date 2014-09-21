package com.cab404.libtabun.data;

/**
 * @author cab404
 */
public enum Type {
	TOPIC("Topic"),
	BLOG("Blog"),
	USER("User"),
	TALK("Talk"),
	COMMENT("Comment");

	public final String name;
	Type(String name) {
		this.name = name;
	}
}
