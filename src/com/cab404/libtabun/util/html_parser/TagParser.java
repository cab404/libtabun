package com.cab404.libtabun.util.html_parser;

import com.cab404.libtabun.util.SU;
import com.cab404.libtabun.util.html_parser.Tag.Type;

import java.util.List;

/**
 * +20% cooler; +4k% faster;
 *
 * @author cab404
 */
public class TagParser {
    StringBuilder buffer, full_data;
    private TagHandler handler;

    TagParser(TagHandler handler) {
        this.handler = handler;
        full_data = new StringBuilder();
        buffer = new StringBuilder();
    }

    private static final String
            COMM_START = "!--",
            COMM_END = "-->",
            TAG_START = "<",
            TAG_END = ">";

    int prev = 0;
    int i, j = 0;

    synchronized void process(String line) {
        buffer.append(line);
        full_data.append(line);

        while (true) {

            i = buffer.indexOf(TAG_START, 0);
            j = buffer.indexOf(TAG_END, i);
            if (i == -1 || j == -1) break;

            Tag tag = new Tag();
            tag.type = Type.OPENING;
            tag.start = prev + i;
            tag.end = prev + j + 1;
            tag.text = buffer.substring(i, j + 1);

            String inner = buffer.substring(i + 1, j);
            int l = inner.length() - 1;


            if (inner.startsWith(COMM_START)) {
                tag.type = Type.COMMENT;
                tag.name = COMM_START;

                j = buffer.indexOf(COMM_END, i);
                if (j == -1) break;
                j += COMM_START.length();
                tag.text = buffer.substring(i, j);
                j--;

                handler.handle(tag);
                step();
                continue;
            }


            if (inner.charAt(0) == '/') {
                tag.type = Type.CLOSING;
                inner = buffer.substring(i + 2, j);
            } else if (inner.charAt(l) == '/') {
                tag.type = Type.STANDALONE;
                inner = buffer.substring(i + 1, j - 1);
            }


            List<String> name_and_everything_else = SU.charSplit(inner, 2, ' ');
            tag.name = name_and_everything_else.get(0);

            if (tag.name.charAt(0) == '!')
                tag.type = Type.COMMENT; // Handling !doctype and others.

            if (name_and_everything_else.size() == 2) {
                // Parsing properties.
                String params = name_and_everything_else.get(1).trim();
                String key = null;
                int s = 0;
                boolean quot = false;
                int mode = 0; // 0 - ищем конец ключа, 1 - ищем начало значения, 2 - ищем конец значения.

                for (int index = 0; index < params.length(); index++) {
                    char current = params.charAt(index);
                    if (mode == 0 && current == '=') {
                        key = params.substring(s, index);
                        mode = 1;
                        continue;
                    }
                    if (mode == 1 && (current == '"' || current == '\'')) {
                        quot = current == '\'';
                        s = index + 1;
                        mode = 2;
                        continue;
                    }
                    if (mode == 2 && current == (quot ? '\'' : '"')) {
                        tag.props.put(key.trim(), params.substring(s, index));
                        s = index + 1;
                        mode = 0;
                    }

                }

            }

            step();
            handler.handle(tag);
        }
    }

    private void step() {
        buffer.delete(0, j + 1);
        prev += j + 1;
    }

    static abstract interface TagHandler {
        public void handle(Tag tag);
    }

}