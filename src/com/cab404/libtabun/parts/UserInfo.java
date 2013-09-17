package com.cab404.libtabun.parts;

import com.cab404.libtabun.U;
import com.cab404.libtabun.facility.RequestFactory;
import com.cab404.libtabun.facility.ResponseFactory;
import javolution.util.FastList;

/**
 * @author cab404
 */
public class UserInfo {
    public float skill, votes;
    public String name, nick, about, small_icon, mid_icon, big_icon, photo;

    public FastList<Userdata> personal;
    public FastList<Contact> contacts;
    public FastList<String> friends;

    public UserInfo() {
        personal = new FastList<>();
        contacts = new FastList<>();
        name = nick = about = small_icon = big_icon = mid_icon = photo = "";
    }

    public void fillImages(){
        String uni = "";
        if (!small_icon.isEmpty()) uni = small_icon.replace("24x24","***");
        if (!mid_icon.isEmpty()) uni = small_icon.replace("48x48","***");
        if (!big_icon.isEmpty()) uni = small_icon.replace("100x100","***");

        small_icon = uni.replace("***","24x24");
        mid_icon = uni.replace("***","48x48");
        big_icon = uni.replace("***","100x100");
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
        int part = 0;
        String temp = "";

        @Override
        public boolean line(String line) {
            switch (part) {
                case 0:
                    if (line.trim().equals("<div class=\"profile\">")) part++;
                    break;
                case 1:
                    if (line.trim().contains("vote_total")) {
                        votes = Float.parseFloat(U.sub(line, ">", "<"));
                        part++;
                    }
                    break;
                case 2:
                    if (line.trim().contains("user_skill")) {
                        skill = Float.parseFloat(U.sub(line, ">", "<"));
                        part++;
                    }
                    break;
                case 3:
                    if (line.trim().contains("class=\"avatar\"")) {
                        big_icon = U.sub(line, "src=\"", "\"");
                        small_icon = big_icon.replace("100x100", "24x24");
                        mid_icon = big_icon.replace("100x100", "48x48");
                        part++;
                    }
                    break;
                case 4:
                    if (line.trim().equals("<h3>О себе</h3>")) part++;
                    break;
                case 5:
                    if (!line.trim().equals("</div>")) {
                        about += line;
                    } else part++;
                    break;
                case 6:
                    if (line.trim().equals("<ul class=\"profile-dotted-list\">")) part++;
                    break;
                case 7:
                    if (line.trim().contains("<h2 class=\"header-table mb-15\">")) {
                        for (String prop : temp.split("<li>")) {
                            if (!prop.trim().isEmpty()) {
                                String key = U.sub(prop, "<span>", "</span>").replace(":", "");
                                String value = U.sub(U.sub(prop, "<strong", "</li>"), ">", "</strong>");
                                personal.add(new Userdata(key, value));
                            }
                        }
                        part++;
                    } else
                        temp += line.trim();
                    break;
                case 8:
                    if (line.trim().equals("<h2 class=\"header-table\">Контакты</h2>")) {
                        part++;
                    }
                    break;
                case 9:
                    if (line.contains("icon-contact icon-contact")) {
                        String type = U.sub(line, "icon-contact-", "\"");
                        String value = U.removeAllTags(U.sub(line, "</i>", "</li>")).trim();
                        contacts.add(new Contact(type, value));
                    } else if (line.contains("ignoreUser")) return false;

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
