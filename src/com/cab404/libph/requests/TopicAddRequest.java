package com.cab404.libph.requests;

import com.cab404.libph.data.Topic;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.EntrySet;
import com.cab404.moonlight.util.SU;
import com.cab404.moonlight.util.U;

/**
 * @author cab404
 */
public class TopicAddRequest extends LSCreateRequest {

	private Topic topic;

	public TopicAddRequest(Topic topic) { this.topic = topic; }

	@Override protected void onRedirect(String to) {
		super.onRedirect(to);
		topic.id = U.parseInt(SU.bsub(to, "/", ".html"));
	}

	@Override protected String getURL(AccessProfile profile) {
		return "/topic/add";
	}

	@Override protected void getData(EntrySet<String, String> data) {
		data.put("blog_id", topic.blog.id + "");
		data.put("topic_title", topic.title);
		data.put("topic_text", topic.text);
		data.put("topic_tags", SU.join(topic.tags, ", ").toString());
		data.put("topic_type", "topic");
		data.put("submit_topic_publish", "");
	}

	@Override protected void onSuccess(String url) {
	}

}
