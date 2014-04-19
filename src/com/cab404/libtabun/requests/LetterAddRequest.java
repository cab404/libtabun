package com.cab404.libtabun.requests;

import com.cab404.libtabun.data.Letter;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.EntrySet;
import com.cab404.moonlight.util.SU;
import com.cab404.moonlight.util.U;
import org.json.simple.JSONObject;

/**
 * @author cab404
 */
public class LetterAddRequest extends LSCreateRequest {

    private Letter letter;
    @Override public String getURL(AccessProfile profile) {
        return "/talk/add";
    }

    public LetterAddRequest(Letter letter) {
        this.letter = letter;
    }

    @Override public void getData(EntrySet<String, String> data) {
        data.put("talk_users", SU.join(letter.recipients, ", "));
        data.put("talk_title", letter.title);
        data.put("talk_text", letter.text);
        data.put("submit_talk_add", "");
    }

}
