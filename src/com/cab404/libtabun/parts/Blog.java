package com.cab404.libtabun.parts;

import com.cab404.libtabun.data.PostLabel;
import com.cab404.libtabun.util.SU;
import com.cab404.libtabun.facility.RequestFactory;
import com.cab404.libtabun.facility.ResponseFactory;

import java.util.ArrayList;

/**
 * PaWPoL с рейтингом, описанием и читателями.
 *
 * @author cab404
 */
public class Blog extends PaWPoL implements PaginatedPart {
    public String name, url_name;
    public ArrayList<PostLabel> posts;
    public int numpages = 0;
    public int curpage = 0;
    public LivestreetKey key;

    public Blog() {
        posts = new ArrayList<>();
    }

    public Blog(User user, String url_name) {
        this();
        this.url_name = url_name;
        posts = new ArrayList<>();
    }

    /**
     * Знаю, не нужно было это пихать сюда. Проще говоря - эта штука нужна,
     * чтобы просто достать все лэйблы с заданной страницы.
     */
    public void switchToCustomPage(User user, String relative_address) {
        BlogParser parser = new BlogParser();
        parser.part = 1;

        this.url_name = relative_address;
        ResponseFactory.read(
                user.execute(RequestFactory.get(relative_address).build()),
                parser
        );
    }

    public String getUrl() {
        return "/blog/" + url_name;
    }

    @Override public boolean loadNextPage(User user) {
        return loadPage(user, ++curpage);
    }

    @Override public boolean loadPage(User user, int page) {
        curpage = page;
        ArrayList<PostLabel> tmp = new ArrayList<>();
        tmp.addAll(posts);

        ResponseFactory.read(
                user.execute(RequestFactory.get(getUrl() + "/page" + page).build()),
                new BlogParser()
        );

        if (posts.isEmpty()) {
            posts = tmp;
            return false;
        }
        return true;
    }

    @Override public boolean hasPages() {
        return true;
    }

    @Override public int getPageCount() {
        return numpages;
    }

    private class BlogParser implements ResponseFactory.Parser {
        PostLabelListParser pllp;
        int part = 0;

        public BlogParser() {
            pllp = new PostLabelListParser(PostLabelListParser.EndsWith.PAGINATOR);
            fetcher = new KeyFetcher();
        }

        KeyFetcher fetcher;
        @Override
        public boolean line(String line) {
            switch (part) {
                case 0:
                    if (!fetcher.line(line)) {
                        key = fetcher.key;
                        part++;
                    }
                case 1:
                    if (line.contains("\"page-header\"")) {
                        name = SU.sub(line, ">", "<").trim();
                        part++;
                    }
                case 2:
                    if (!pllp.line(line)) {
                        part++;
                    }
                    posts = pllp.labels;
                    break;
                case 3:
                    if (line.contains("title=\"последняя\"")) {
                        numpages = Integer.parseInt(SU.sub(line, "page", "/"));
                        return false;
                    }
                    break;
            }
            return true;
        }
    }
}
