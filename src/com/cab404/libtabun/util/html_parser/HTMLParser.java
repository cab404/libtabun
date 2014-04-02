package com.cab404.libtabun.util.html_parser;

import com.cab404.libtabun.util.SU;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Простой парсер HTML
 *
 * @author cab404
 */
public class HTMLParser implements Iterable<Tag> {

    int offset = 0;

    @Override
    public Iterator<Tag> iterator() {
        return tags.iterator();
    }

    public Tag get(int id) {
        return tags.get(id);
    }


    public List<Tag> tags;
    public String html;

    private HTMLParser() {
        tags = new ArrayList<>();
    }

    public HTMLParser(String parse) {
        this();
        html = parse;
        tags = TagParser.parse(parse);

        for (int i = 0; i < tags.size(); i++)
            tags.get(i).index = i;
    }

    public List<Tag> getAllTagsByProperty(String key, String value) {
        ArrayList<Tag> _return = new ArrayList<>();
        for (Tag tag : this) {
            if (value.equals(tag.props.get(key))) _return.add(tag);
        }
        return _return;
    }

    public List<Integer> getAllIDsByName(String div) {
        ArrayList<Integer> _return = new ArrayList<>();
        for (int i = 0; i != tags.size(); i++) {
            if (div.equals(tags.get(i).name)) _return.add(i);
        }
        return _return;
    }

    public List<Integer> getAllIDsByProperty(String key, String value) {
        ArrayList<Integer> _return = new ArrayList<>();
        for (int i = 0; i != tags.size(); i++) {
            if (value.equals(tags.get(i).props.get(key))) _return.add(i);
        }
        return _return;
    }

    public List<Tag> getAllTagsByName(String name) {
        ArrayList<Tag> _return = new ArrayList<>();
        for (Tag tag : this) {
            if (tag.name.equals(name)) _return.add(tag);
        }
        return _return;
    }

    public Tag getTagByName(String name) {
        for (Tag tag : this) if (tag.name.equals(name)) return tag;
        throw new TagNotFoundException();
    }

    public Tag getTagByProperty(String key, String value) {
        return tags.get(getTagIndexByProperty(key, value));
    }

    public int getTagIndexByProperty(String key, String value) {
        for (int i = 0; i != tags.size(); i++) {
            if (value.equals(tags.get(i).props.get(key))) return i;
        }
        throw new TagNotFoundException();
    }

    public int getIndexForTag(Tag tag) {
        return tag.index - offset;
    }

    public int getTagIndexForName(String name) {
        for (int i = 0; i != tags.size(); i++) if (tags.get(i).name.equals(name)) return i;
        throw new TagNotFoundException();
    }

    public int getTagIndexForParamRegex(String key, String regex) {
        Pattern pattern = Pattern.compile(regex);
        for (int i = 0; i != tags.size(); i++) {
            if (tags.get(i).props.containsKey(key))
                if (pattern.matcher(tags.get(i).props.get(key)).matches()) return i;
        }
        throw new TagNotFoundException();
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

        throw new TagNotFoundException();
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
        _return.offset = index;
        _return.html = html;

        Tag tag = tags.get(index);
        if (tag.isClosing) throw new RuntimeException("Попытка достать парсер для закрывающего тега!");
        if (tag.isStandalone) throw new RuntimeException("Попытка достать парсер для standalone-тега!");

        int level = 0, findex;
        for (findex = index; findex != tags.size(); findex++) {
            Tag check = tags.get(findex);
            _return.tags.add(check);

            if (!check.isComment)
                if (check.isClosing) level--;
                else if (!check.isStandalone) level++;
            if (level == 0 && check.isClosing && check.name.equals(tag.name))
                break;
        }

        return _return;
    }

    public List<Tag> getTopChildren(Tag tag) {
        if (tag.isClosing) throw new RuntimeException("Попытка достать парсер для закрывающего тега!");
        if (tag.isStandalone) throw new RuntimeException("Попытка достать парсер для standalone-тега!");

        int level = 0, index = getIndexForTag(tag);

        ArrayList<Tag> _return = new ArrayList<>();

        for (; index != tags.size(); index++) {
            Tag check = tags.get(index);

            if (!check.isComment && !check.isStandalone)
                level += check.isClosing ? -1 : 0;

//            U.v(level + U.tabs(level) + check.text);

            if (level == 1 && !check.isClosing)
                _return.add(check);

            if (!check.isComment && !check.isStandalone)
                level += check.isClosing ? 0 : +1;

            if (level == 0 && check.isClosing && check.name.equals(tag.name))
                break;

        }

        return _return;
    }

    @Override
    public String toString() {
        return getContents(0);
    }

    public static class TagNotFoundException extends RuntimeException {
        public TagNotFoundException(String s) {
            super(s);
        }

        public TagNotFoundException() {
            super();
        }
    }


    /**
     * Very simple implementation of XPath language interpreter.
     */
    public List<Tag> xPath(String path) {
        ArrayList<Tag> results = new ArrayList<>(tags);

        List<String> request = SU.charSplit(path, '/');

        for (int index = 0; index < request.size(); index++) {

            List<String> node = SU.charSplit(request.get(index), '&');

            String name = node.remove(0);
            for (int i = 0; i < results.size(); ) {
                if (!SU.fast_match(name, results.get(i).name)) {
                    results.remove(i);
                    continue;
                }
                i++;
            }

            for (String quiz : node) {
                List<String> property = SU.charSplit(quiz, 2, '=');

                String p_name = property.get(0);
                String p_val = property.get(1);

                for (int i = 0; i < results.size(); ) {
                    Tag proc = results.get(i);

                    if (!(proc.props.containsKey(p_name) && SU.fast_match(p_val, proc.get(p_name)))) {
                        results.remove(i);
                        continue;
                    }

                    i++;
                }

            }

            if (index < request.size() - 1) {
                ArrayList<Tag> top = new ArrayList<>();
                for (Tag tag : results)
                    if (!(tag.isStandalone || tag.isComment || tag.isClosing))
                        top.addAll(getTopChildren(tag));
                results = top;
            }

        }

        return results;
    }

    public String xPathStr(String str) {
        try {
            return getContents(xPathFirstTag(str));
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            return null;
        }
    }

    public Tag xPathFirstTag(String str) {
        try {
            return xPath(str).get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

}