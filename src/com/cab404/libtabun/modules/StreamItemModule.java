package com.cab404.libtabun.modules;

import com.cab404.libtabun.data.Profile;
import com.cab404.libtabun.data.StreamItem;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.ModuleImpl;
import com.cab404.moonlight.parser.HTMLTree;
import com.cab404.moonlight.parser.Tag;
import com.cab404.moonlight.util.SU;

/**
 * @author cab404
 */
public class StreamItemModule extends ModuleImpl<StreamItem> {

	@Override public StreamItem extractData(HTMLTree page, AccessProfile profile) {
		StreamItem item = new StreamItem();
		String clazz = page.get(0).get("class");

		for (StreamItem.Type type : StreamItem.Type.values())
			if (clazz.contains(type.name().toLowerCase())) {
				item.type = type;
				break;
			}
		if (item.type == null) System.err.println("UNKNOWM ITEM CLASS: " + clazz);

		item.user = new Profile();
		item.user.login = page.xPathStr("p/a/strong");
		item.user.mid_icon = page.xPathFirstTag("a/img").get("src");
		item.user.fillImages();

		Tag data;
		try {
			data = page.xPath("a").get(1);
		} catch (Exception ex) {
			data = page.xPath("span/a").get(1);
		}

		item.data = SU.deEntity(page.getContents(data));
		item.link = data.get("href");

		item.date = page.xPathFirstTag("p/span").get("title");

		return item;
	}

	@Override public boolean doYouLikeIt(Tag tag) {
		return "li".equals(tag.name) && tag.get("class").contains("stream-item");
	}
}
