package com.cab404.libtabun.data;

import com.cab404.libtabun.util.JSONable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author cab404
 */
public class Profile extends JSONable {
    @JSONField
    public Float strength, votes;
    @JSONField
    public String name, login, about, small_icon, mid_icon, big_icon, photo;
    @JSONField
    public Integer id;
    @JSONField
    public final ArrayList<Profile> partial_friend_list;
    @JSONField
    public final List<Map.Entry<UserInfoType, String>> personal;
    @JSONField
    public final List<Map.Entry<ContactType, String>> contacts;

    public Profile() {
        personal = new ArrayList<>();
        contacts = new ArrayList<>();
        partial_friend_list = new ArrayList<>();
    }

    public List<String> get(ContactType type) {
        ArrayList<String> list = new ArrayList<String>();

        for (Map.Entry<ContactType, String> e : contacts)
            if (e.getKey().equals(type))
                list.add(e.getValue());

        return list;
    }

    public String get(UserInfoType type) {
        for (Map.Entry<UserInfoType, String> e : personal)
            if (e.getKey().equals(type))
                return e.getValue();

        return null;
    }


    public void fillImages() {
        String uni = "";
        if (small_icon != null && !small_icon.isEmpty()) uni = small_icon.replace("24x24", "***");
        else if (mid_icon != null && !mid_icon.isEmpty()) uni = mid_icon.replace("48x48", "***");
        else if (big_icon != null && !big_icon.isEmpty()) uni = big_icon.replace("100x100", "***");

        small_icon = uni.replace("***", "24x24");
        mid_icon = uni.replace("***", "48x48");
        big_icon = uni.replace("***", "100x100");
    }

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

    public static enum UserInfoType {
        GENDER("Пол"),
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

        UserInfoType(String name) {
            this.name = name;
        }


        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Возвращает тип данных по его нормальному названию на русском языке, перебирая все элементы.
     * Если не находит, то кидает RuntimeException.
     * <p/>
     * Для этого места нормально.
     * Вряд ли понадобится неимоверно часто доставать типы контактов,
     * да и самих типов мало.
     * Иначе я бы добавил бинарный поиск, тем более, что массив данных
     * не меняется.
     * Тоже относится и к {@link com.cab404.libtabun.data.Profile#getContactType(String)}.
     *
     * @throws java.lang.RuntimeException Если не находит тип данных.
     */
    public static UserInfoType getDataType(String name) {
        for (UserInfoType type : UserInfoType.values())
            if (type.name.equals(name))
                return type;
        throw new RuntimeException("Неподдерживаемый тип данных: " + name);
    }

    /**
     * @see com.cab404.libtabun.data.Profile#getDataType(String)
     */
    public static ContactType getContactType(String name) {
        for (ContactType type : ContactType.values())
            if (type.name.equals(name))
                return type;
        throw new RuntimeException("Неподдерживаемый тип данных: " + name);
    }


}
