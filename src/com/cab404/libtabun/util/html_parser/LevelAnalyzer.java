package com.cab404.libtabun.util.html_parser;

import com.cab404.libtabun.util.U;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Довольно простой эвристический анализатор ошибок HTML.
 * Не любит работать с субдеревьями, ибо использует индексы прямо из тегов.
 */
class LevelAnalyzer {
    private LinkedList<LeveledTag> tags;

    LevelAnalyzer(HTMLTree tree) {
        tags = new LinkedList<>();
        for (Tag tag : tree)
            tags.add(new LeveledTag(tag, currentLevel()));
    }

    public LeveledTag get(int index) {
        return tags.get(index);
    }

    public class LeveledTag {
        public final Tag tag;
        private int level;

        public int getLevel() {
            return level;
        }

        private LeveledTag(Tag tag, int level) {
            this.tag = tag;
            this.level = level;
        }
    }

    /**
     * Делаем из незакрытых тегов теги standalone (<tag/>)
     */
    private void fixLyingLoners(LeveledTag start) {
        HashMap<String, Integer> levels = new HashMap<>();

        for (int i = tags.size() - 1; i > start.tag.index; i--) {
            LeveledTag checking = tags.get(i);

            int c_level;
            if (!levels.containsKey(checking.tag.name)) {
                levels.put(checking.tag.name, 0);
                c_level = 0;
            } else {
                c_level = levels.get(checking.tag.name);
            }


            if (checking.tag.isClosing()) {
                c_level++;
                levels.put(checking.tag.name, c_level);
            }

            if (checking.tag.isOpening()) {
                c_level--;
                if (c_level < 0) {
                    checking.tag.type = Tag.Type.STANDALONE;
                    c_level++;
                }
                levels.put(checking.tag.name, c_level);
            }

        }

    }

    /**
     * Перерасставляет отступы и закрываем ненужные теги.
     */
    public void fixLayout() {
        int layer = 0;

        for (LeveledTag tag : tags)
            if (tag.tag.isOpening() && tag.level == 0)
                fixLyingLoners(tag);

        for (LeveledTag curr : tags) {

            if (curr.tag.isClosing())
                layer--;

            curr.level = layer;

            if (curr.tag.isOpening())
                layer++;

        }
    }

    private int currentLevel() {
        if (tags.isEmpty()) return 0;
        else {
            LeveledTag last = tags.getLast();
            if (last.tag.isOpening())
                return last.level + 1;
            else
                return last.level;
        }
    }

    @Override public String toString() {
        StringBuilder builder = new StringBuilder();
        for (LeveledTag tag : tags) {
            builder.append(U.tabs(tag.level)).append(tag.tag).append("\n");
        }
        return builder.toString();
    }
}
