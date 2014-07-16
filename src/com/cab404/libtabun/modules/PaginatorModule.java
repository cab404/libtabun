package com.cab404.libtabun.modules;

import com.cab404.libtabun.data.Paginator;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.ModuleImpl;
import com.cab404.moonlight.parser.HTMLTree;
import com.cab404.moonlight.parser.Tag;
import com.cab404.moonlight.util.SU;

/**
 * @author cab404
 */
public class PaginatorModule extends ModuleImpl<Paginator> {

	@Override public Paginator extractData(HTMLTree page, AccessProfile profile) {
		Paginator paginator = new Paginator();

		paginator.page = page.xPathStr("div/ul/li&class=active/span");
		paginator.maximum_page = Integer.parseInt(
				SU.sub(
						page.xPathFirstTag("div/ul/li/a&title=последняя").get("href"),
						"page",
						"/")
		);

		return paginator;
	}

	@Override public boolean doYouLikeIt(Tag tag) {
		return "pagination".equals(tag.get("class"));
	}

}
