package com.cab404.libtabun.util.html_parser;

import com.cab404.libtabun.util.SU;

import java.util.*;

/**
 * Довольно простой эвристический анализатор ошибок HTML.
 * Не любит работать с субдеревьями, ибо использует индексы прямо из тегов.
 */
public class LevelAnalyzer {
    private LinkedList<LeveledTag> tags;

    LevelAnalyzer() {
        tags = new LinkedList<>();
    }

    public void add(Tag tag) {
        tag.index = tags.size();
        tags.add(new LeveledTag(tag, currentLevel()));
//        Добавить код распределения парсинга сюда.
        if (tag.isClosing()) {
            LeveledTag opening = findOpening(tag.index);
            fixLyingLoners(opening.tag.index, tags.size());
            fixIndents(opening.tag.index, tags.size());
        }
    }

    public LeveledTag get(int index) {
        return tags.get(index);
    }
    public int size() {return tags.size();}

    public class LeveledTag {
        public final Tag tag;
        private int level;
        private boolean fixed = false;

        public int getLevel() {
            return level;
        }

        private LeveledTag(Tag tag, int level) {
            this.tag = tag;
            this.level = level;
        }
    }

    private Map<String, Integer> analyzeSlice(int start, int end) {
        HashMap<String, Integer> levels = new HashMap<>();

        for (int i = start; i < end; i++) {
            LeveledTag checking = tags.get(i);
            if (checking.tag.isComment()) continue;
            if (checking.fixed) continue;

            int c_level;

            if (!levels.containsKey(checking.tag.name)) {
                levels.put(checking.tag.name, 0);
                c_level = 0;
            } else {
                c_level = levels.get(checking.tag.name);
            }

            if (checking.tag.isClosing())
                c_level--;
            if (checking.tag.isOpening())
                c_level++;

            levels.put(checking.tag.name, c_level);

        }

        return levels;
    }

    /**
     * Делаем из незакрытых тегов теги standalone (<tag/>)
     */
    private void fixLyingLoners(int start, int end) {
        Map<String, Integer> levels = analyzeSlice(start, end);

        for (int i = start; i < end; i++) {
            LeveledTag tag = tags.get(i);
            if (tag.fixed) continue;
            tag.fixed = true;

            if (tag.tag.isComment()) continue;

            int c_level = levels.get(tag.tag.name);

            if (c_level > 0) {
                if (tag.tag.isOpening()) {
                    tag.tag.type = Tag.Type.STANDALONE;
                    levels.put(tag.tag.name, --c_level);
                }
            }

            if (c_level < 0) {
                if (tag.tag.isStandalone()) {
                    tag.tag.type = Tag.Type.OPENING;

                    levels.put(tag.tag.name, ++c_level);
                }
                if (tag.tag.isClosing()) {
                    tag.tag.type = Tag.Type.STANDALONE;

                    levels.put(tag.tag.name, ++c_level);
                }
            }

        }

        for (Map.Entry<String, Integer> e : levels.entrySet())
            if (e.getValue() != 0)
                throw new RuntimeException("Parsing error - cannot resolve tree at tag " + e.getKey());

    }

    public LeveledTag findOpening(int index) {
        LeveledTag end = tags.getLast();
        int c_level = 0;
        for (int i = index; i >= 0; i--) {
            LeveledTag curr = get(i);
            if (curr.tag.name.equals(end.tag.name)) {
                if (curr.tag.isOpening()) {
                    c_level++;
                    if (c_level == 0)
                        return curr;
                }
                if (curr.tag.isStandalone()) {
                    if (c_level == -1) {
                        curr.tag.type = Tag.Type.OPENING;
                        return curr;
                    }
                }

                if (curr.tag.isClosing())
                    c_level--;

            }
        }

        throw new RuntimeException("WARNING, OPENING TAG NOT FOUND, ABORTING EVERYTHING! PAAANIIIIC!!!!");
    }

    void fixIndents(int start, int end) {
        int layer = 0;

        for (int i = start; i < end; i++) {
            LeveledTag curr = get(i);

            if (curr.tag.isClosing())
                layer--;

            curr.level = layer;

            if (curr.tag.isOpening())
                layer++;

        }
    }

    /**
     * Перерасставляет отступы.
     */
    public void fixLayout() {
        fixIndents(0, tags.size());
    }

    private int currentLevel() {
        if (tags.isEmpty()) return 0;
        else {
            LeveledTag last = tags.getLast();
            if (last.tag.isOpening())
                return last.level + 1;
            else if (last.tag.isClosing())
                return last.level - 1;
            else
                return last.level;
        }
    }

    @Override public String toString() {
        StringBuilder builder = new StringBuilder();
        for (LeveledTag tag : tags) {
            builder
                    .append(tag.level)
                    .append(SU.tabs(tag.level))
                    .append(tag.tag)
                    .append("\n");
        }
        return builder.toString();
    }

    public List<LeveledTag> getSlice(int start, int end) {
        return Collections.unmodifiableList(tags.subList(start, end));
    }

    private Iterable<LeveledTag> tags() {
        return new Iterable<LeveledTag>() {
            @Override public Iterator<LeveledTag> iterator() {
                return new Iterator<LeveledTag>() {
                    int i = 0;
                    @Override public boolean hasNext() {
                        return i < tags.size() - 1;
                    }
                    @Override public LeveledTag next() {
                        return tags.get(++i);
                    }
                    @Override public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

}
