package com.cab404.libtabun.util;

import com.cab404.libtabun.data.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * @author cab404
 *         <p/>
 *         06.11.14
 */
public class TabunJSON {

    public static Blog parseBlog(JSONObject object) {
        if (object == null) return null;

        Blog blog = new Blog();
        if (object.containsKey("name")) blog.name = (String) object.get("name");
        if (object.containsKey("icon")) blog.icon = (String) object.get("icon");
        if (object.containsKey("about")) blog.about = (String) object.get("about");
        if (object.containsKey("rating")) blog.rating = ((Double) object.get("rating")).floatValue();
        if (object.containsKey("readers")) blog.readers = ((Long) object.get("readers")).intValue();
        if (object.containsKey("creation_date"))
            blog.creation_date = Tabun.parseSQLDate((String) object.get("creation_date"));
        if (object.containsKey("url_name")) blog.url_name = (String) object.get("url_name");
        if (object.containsKey("restricted")) blog.restricted = (boolean) object.get("restricted");

        return blog;
    }

    public static Profile parseProfile(JSONObject object) {
        if (object == null) return null;

        Profile profile = new Profile();
        if (object.containsKey("small_icon")) profile.small_icon = (String) object.get("small_icon");
        if (object.containsKey("mid_icon")) profile.mid_icon = (String) object.get("mid_icon");
        if (object.containsKey("big_icon")) profile.big_icon = (String) object.get("big_icon");
        if (object.containsKey("strength")) profile.strength = ((Double) object.get("strength")).floatValue();
        if (object.containsKey("login")) profile.login = (String) object.get("login");
        if (object.containsKey("about")) profile.about = (String) object.get("about");
        if (object.containsKey("photo")) profile.photo = (String) object.get("photo");
        if (object.containsKey("votes")) profile.votes = ((Double) object.get("votes")).floatValue();
        if (object.containsKey("name")) profile.name = (String) object.get("name");
        if (object.containsKey("id")) profile.id = ((Long) object.get("id")).intValue();

        return profile;
    }

    public static Topic parseTopic(JSONObject object) {
        if (object == null) return null;

        Topic topic = new Topic();
        if (object.containsKey("id")) topic.id = ((Long) object.get("id")).intValue();
        if (object.containsKey("date")) topic.date = Tabun.parseSQLDate((String) object.get("date"));
        if (object.containsKey("text")) topic.text = (String) object.get("text");
        if (object.containsKey("title")) topic.title = (String) object.get("title");
        if (object.containsKey("votes")) topic.votes = (String) object.get("votes");
        if (object.containsKey("comments")) topic.comments = ((Long) object.get("comments")).intValue();
        if (object.containsKey("your_vote")) topic.your_vote = ((Long) object.get("your_vote")).intValue();
        if (object.containsKey("blog")) topic.blog = parseBlog((JSONObject) object.get("blog"));
        if (object.containsKey("comments_new")) topic.comments_new = ((Long) object.get("comments_new")).intValue();
        if (object.containsKey("author")) topic.author = parseProfile((JSONObject) object.get("author"));
        if (object.containsKey("vote_enabled")) topic.vote_enabled = (boolean) object.get("vote_enabled");
        if (object.containsKey("in_favourites")) topic.in_favourites = (boolean) object.get("in_favourites");


        if (object.containsKey("tags")) {
            topic.tags = new ArrayList<>();
            for (Object obj : (JSONArray) object.get("tags"))
                topic.tags.add((String) obj);
        }

        return topic;
    }

    public static Comment parseComment(JSONObject object) {
        if (object == null) return null;
        Comment comment = new Comment();

        if (object.containsKey("author")) comment.author = parseProfile((JSONObject) object.get("author"));
        if (object.containsKey("date")) comment.date = Tabun.parseSQLDate((String) object.get("date"));
        if (object.containsKey("deleted")) comment.deleted = (boolean) object.get("deleted");
        if (object.containsKey("in_favs")) comment.in_favs = (boolean) object.get("in_favs");
        if (object.containsKey("is_new")) comment.is_new = (boolean) object.get("is_new");
        if (object.containsKey("parent")) comment.parent = ((Long) object.get("parent")).intValue();
        if (object.containsKey("text")) comment.text = (String) object.get("text");
        if (object.containsKey("votes")) comment.votes = ((Long) object.get("votes")).intValue();
        if (object.containsKey("id")) comment.id = ((Long) object.get("id")).intValue();

        return comment;
    }

    public static Letter parseLetter(JSONObject object) {
        if (object == null) return null;

        Letter letter = new Letter();
        if (object.containsKey("id")) letter.id = ((Long) object.get("id")).intValue();
        if (object.containsKey("date")) letter.date = Tabun.parseSQLDate((String) object.get("date"));
        if (object.containsKey("text")) letter.text = (String) object.get("text");
        if (object.containsKey("title")) letter.title = (String) object.get("title");
        if (object.containsKey("starter")) letter.starter = parseProfile((JSONObject) object.get("starter"));
        if (object.containsKey("comments")) letter.comments = ((Long) object.get("comments")).intValue();
        if (object.containsKey("comments_new")) letter.comments_new = ((Long) object.get("comments_new")).intValue();

        if (object.containsKey("recipients")) {
            letter.recipients = new ArrayList<>();
            for (Object obj : (JSONArray) object.get("recipients"))
                letter.recipients.add((String) obj);
        }

        return letter;
    }

}
