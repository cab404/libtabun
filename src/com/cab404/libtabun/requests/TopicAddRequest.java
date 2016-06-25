package com.cab404.libtabun.requests;

import com.cab404.libtabun.data.Topic;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.EntrySet;
import com.cab404.moonlight.util.SU;
import com.cab404.moonlight.util.U;

/**
 * @author cab404
 */
public class TopicAddRequest extends LSCreateRequest {

    private Topic topic;
    public boolean editable = true;

    public TopicAddRequest(Topic topic) {
        this.topic = topic;
    }

    @Override
    protected void handleResponse(String response) {
        if (!response.trim().isEmpty())
            super.handleResponse(response);
    }

    @Override
    protected void onRedirect(String to) {
        topic.id = U.parseInt(SU.bsub(to, "/", ".html"));
        success = true;
    }

    boolean success = false;

    @Override
    public boolean success() {
        return success;
    }

    @Override
    protected String getURL(AccessProfile profile) {
        return "/topic/add";
    }

    @Override
    protected void getData(EntrySet<String, String> data) {
        data.put("blog_id", topic.blog.id + "");
        data.put("topic_title", topic.title);
        data.put("topic_text", topic.text);
        data.put("topic_tags", SU.join(topic.tags, ", "));
        data.put("topic_type", "topic");
        data.put("topic_forbid_comment", (editable ? 0 : 1)+ "");
        data.put("submit_topic_publish", "");
    }

    @Override
    protected void onSuccess(String url) {
    }

}
