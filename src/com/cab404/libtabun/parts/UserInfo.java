package com.cab404.libtabun.parts;

import com.cab404.libtabun.U;
import com.cab404.libtabun.facility.HTMLParser;
import com.cab404.libtabun.facility.RequestFactory;
import com.cab404.libtabun.facility.ResponseFactory;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author cab404
 */
public class UserInfo {
    public float strength, votes;
    public String name, nick, about, small_icon, mid_icon, big_icon, photo;
    public int id;

    public ArrayList<Userdata> personal;
    public ArrayList<Contact> contacts;
    public ArrayList<String> friends;

    public UserInfo() {
        personal = new ArrayList<>();
        contacts = new ArrayList<>();
        name = nick = about = small_icon = big_icon = mid_icon = photo = "";
    }

    public void fillImages() {
        String uni = "";
        if (!small_icon.isEmpty()) uni = small_icon.replace("24x24", "***");
        else if (!mid_icon.isEmpty()) uni = mid_icon.replace("48x48", "***");
        else if (!big_icon.isEmpty()) uni = big_icon.replace("100x100", "***");

        small_icon = uni.replace("***", "24x24");
        mid_icon = uni.replace("***", "48x48");
        big_icon = uni.replace("***", "100x100");
    }

    public UserInfo(User user, String username) {
        this();
        ResponseFactory.read(
                user.execute(RequestFactory.get("/profile/" + username).build()),
                new UserInfoParser()
        );
    }

    public UserInfo(User user) {
        this(user, user.getLogin());
    }

    public class UserInfoParser implements ResponseFactory.Parser {
        int prt = 0;
        StringBuffer temp = new StringBuffer();

        @Override
        public boolean line(String line) {
            switch (prt) {
                case 0:
                    if (line.trim().equals("</div> <!-- /container -->")) prt++;
                    else temp.append(line).append("\n");
                    break;
                case 1: {
                    HTMLParser parser = new HTMLParser(temp.toString());

                    // Достаём более-менее основную инфу.
                    try {
                        HTMLParser head_info = parser.getParserForIndex(parser.getTagIndexByProperty("class", "profile"));

                        HTMLParser vote_part = head_info.getParserForIndex(head_info.getTagIndexByProperty("class", "vote-profile"));
                        id = U.parseInt(vote_part.tags.get(1).props.get("id").replace("vote_area_user_", ""));
                        votes = U.parseFloat(vote_part.getContents(vote_part.getTagIndexByProperty("id", "vote_total_user_" + id)));

                        HTMLParser strength_part = head_info.getParserForIndex(head_info.getTagIndexByProperty("class", "strength"));
                        strength = U.parseFloat(strength_part.getContents(strength_part.getTagIndexByProperty("id", "user_skill_" + id)));

                        nick = head_info.getContents(head_info.getTagByProperty("itemprop", "nickname"));
                        try {
                            name = head_info.getContents(head_info.getTagByProperty("itemprop", "name"));
                        } catch (HTMLParser.TagNotFoundError e) {
                            name = "";
                        }
                    } catch (HTMLParser.TagNotFoundError e){
                        throw new RuntimeException("Пользователя не существует, или произошло незнамо что.\n" + parser.html, e);
                    }

                    // Достаём второстепенную инфу.
                    {
                        HTMLParser about_p = parser.getParserForIndex(parser.getTagIndexByProperty("class", "profile-info-about"));
                        try {
                            about = about_p.getContents(about_p.getTagIndexByProperty("class", "text"));
                        } catch (HTMLParser.TagNotFoundError e) {
                            about = "";
                        }
                        big_icon = about_p.getTagByProperty("alt", "avatar").props.get("src");
                        fillImages();
                    }

                    // Достаём ещё более второстепенную инфу. И она завёрнута сурово.
                    {
                        HTMLParser lists = parser.getParserForIndex(parser.getTagIndexByProperty("class", "profile-left"));

                        // И сказал cab404 хтмлпарсерам - плодитесь и размножайтесь.
                        for (int list_id : lists.getAllIDsByName("ul")) {
                            if (lists.get(list_id).isClosing) continue;
                            HTMLParser list = lists.getParserForIndex(list_id);
                            for (int entry_id : list.getAllIDsByName("li")) {
                                if (list.get(entry_id).isClosing) continue;
                                HTMLParser entry = list.getParserForIndex(entry_id);

                                try {
                                    String key = entry.getContents(entry.getTagIndexForName("span")).replaceAll(":", "");
                                    String value = entry.getContents(entry.getTagIndexForName("strong"));

                                    personal.add(new Userdata(key, value));
                                } catch (Error e) {
                                    break;
                                }
                            }
                        }
                    }

                    // Достаём контакты. Тут легче, ибо <li>шних </li> нету.
                    {
                        HTMLParser contact_p = parser.getParserForIndex(parser.getTagIndexByProperty("class", "profile-right"));
                        for (int i : contact_p.getAllIDsByName("li")) {
                            if (!contact_p.get(i).isClosing) {
                                String foo = contact_p.getContents(i);
                                String key = U.sub(foo, "title=\"", "\"");
                                String value = U.removeAllTags(foo);
                                contacts.add(new Contact(key, value));
                            }
                        }
                    }
                }
                return false;
            }
            return true;
        }
    }

    public static class Contact implements Map.Entry<String, String> {

        public static enum ContactType {
            PHONE("phone", "Телефон"),
            EMAIL("mail", "Электропочта"),
            SKYPE("skype", "Skype"),
            ICQ("icq", "ICQ"),
            SITE("www", "Сайт"),
            TWITTER("twitter", "Твиттер"),
            FACEBOOK("facebook", "Facebook"),
            VKONTAKTE("vkontakte", "ВК"),
            ODNOKLASSNIKI("odnoklassniki", "Одноклассники"),
            UNKNOWN("???", "");

            public String name;
            public String normal_name;

            ContactType(String name, String normal_name) {
                this.name = name;
                this.normal_name = normal_name;
            }

            @Override
            public String toString() {
                return name;
            }
        }

        ContactType type;
        String value;

        public Contact(String type, String value) {
            this.value = value;
            for (ContactType ctype : ContactType.values()) {
                if (ctype.name.equals(type)) {
                    this.type = ctype;
                    break;
                }
            }
            if (this.type == null) throw new Error("Непонятный тип контакта - " + type);
        }

        @Override public String getKey() {
            return type.normal_name;
        }

        @Override public String getValue() {
            return value;
        }

        @Override public String setValue(String o) {
            // Данунафиг
            return null;
        }
    }

    public static class Userdata implements Map.Entry<String, String> {

        public static enum UserdataType {
            SEX("Пол"),
            BIRTHDAY("Дата рождения"),
            PLACE("Местоположение"),
            CREATED("Создал"),
            MODERATOR("Модерирует"),
            ADMIN("Администрирует"),
            BELONGS("Состоит в"),
            REG_DATE("Зарегистрирован"),
            LAST_VISITED("Последний визит"),;

            public String name;

            UserdataType(String name) {
                this.name = name;
            }

            @Override
            public String toString() {
                return name;
            }
        }

        public String value;
        public UserdataType data_type;

        public Userdata(String type, String value) {
            for (UserdataType dtype : UserdataType.values()) {
                if (dtype.name.equals(type)) {
                    data_type = dtype;
                    break;
                }
            }
            if (data_type == null) throw new Error("Непонятный тип данных - " + type);
            this.value = value;
        }

        @Override public String getKey() {
            return data_type.name;
        }

        @Override public String getValue() {
            return value;
        }

        @Override public String setValue(String o) {
            return null; // Данунафиг[1]
        }
    }
}
