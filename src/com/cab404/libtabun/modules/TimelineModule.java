package com.cab404.libtabun.modules;

import com.cab404.libtabun.data.Profile;
import com.cab404.libtabun.data.TimelineEntry;
import com.cab404.libtabun.data.Topic;
import com.cab404.libtabun.util.Tabun;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.ModuleImpl;
import com.cab404.moonlight.parser.HTMLTree;
import com.cab404.moonlight.parser.Tag;
import com.cab404.moonlight.util.SU;

/**
 * @author cab404
 */
public class TimelineModule extends ModuleImpl<TimelineEntry> {

	@Override public TimelineEntry extractData(HTMLTree page, AccessProfile profile) {
		TimelineEntry entry = new TimelineEntry();

		entry.commenter = new Profile();
		entry.topic = new Topic();

		entry.comment_id = Integer.parseInt(SU.bsub(page.xPathFirstTag("a&class=stream-topic").get("href"), "#comment", ""));
		entry.commenter.login = page.xPathStr("p/a&class=author");

		entry.topic.comments = Integer.parseInt(SU.removeAllTags(page.xPathStr("span&class=block-item-comments")));
		entry.topic.title = page.xPathStr("a&class=stream-topic");


		entry.topic.blog = Tabun.resolveURL(page.xPathFirstTag("p/a&class=stream-blog").get("href"));
		entry.topic.blog.name = page.xPathStr("p/a&class=stream-blog");

		return entry;
	}

	@Override public boolean doYouLikeIt(Tag tag) {
		return "li".equals(tag.name);
	}

}
