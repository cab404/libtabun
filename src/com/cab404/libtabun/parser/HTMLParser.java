package com.cab404.libtabun.parser;

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
        public FastMap<String, String> props;
        public int start, end;
        public String name, text;
        public boolean isClosing;    // Тег типа </x>
        public boolean isStandalone; // Тег типа <x/>

        public Tag() {
            props = new FastMap<>();
        }
    }

    public FastList<Tag> tags;
    public String html;

    Pattern
            tag_pattern = Pattern.compile("<.+?>"),
            property = Pattern.compile("(?<=\\s)(?!\\s)(.*?=\".*?\")"),
            closing = Pattern.compile("</.+?>"),
            alone = Pattern.compile("<.+?/>"),
            name = Pattern.compile("(?<=</|<!|<)(\\w+?)(?=[>\\s])");


    public HTMLParser(String parse) {
        html = parse;
        Matcher matcher = tag_pattern.matcher(parse);

        tags = new FastList<>();
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
            if (!n.find()) continue; // Нашли комментарий.
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

    public Tag getTagByProperty(String key, String value) {
        for (Tag tag : this) {
            if (value.equals(tag.props.get(key))) return tag;
        }
        return null;
    }

    public String getContents(Tag tag) {
        int level = 0, index, findex;

        // Рисуем кружочки.
        for (index = 0; index != tags.size(); index++) if (tags.get(index) == tag) break;
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
}
