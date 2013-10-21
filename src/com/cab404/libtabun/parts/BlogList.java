package com.cab404.libtabun.parts;

import com.cab404.libtabun.U;
import com.cab404.libtabun.facility.HTMLParser;
import com.cab404.libtabun.facility.ResponseFactory;

import java.util.ArrayList;

/**
 * Список блогов
 */
public class BlogList {
    ArrayList<BlogLabel> labels;

    public BlogList() {
        labels = new ArrayList<>();
    }

    public static class BlogLabel {
        String name, url_name;
        float votes;
        int readers;
    }


    public class ListParser implements ResponseFactory.Parser {
        boolean reading = false;
        StringBuilder builder = new StringBuilder();

        @Override public boolean line(String line) {
            if (line.trim().equals("<tbody>")) reading = true;
            if (reading) builder.append(line).append('\n');
            if (line.trim().equals("</tbody>")) {

                HTMLParser main = new HTMLParser(builder.toString());

                for (int tag_id : main.getAllIDsByName("tr")) {

                    if (main.get(tag_id).isClosing) continue;

                    BlogLabel lab = new BlogLabel();
                    HTMLParser doc = main.getParserForIndex(tag_id);

                    lab.name = doc.getContents(doc.getTagIndexByProperty("class", "blog_name"));
                    lab.url_name = U.bsub(doc.getTagByProperty("class", "blog_name").props.get("href"), "/", "/");
                    lab.votes = U.parseFloat(doc.getContents(doc.getTagIndexByProperty("class", "cell-rating align-center")));
                    lab.readers = U.parseInt(doc.getContents(doc.getTagIndexByProperty("class", "cell_readers")));
                }
                return false;
            }
            return true;
        }
    }
}
