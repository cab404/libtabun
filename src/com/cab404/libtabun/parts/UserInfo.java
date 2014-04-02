package com.cab404.libtabun.parts;

import java.util.ArrayList;
import java.util.Map;

/**
 *
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
            ODNOKLASSNIKI("odnoklassniki", "Одноклассники"),;

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
