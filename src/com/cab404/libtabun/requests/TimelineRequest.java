package com.cab404.libtabun.requests;

import com.cab404.libtabun.data.TimelineEntry;
import com.cab404.libtabun.modules.TimelineModule;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.EntrySet;
import com.cab404.moonlight.parser.HTMLTree;
import com.cab404.moonlight.parser.Tag;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cab404
 */
public class TimelineRequest extends LSRequest {
	public List<TimelineEntry> timeline = new ArrayList<>();

	@Override protected void getData(EntrySet<String, String> data) {}

	@Override protected String getURL(AccessProfile profile) {
		return "/ajax/stream/comment";
	}

	@Override protected void handle(JSONObject object) {
		HTMLTree tree = new HTMLTree((String) object.get("sText"));
		TimelineModule timelineModule = new TimelineModule();

		for (Tag tag : tree)
			if (tag.isOpening() && timelineModule.doYouLikeIt(tag))
				timeline.add(timelineModule.extractData(tree.getTree(tag), null));

	}
}
