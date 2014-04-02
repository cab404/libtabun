package com.cab404.libtabun.parts;

import com.cab404.libtabun.facility.RequestFactory;
import com.cab404.libtabun.facility.ResponseFactory;
import com.cab404.libtabun.util.SU;
import com.cab404.libtabun.util.U;
import com.cab404.libtabun.util.html_parser.HTMLParser;
import com.cab404.libtabun.util.html_parser.Tag;

import java.util.ArrayList;
import java.util.List;
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
        try {
            ResponseFactory.read(
                    user.execute(RequestFactory.get("/profile/" + username).build()),
                    new UserInfoParser()
            );
        } catch (Exception ex) {
            throw new RuntimeException("Ошибка при попытке достать страницу /profile/" + username, ex);
        }
    }

    public UserInfo(User user) {
        this(user, user.getLogin());
    }

    public class UserInfoParser extends U.TextPartParser {

        @Override public void process(StringBuilder out) {
            HTMLParser parser = new HTMLParser(out.toString());

            // Достаём более-менее основную инфу.
            try {

                id = U.parseInt(SU.bsub(parser.xPathFirstTag("div&class=profile/div&class=vote-profile/div&id=*user_*").get("id"), "_", ""));
                votes = U.parseFloat(parser.xPathStr("div&class=profile/div&class=vote-profile/div/div&class=*count/span"));
                strength = U.parseFloat(parser.xPathStr("div&class=profile/div&class=strength/div&class=count"));
                nick = parser.xPathStr("div&class=profile/h2&itemprop=nickname");

                name = parser.xPathStr("div&class=profile/p&itemprop=name");
                name = name == null ? "" : name;

            } catch (Exception e) {
                throw new RuntimeException("Пользователя не существует, или произошло незнамо что.\n" + parser.html, e);
            }

            // Достаём второстепенную инфу.
            {
                about = parser.xPathStr("div&class=*about/p&class=text");
                big_icon = parser.xPathFirstTag("div&class=*about/a&class=avatar/img").get("src");
                fillImages();
            }

            {
                List<Tag> spans = parser.xPath("div&class=wrapper/div&class=*left/ul/li/span");
                List<Tag> data = parser.xPath("div&class=wrapper/div&class=*left/ul/li/strong");
                for (int i = 0; i < spans.size(); i++) {
                    String key = SU.sub(parser.getContents(spans.get(i)), "", ":");
                    String value = parser.getContents(data.get(i));

                    personal.add(new Userdata(key, value));
                }

            }

            // Достаём контакты. Тут легче, ибо <li>шних </li> нету.
            {
                List<Tag> liList = parser.xPath("div&class=wrapper/div&class=*right/ul/li");
                for (Tag tag : liList) {
                    String foo = parser.getContents(tag);
                    String key = SU.sub(foo, "title=\"", "\"");
                    String value = SU.removeAllTags(foo);
                    contacts.add(new Contact(key, value));
                }
            }
        }

        @Override public boolean isStart(String line) {
            return line.trim().equals("<div class=\"profile\">");
        }
        @Override public boolean isEnd(String line) {
            return line.trim().equals("</div> <!-- /container -->");
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
            HAS_INVITED("Приглашенные"),
            INVITED_BY("Пригласил"),
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
            if (data_type == null) throw new RuntimeException("Непонятный тип данных - " + type);
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
