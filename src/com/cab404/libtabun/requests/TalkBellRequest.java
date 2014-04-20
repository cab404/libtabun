package com.cab404.libtabun.requests;

import com.cab404.libtabun.data.Letter;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.EntrySet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cab404
 */
public class TalkBellRequest extends LSRequest {

    public List<Letter> responses;
    public List<Letter> new_letters;

    public TalkBellRequest() {
        responses = new ArrayList<>();
        new_letters = new ArrayList<>();
    }

    @Override protected void getData(EntrySet<String, String> data) {}

    @Override protected String getURL(AccessProfile profile) {
        return "/talkbell/";
    }

    @Override protected void handle(JSONObject object) {
        if (!(boolean) object.get("bStError")) {

            if (object.get("aCommentsStuff") != null)
                for (Object non_parsed_letter : (JSONArray) object.get("aCommentsStuff")) {
                    JSONObject letter_data = (JSONObject) non_parsed_letter;
                    Letter letter = new Letter();
                    responses.add(letter);

                    letter.title = (String) letter_data.get("title");
                    letter.id = Integer.parseInt(letter_data.get("id").toString());
                    letter.comments_new = Integer.parseInt(letter_data.get("new_count").toString());
                }

            if (object.get("aTalksStuff") != null)
                for (Object non_parsed_letter : (JSONArray) object.get("aTalksStuff")) {
                    JSONObject letter_data = (JSONObject) non_parsed_letter;
                    Letter letter = new Letter();
                    responses.add(letter);

                    letter.title = (String) letter_data.get("title");
                    letter.id = Integer.valueOf(letter_data.get("id").toString());
                    letter.starter.big_icon = letter_data.get("user_avatar").toString();
                    letter.starter.id = Integer.parseInt(letter_data.get("user_id").toString());
                    letter.starter.login = letter_data.get("user_login").toString();
                    letter.starter.fillImages();
                }

        }
    }


}
