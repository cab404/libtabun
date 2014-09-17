package com.cab404.libtabun.data;

import java.io.Serializable;

/**
 * @author cab404
 */
public class LivestreetKey implements Serializable {
	private static final long serialVersionUID = 0L;

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
