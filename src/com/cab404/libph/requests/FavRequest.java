package com.cab404.libph.requests;

import com.cab404.libph.data.Type;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.EntrySet;

/**
 * @author cab404
 */
public class FavRequest extends LSRequest {

	private final Type type;
	private final int id;
	private final boolean add;

	/**
	 * @param add true - добавить в избранное, false - удалить
	 */
	public FavRequest(Type type, int id, boolean add) {
		this.type = type;
		this.id = id;
		this.add = add;
	}

	@Override protected void getData(EntrySet<String, String> data) {
		data.put("id" + type.name, id + "");
		data.put("type", add ? "1" : "0");
	}

	@Override protected String getURL(AccessProfile profile) {
		return "/ajax/favourite/" + type.name.toLowerCase() + "/";
	}

}
