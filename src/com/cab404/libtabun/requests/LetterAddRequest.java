package com.cab404.libtabun.requests;

import com.cab404.libtabun.data.Letter;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.EntrySet;
import com.cab404.moonlight.util.SU;

/**
 * @author cab404
 */
public class LetterAddRequest extends LSCreateRequest {

    private Letter letter;
    public LetterAddRequest(Letter letter) { this.letter = letter; }

    public int getID() {
        if (success()) {
            return letter.id;
        }
        return -1;
    }

    @Override protected String getURL(AccessProfile profile) {return "/talk/add";}

    @Override protected void getData(EntrySet<String, String> data) {
        data.put("talk_users", SU.join(letter.recipients, ", ").toString());
        data.put("talk_title", letter.title);
        data.put("talk_text", letter.text);
        data.put("submit_talk_add", "");
    }

    @Override protected void onSuccess(String url) {
        letter.id = Integer.parseInt(SU.bsub(url, "read/", "/"));
    }

}
