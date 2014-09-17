package com.cab404.libtabun.requests;

import com.cab404.libtabun.data.Type;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.EntrySet;
import org.json.simple.JSONObject;

/**
 * @author cab404
 */
public class VoteRequest extends LSRequest {


	private final int id;
	private final int vote;
	private final Type type;
	public float result = -9000;

	/**
	 * @param type Константа из {@link com.cab404.libtabun.data.Type}
	 */
	public VoteRequest(int id, int vote, Type type) {
		this.id = id;
		this.vote = vote;
		this.type = type;
	}

	@Override protected void handle(JSONObject object) {
		super.handle(object);
		if (success)
			result = Float.parseFloat(object.get("iRating") + ""); // Ибо Long, ибо нафиг.
	}

	@Override protected void getData(EntrySet<String, String> data) {
		data.put("value", vote + "");
		data.put("id" + type.name, id + "");
	}

	@Override protected String getURL(AccessProfile profile) {
		return "/ajax/vote/" + type.name.toLowerCase() + "/";
	}

}
