package com.cab404.libtabun.parts;

import com.cab404.libtabun.U;
import com.cab404.libtabun.facility.RequestFactory;
import com.cab404.libtabun.facility.ResponseFactory;
import javolution.util.FastList;

/**
 * PaWPoL с рейтингом, описанием и читателями.
 *
 * @author cab404
 */
public class Blog extends PaWPoL {
    public String name, url_name;
    public FastList<PostLabel> posts;
    public int numpages = 0;

    public Blog() {
    }

    public Blog(User user, String url_name) {
        this.url_name = url_name;
        switchToPage(user, 1);
    }

    public void switchToPage(User user, int page) {
        ResponseFactory.read(
                user.execute(RequestFactory.get("/blog/" + url_name + "/page" + page).build()),
                new BlogParser()
        );
    }

    private class BlogParser implements ResponseFactory.Parser {
        PostLabelListParser pllp;
        int part = 0;

        public BlogParser() {
            pllp = new PostLabelListParser(PostLabelListParser.EndsWith.PAGINATOR);
        }

        @Override
        public boolean line(String line) {
            switch (part) {
                case 0:
                    if (line.contains("\"page-header\"")){
                        name = U.sub(line, ">", "<").trim();
                        part++;
                    }
                case 1:
                    if (!pllp.line(line)) {
                        posts = pllp.labels;
                        part++;
                    }
                    break;
                case 2:
                    if (line.contains("title=\"последняя\"")) {
                        numpages = Integer.parseInt(U.sub(line, "page", "/"));
                        return false;
                    }
                    break;
            }
            return true;
        }
    }


}
