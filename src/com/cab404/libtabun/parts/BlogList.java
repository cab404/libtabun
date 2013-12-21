package com.cab404.libtabun.parts;

import com.cab404.libtabun.U;
import com.cab404.libtabun.facility.HTMLParser;
import com.cab404.libtabun.facility.MessageFactory;
import com.cab404.libtabun.facility.RequestFactory;
import com.cab404.libtabun.facility.ResponseFactory;
import org.apache.http.client.methods.HttpRequestBase;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Список блогов
 */
public class BlogList implements PaginatedPart {
    public ArrayList<BlogLabel> labels;

    public BlogList() {
        labels = new ArrayList<>();
    }

    public static class BlogLabel {
        public String name, url_name;
        public float votes;
        public int readers;

        @Override public boolean equals(Object o) {
            return o instanceof BlogLabel && name.equals(((BlogLabel) o).name);
        }

        @Override public int hashCode() {
            return name.hashCode();
        }
    }


    public class ListParser implements ResponseFactory.Parser {
        boolean reading = false;
        StringBuilder builder = new StringBuilder();

        @Override public boolean line(String line) {
            if (line.trim().equals("<tbody>")) reading = true;
            if (reading) builder.append(line).append('\n');
            if (line.trim().equals("</tbody>")) {

                HTMLParser main = new HTMLParser(builder.toString());
                labels.clear();

                for (int tag_id : main.getAllIDsByName("tr")) {
                    if (main.get(tag_id).isClosing) continue;

                    BlogLabel lab = new BlogLabel();
                    HTMLParser doc = main.getParserForIndex(tag_id);
                    try {
                        lab.name = doc.getContents(doc.getTagIndexByProperty("class", "blog-name"));
                        lab.votes = U.parseFloat(doc.getContents(doc.getTagIndexForParamRegex("class", "^\\Qcell-rating\\E.+")));
                        lab.readers = U.parseInt(doc.getContents(doc.getTagIndexByProperty("class", "cell-readers")));
                        lab.url_name = U.bsub(doc.getTagByProperty("class", "blog-name").props.get("href"), "/blog/", "/");
                        labels.add(lab);
                    } catch (HTMLParser.TagNotFoundError unchecked) {
                        // Поисковые пегасы ничего не нашли.
                        return false;
                    }
                }
                return false;
            }
            return true;
        }
    }

    int currentPage = 0;

    @Override public boolean loadNextPage(User user) {
        return loadPage(user, ++currentPage);
    }

    public boolean loadPage(User user, int page) {
        currentPage = page;
        ResponseFactory.read(user.execute(RequestFactory.get("/blogs/page" + page).build()), new ListParser());
        return true;
    }

    @Override public boolean hasPages() {
        return false;
    }

    @Override public int getPageCount() {
        return 9000;
    }

    public void find(User user, String phrase) {
        HttpRequestBase request = RequestFactory.post("/blogs/ajax-search/")
                .XMLRequest()
                .setBody("blog_title=" + U.rl(phrase) + "&security_ls_key=" + user.key)
                .build();

        String read = ResponseFactory.read(user.execute(request));

        JSONObject object = MessageFactory.processJSONwithMessage(read);

        String text = (String) object.get("sText");
        if (text == null) return;

        ListParser parser = new ListParser();
        for (String line : text.split("\n")) parser.line(line);
    }
}
