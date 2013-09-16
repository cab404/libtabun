package com.cab404.libtabun.facility;

import javolution.util.FastList;
import javolution.util.FastMap;

import java.util.Iterator;
import java.util.regex.Matcher;
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

    public static class Tag {
        public int start, end;
        public String name, text;
        public boolean isClosing;    // Тег типа </x>
        public boolean isStandalone; // Тег типа <x/>
        public FastMap<String, String> props;

        public Tag() {
            props = new FastMap<>();
        }
    }

    public FastList<Tag> tags;
    public String html;

    Pattern
            tag_pattern = Pattern.compile("(?s:<.*?>)"),
            property = Pattern.compile("(?<=\\s)(?!\\s)(?s:.*?=\".*?\")"),
            closing = Pattern.compile("(?s:</.*?>)"),
            alone = Pattern.compile("(?s:<.*?/>)"),
            name = Pattern.compile("(?<=</|<!|<)(\\w+?)(?=[>\\s])");

    private HTMLParser() {
        tags = new FastList<>();
    }

    public HTMLParser(String parse) {
        this();
        html = parse;
        Matcher matcher = tag_pattern.matcher(parse);

//        String offset = "";
        // Ищем теги
        while (matcher.find()) {
            Tag out = new Tag();
            out.start = matcher.start();
            out.end = matcher.end();

            // Весь тег целиком
            out.text = parse.substring(matcher.start(), matcher.end());

            // Закрытый/Одиночный тег
            out.isClosing = closing.matcher(out.text).matches();
            out.isStandalone = alone.matcher(out.text).matches();

//            if (out.isClosing) offset = offset.substring(4);

            // Ищем имя тега
            Matcher n = name.matcher(out.text);
            if (!n.find()) continue; // Нашли комментарий или что похуже.
            out.name = out.text.substring(n.start(), n.end());

            // Свойства тега
            Matcher properties = property.matcher(out.text);
//            U.v(offset + out.name);

            while (properties.find()) {
                String[] kw = out.text.substring(properties.start(), properties.end()).split("=", 2);
                out.props.put(kw[0], kw[1].substring(1, kw[1].length() - 1));
//                U.v(offset + "\t" + kw[0] + ":\t" + kw[1].substring(1, kw[1].length() - 1));
            }

//            if (!out.isClosing && !out.isStandalone) offset += "....";

            tags.add(out);
        }
    }

    public FastList<Tag> getAllTagsByProperty(String key, String value) {
        FastList<Tag> _return = new FastList<>();
        for (Tag tag : this) {
            if (value.equals(tag.props.get(key))) _return.add(tag);
        }
        return _return;
    }

    public FastList<Tag> getAllTagsByName(String name) {
        FastList<Tag> _return = new FastList<>();
        for (Tag tag : this) {
            if (tag.name.equals(name)) _return.add(tag);
        }
        return _return;
    }

    public Tag getTagByName(String name) {
        for (Tag tag : this) if (tag.name.equals(name)) return tag;
        throw new Error("Tag not found");
    }

    public Tag getTagByProperty(String key, String value) {
        return tags.get(getTagIndexByProperty(key, value));
    }

    public int getTagIndexByProperty(String key, String value) {
        for (int i = 0; i != tags.size(); i++) {
            if (value.equals(tags.get(i).props.get(key))) return i;
        }
        throw new Error("Tag not found");
    }

    public int getIndexForTag(Tag tag) {
        for (int i = 0; i != tags.size(); i++) if (tags.get(i) == tag) return i;
        throw new Error("Tag not found");
    }

    public String getContents(Tag tag) {
        return getContents(getIndexForTag(tag));
    }

    public String getContents(int index) {
        int level = 0, findex;

        // Рисуем кружочки.
        Tag tag = tags.get(index);
        // Рисуем остальную сову.
        for (findex = index; findex != tags.size(); findex++) {
            Tag check = tags.get(findex);

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
}
