package com.cab404.libtabun.requests;

import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.EntrySet;

/**
 * Работает со списками писем. success() всегда возвращает true, ибо влом лезть в возвращаемый кусок разметки.
 *
 * @author cab404
 */
public class LetterListRequest extends LSRequest {

    private Action action;
    private Integer[] ids;

    public enum Action {DELETE, READ}

    public LetterListRequest(Action action, Integer... ids) {
        this.action = action;
        this.ids = ids;
        success = true;
    }

    @Override protected void getData(EntrySet<String, String> data) {
        for (int id : ids)
            data.put("talk_select[" + id + "]", "on");
        data.put("submit_talk_read", action == Action.READ ? "1" : "0");
        data.put("submit_talk_del", action == Action.DELETE ? "1" : "0");
    }

    @Override protected void handleResponse(String response) {}

    @Override protected String getURL(AccessProfile profile) {
        return "/talk";
    }
}
