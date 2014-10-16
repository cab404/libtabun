package com.cab404.libph.modules;

import com.cab404.libph.data.TabunError;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.ModuleImpl;
import com.cab404.moonlight.parser.HTMLTree;
import com.cab404.moonlight.parser.Tag;

/**
 * @author cab404
 */
public class ErrorModule extends ModuleImpl<TabunError> {

	@Override public TabunError extractData(HTMLTree page, AccessProfile profile) {
		String err_msg = page.xPathStr("h2/span");

		if (err_msg == null) return null;

		if ("404".equals(err_msg)) return TabunError.NOT_FOUND;
		if ("Нет доступа".equals(err_msg)) return TabunError.ACCESS_DENIED;

		return TabunError.UNKNOWN;
	}

	@Override public boolean doYouLikeIt(Tag tag) {
		return tag.get("class").equals("content-error");
	}

}
