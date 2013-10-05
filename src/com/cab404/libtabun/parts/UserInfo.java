package com.cab404.libtabun.parts;

import com.cab404.libtabun.U;
import com.cab404.libtabun.facility.HTMLParser;
import com.cab404.libtabun.facility.RequestFactory;
import com.cab404.libtabun.facility.ResponseFactory;
import javolution.util.FastList;

/**
 * @author cab404
 */
public class UserInfo {
    public float strength, votes;
    public String name, nick, about, small_icon, mid_icon, big_icon, photo;
    public int id;

    public FastList<Userdata> personal;
    public FastList<Contact> contacts;
    public FastList<String> friends;

    public UserInfo() {
        personal = new FastList<>();
        contacts = new FastList<>();
        name = nick = about = small_icon = big_icon = mid_icon = photo = "";
    }

    public void fillImages() {
        String uni = "";
        if (!small_icon.isEmpty()) uni = small_icon.replace("24x24", "***");
        if (!mid_icon.isEmpty()) uni = small_icon.replace("48x48", "***");
        if (!big_icon.isEmpty()) uni = small_icon.replace("100x100", "***");

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
        String temp = "";

        @Override
        public boolean line(String line) {
            switch (prt) {
                case 0:
                    if (line.trim().equals("</div> <!-- /container -->")) prt++;
                    else temp += line;
                    break;
                case 1: {
                    HTMLParser parser = new HTMLParser(temp);

                    // Достаём более-менее основную инфу.
                    {
                        HTMLParser head_info = parser.getParserForIndex(parser.getTagIndexByProperty("class", "profile"));

                        HTMLParser vote_part = head_info.getParserForIndex(head_info.getTagIndexByProperty("class", "vote-profile"));
                        votes = U.parseFloat(vote_part.getContents(vote_part.getTagIndexByProperty("id", "vote_total_user_" + id)));
                        id = U.parseInt(vote_part.getTagByName("div").props.get("id").replace("vote_area_user_", ""));

                        HTMLParser strength_part = head_info.getParserForIndex(head_info.getTagIndexByProperty("class", "strength"));
                        strength = U.parseFloat(vote_part.getContents(vote_part.getTagIndexByProperty("id", "user_skill_" + id)));

                        nick = head_info.getContents(head_info.getTagByProperty("itemprop", "nickname"));
                        name = head_info.getContents(head_info.getTagByProperty("itemprop", "name"));
                    }

                    // Достаём второстепенную инфу.
                    {
                        HTMLParser about_p = parser.getParserForIndex(parser.getTagIndexByProperty("class", "profile-info-about"));

                        about = about_p.getContents(about_p.getTagIndexByProperty("class", "text"));
                        big_icon = about_p.getTagByProperty("alt", "avatar").props.get("src");
                        fillImages();
                    }

                    // Достаём ещё более второстепенную инфу. И она завёрнута сурово.
                    {
                        HTMLParser lists = parser.getParserForIndex(parser.getTagIndexByProperty("class", "profile-left"));

                        // И сказал cab404 хтмлпарсерам - плодитесь и размножайтесь.
                        for (int id : lists.getAllIDsByProperty("class", "profile-dotted-list")) {
                            HTMLParser dotted = lists.getParserForIndex(id);
                            for (int kv : dotted.getAllIDsByName("div")) {
                                if (!dotted.tags.get(kv).isClosing) {
                                    HTMLParser li = lists.getParserForIndex(kv);
                                    String key = li.getContents(li.getTagIndexForName("span"));
                                    String value = li.getContents(li.getTagIndexForName("strong"));
                                    personal.add(new Userdata(key, value));
                                }
                            }
                        }
                    }

                    // Достаём контакты. Тут легче, ибо <li>шних </li> нету.
                    {
                        HTMLParser contact_p = parser.getParserForIndex(parser.getTagIndexByProperty("class", "profile-right"));
                        for (int i : contact_p.getAllIDsByName("li")) {
                            if (!contact_p.tags.get(i).isClosing) {
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

    public static class Contact {
        public static enum ContactType {
            PHONE("phone"),
            EMAIL("mail"),
            SKYPE("skype"),
            ICQ("icq"),
            SITE("www"),
            TWITTER("twitter"),
            FACEBOOK("facebook"),
            VKONTAKTE("vkontakte"),
            ODNOKLASSNIKI("odnoklassniki"),
            UNKNOWN("???");

            public String name;

            ContactType(String name) {
                this.name = name;
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
    }

    public static class Userdata {
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
    }
}
