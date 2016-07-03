package com.cab404.libtabun.requests;

import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.EntrySet;

/**
 * @author cab404
 */
public class TopicSubscriptionRequest extends LSRequest {
    private final int topic;
    private final boolean subscribe;

    public TopicSubscriptionRequest(int topic, boolean subscribe) {
        this.topic = topic;
        this.subscribe = subscribe;
    }

    @Override
    protected void getData(EntrySet<String, String> data) {
            data.put("target_type", "topic_new_comment");
            data.put("target_id", topic + "");
            data.put("mail", "");
            data.put("value", (subscribe ? 1 : 0) + "") ;
    }

    @Override
    protected String getURL(AccessProfile profile) {
        return "/subscribe/ajax-subscribe-toggle";
    }
}
