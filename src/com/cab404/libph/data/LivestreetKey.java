package com.cab404.libph.data;

import com.cab404.libph.util.JSONable;

/**
 * @author cab404
 */
public class LivestreetKey extends JSONable {

	@JSONField
	public String key;

	@Deprecated
	public LivestreetKey(String address, String key) {
		this.key = key;
	}

	public LivestreetKey(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return key;
	}

}
