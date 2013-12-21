package com.cab404.libtabun.facility;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Простой парсер HTML
 *
 * @author cab404
 */
public class HTMLParser implements Iterable<HTMLParser.Tag> {

    @Override
    public Iterator<Tag> iterator() {
        return tags.iterator();
    }

    public Tag get(int id) {
        return tags.get(id);
    }


    public static class Tag {
        public int start, end;
        public String name, text;
        public boolean isClosing;    // Тег типа </x>
        public boolean isStandalone; // Тег типа <x/>
        public boolean isComment; // Тег типа <!-- x --> и <! x>
        public Map<String, String> props;

        public Tag() {
            props = new HashMap<>();
        }

        @Override public String toString() {
            return new StringBuilder()
                    .append("== TAG ==").append("\n")
                    .append("Code: '").append(text).append("' \n")
                    .append("Name: '").append(name).append("' \n")
                    .append("StA: ").append(isStandalone).append(" \n")
                    .append("Cl: ").append(isClosing).append(" \n")
                    .append("Cm: ").append(isComment).append(" \n")
                    .toString();
        }
    }

    public ArrayList<Tag> tags;
    public String html;

//    Pattern
//            tag_pattern = Pattern.compile("(?s:<.*?>)"),
//            property = Pattern.compile("(?<=\\s)(?!\\s)(?s:.*?=\".*?\")"),
//            closing = Pattern.compile("(?s:</.*?>)"),
//            alone = Pattern.compile("(?s:<.*?/>)"),
//            name = Pattern.compile("(?<=</|<!|<)(\\w+?)(?=[>\\s])");

    private HTMLParser() {
        tags = new ArrayList<>();
    }

    public HTMLParser(String parse) {
        this();
        html = parse;
        tags = HTMLParser2.parse(parse);
//        Matcher matcher = tag_pattern.matcher(parse);
//
////        String offset = "";
//        // Ищем теги
//        while (matcher.find()) {
//            Tag out = new Tag();
//            out.start = matcher.start();
//            out.end = matcher.end();
//
//            // Весь тег целиком
//            out.text = parse.substring(matcher.start(), matcher.end());
//
//            // Закрытый/Одиночный тег
//            out.isClosing = closing.matcher(out.text).matches();
//            out.isStandalone = alone.matcher(out.text).matches();
//
////            if (out.isClosing) offset = offset.substring(4);
//
//            // Ищем имя тега
//            Matcher n = name.matcher(out.text);
//            if (!n.find()) continue; // Нашли комментарий или что похуже.
//            out.name = out.text.substring(n.start(), n.end());
//
//            // Свойства тега
//            Matcher properties = property.matcher(out.text);
////            U.v(offset + out.name);
//
//            while (properties.find()) {
//                String[] kw = out.text.substring(properties.start(), properties.end()).split("=", 2);
//                out.props.put(kw[0], kw[1].substring(1, kw[1].length() - 1));
////                U.v(offset + "\t" + kw[0] + ":\t" + kw[1].substring(1, kw[1].length() - 1));
//            }
//
////            if (!out.isClosing && !out.isStandalone) offset += "....";
//
//            tags.add(out);
//        }
    }

    public ArrayList<Tag> getAllTagsByProperty(String key, String value) {
        ArrayList<Tag> _return = new ArrayList<>();
        for (Tag tag : this) {
            if (value.equals(tag.props.get(key))) _return.add(tag);
        }
        return _return;
    }

    public Vector<Integer> getAllIDsByName(String div) {
        Vector<Integer> _return = new Vector<>();
        for (int i = 0; i != tags.size(); i++) {
            if (div.equals(tags.get(i).name)) _return.add(i);
        }
        return _return;
    }

    public Vector<Integer> getAllIDsByProperty(String key, String value) {
        Vector<Integer> _return = new Vector<>();
        for (int i = 0; i != tags.size(); i++) {
            if (value.equals(tags.get(i).props.get(key))) _return.add(i);
        }
        return _return;
    }

    public ArrayList<Tag> getAllTagsByName(String name) {
        ArrayList<Tag> _return = new ArrayList<>();
        for (Tag tag : this) {
            if (tag.name.equals(name)) _return.add(tag);
        }
        return _return;
    }

    public Tag getTagByName(String name) {
        for (Tag tag : this) if (tag.name.equals(name)) return tag;
        throw new TagNotFoundError();
    }

    public Tag getTagByProperty(String key, String value) {
        return tags.get(getTagIndexByProperty(key, value));
    }

    public int getTagIndexByProperty(String key, String value) {
        for (int i = 0; i != tags.size(); i++) {
            if (value.equals(tags.get(i).props.get(key))) return i;
        }
        throw new TagNotFoundError();
    }

    public int getIndexForTag(Tag tag) {
        for (int i = 0; i != tags.size(); i++) if (tags.get(i) == tag) return i;
        throw new TagNotFoundError();
    }

    public int getTagIndexForName(String name) {
        for (int i = 0; i != tags.size(); i++) if (tags.get(i).name.equals(name)) return i;
        throw new TagNotFoundError();
    }

    public int getTagIndexForParamRegex(String key, String regex) {
        Pattern pattern = Pattern.compile(regex);
        for (int i = 0; i != tags.size(); i++) {
            if (tags.get(i).props.containsKey(key))
                if (pattern.matcher(tags.get(i).props.get(key)).matches()) return i;
        }
        throw new TagNotFoundError();
    }


    public int getClosingTag(int opening_tag_index) {
        int level = 0, check_index;

        Tag tag = tags.get(opening_tag_index);

        for (check_index = opening_tag_index; check_index < tags.size(); check_index++) {
            Tag check = tags.get(check_index);

            if (check.isClosing) level--;
            else if (!check.isStandalone) level++;

            if (level == 0 && check.isClosing && check.name.equals(tag.name))
                return check_index;
        }

        throw new TagNotFoundError();
    }

    public String getContents(Tag tag) {
        return getContents(getIndexForTag(tag));
    }

    public String getContents(int index) {
        int level = 0, check_index;

        // Рисуем кружочки.
        Tag tag = tags.get(index);
        // Рисуем остальную сову.
        for (check_index = index; check_index < tags.size(); check_index++) {
            Tag check = tags.get(check_index);

            if (check.isClosing) level--;
            else if (!check.isStandalone) level++;
            if (level == 0 && check.isClosing && check.name.equals(tag.name))
                return html.substring(tag.end, check.start);
        }

        return "";
    }

    /**
     * Возвращает уровень тегов целиком.
     */
    public HTMLParser getParserForIndex(int index) {
        HTMLParser _return = new HTMLParser();
        _return.html = html;
        int level = 0, findex;

        Tag tag = tags.get(index);
        if (tag.isClosing) throw new RuntimeException("Попытка достать парсер для закрывающего тега!");
        if (tag.isStandalone) throw new RuntimeException("Попытка достать парсер для standalone-тега!");
        for (findex = index; findex != tags.size(); findex++) {
            Tag check = tags.get(findex);
            _return.tags.add(check);

            if (check.isClosing) level--;
            else if (!check.isStandalone) level++;
            if (level == 0 && check.isClosing && check.name.equals(tag.name))
                break;
        }

        return _return;
    }

    @Override
    public String toString() {
        return getContents(0);
    }

    public static class TagNotFoundError extends RuntimeException {
        public TagNotFoundError(String s) {
            super(s);
        }

        public TagNotFoundError() {
            super();
        }
    }
}
