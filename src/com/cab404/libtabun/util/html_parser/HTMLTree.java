package com.cab404.libtabun.util.html_parser;

import com.cab404.libtabun.util.SU;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Простой парсер HTML. Даже ошибки немного исправляет.
 *
 * @author cab404
 */
public class HTMLTree implements Iterable<Tag> {

    int start = 0;
    int end = 0;

    private LevelAnalyzer leveled;
    private List<Tag> tags;
    public final StringBuilder html;


    @Override
    public Iterator<Tag> iterator() {
        return new Iterator<Tag>() {
            int i = start;
            @Override public boolean hasNext() {
                return i < end;
            }
            @Override public Tag next() {
                return tags.get(i++);
            }
            @Override public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public Tag get(int index) {
        return tags.get(index + start);
    }

    public int getLevel(int index) {
        return leveled.get(index + start).getLevel();
    }

    public int getLevel(Tag tag) {
        return leveled.get(tag.index).getLevel();
    }


    private HTMLTree(HTMLTree tree) {
        tags = new ArrayList<>();
        this.html = tree.html;
    }

    private static TagParser fromString(String text) {
        TagParser parser = new TagParser();
        parser.process(text);
        return parser;
    }

    @Deprecated
    public HTMLTree(String text) {
        this(fromString(text));
    }

    public HTMLTree(TagParser parser) {
        html = parser.full_data;

        tags = Collections.unmodifiableList(parser.tags);

        end = tags.size();

        leveled = new LevelAnalyzer(this);
        leveled.fixLayout();

        for (int i = 0; i < end; i++)
            tags.get(i).index = i;
    }

    public List<Tag> getAllTagsByProperty(String key, String value) {
        ArrayList<Tag> _return = new ArrayList<>();
        for (Tag tag : this)
            if (value.equals(tag.props.get(key)))
                _return.add(tag);
        return _return;
    }

    public List<Tag> getAllTagsByName(String name) {
        ArrayList<Tag> _return = new ArrayList<>();
        for (Tag tag : this)
            if (tag.name.equals(name))
                _return.add(tag);
        return _return;
    }

    public Tag getTagByName(String name) {
        for (Tag tag : this)
            if (tag.name.equals(name))
                return tag;
        throw new TagNotFoundException();
    }

    public Tag getTagByProperty(String key, String value) {
        return tags.get(getTagIndexByProperty(key, value));
    }

    public int getTagIndexByProperty(String key, String value) {
        for (int i = 0; i != tags.size(); i++)
            if (value.equals(tags.get(i).props.get(key)))
                return i;
        throw new TagNotFoundException();
    }

    private int getIndexForTag(Tag tag) {
        return tag.index - start;
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


    public int getClosingTag(Tag tag) {

        int index = getIndexForTag(tag) + 1, level = getLevel(tag);
        for (; index < end; index++) {

            Tag check = get(index);
            int c_level = getLevel(check);

            if (c_level == level && check.name.equals(tag.name))
                return getIndexForTag(check);

        }

        throw new TagNotFoundException();
    }

    public String getContents(int index) {
        return getContents(get(index));
    }

    public String getContents(Tag tag) {

        return html.substring(tag.end, get(getClosingTag(tag)).start);

    }


    /**
     * Возвращает уровень тегов целиком.
     */
    public HTMLTree getTree(Tag tag) {
        return getTree(tag.index);
    }

    /**
     * Возвращает уровень тегов целиком.
     */
    public HTMLTree getTree(int index) {
        Tag start = get(index);
        if (start.isClosing()) throw new RuntimeException("Попытка достать парсер для закрывающего тега!");
        if (start.isStandalone()) throw new RuntimeException("Попытка достать парсер для standalone-тега!");

        HTMLTree _return = new HTMLTree(this);
        _return.leveled = leveled;
        _return.start = index;
        _return.tags = tags;
        _return.end = getClosingTag(start) + 1;

        return _return;
    }

    public List<Tag> getTopChildren(Tag tag) {
        if (tag.isClosing()) throw new RuntimeException("Попытка достать теги верхнего уровня для закрывающего тега!");
        if (tag.isStandalone()) throw new RuntimeException("Попытка достать теги верхнего уровня для standalone-тега!");


        ArrayList<Tag> _return = new ArrayList<>();


        int index = getIndexForTag(tag) + 1, level = getLevel(tag);

        for (; index < tags.size(); index++) {

            Tag check = get(index);
            int c_level = getLevel(check);

            if (getLevel(check) - 1 == level)
                if (check.isOpening() || check.isStandalone())
                    _return.add(check);

            if (c_level == level)
                break;

        }

        return _return;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        int shift = getLevel(0);

        for (Tag tag : this)
            out
                    .append(SU.tabs(getLevel(tag) - shift))
                    .append(tag)
                    .append("\n");
        return out.toString();
    }

    public static class TagNotFoundException extends RuntimeException {
        public TagNotFoundException() {
            super();
        }
    }

    public List<Tag> copyList() {
        return new ArrayList<>(tags.subList(start, end));
    }


    /**
     * Very simple implementation of XPath language interpreter.
     */
    public List<Tag> xPath(String path) {
        List<Tag> results = copyList();

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
                    if (tag.isOpening())
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