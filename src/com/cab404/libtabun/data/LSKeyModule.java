package com.cab404.libtabun.data;

import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.ModuleImpl;
import com.cab404.moonlight.parser.HTMLTree;
import com.cab404.moonlight.parser.Tag;
import com.cab404.moonlight.util.SU;


/**
 * @author cab404
 */

public class LSKeyModule extends ModuleImpl<String> {

	@Override public String extractData(HTMLTree page, AccessProfile profile) {
		String js = page.getContents(page.get(0));
		if (!js.contains("LIVESTREET_SECURITY_KEY")) return null;
		finish();

		return SU.sub(
				js,
				"LIVESTREET_SECURITY_KEY = '",
				"';"
		);

	}


	@Override public boolean doYouLikeIt(Tag tag) {
		return "script".equals(tag.name);
	}

}
