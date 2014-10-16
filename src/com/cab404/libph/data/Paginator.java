package com.cab404.libph.data;

import com.cab404.libph.util.JSONable;

/**
 * @author cab404
 */
public class Paginator extends JSONable {

	@JSONField
	public int page;
	/**
	 * -1, если бесконечно
	 */
	@JSONField
	public int maximum_page;

	@JSONField
	public String prev_href, next_hrev;

}
