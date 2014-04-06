package com.cab404.libtabun.util.loaders;

import com.cab404.libtabun.facility.RequestFactory;
import com.cab404.libtabun.facility.ResponseFactory;
import com.cab404.libtabun.util.html_parser.*;
import com.cab404.libtabun.util.modular.Cookies;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * Represents web->raw-data step, and wrapper for raw-data->data.
 *
 * @author cab404
 */
public abstract class Page extends Request {
    private HTMLAnalyzerThread content;

    /**
     * Возвращает url страницы.
     */
    public abstract String getURL();

    @Override public HttpRequestBase getRequest() {
        return RequestFactory.get(getURL()).build();
    }
    /**
     * Занимается данными.
     */
    protected abstract void parse(HTMLTree page);

    @Override public void response(ResponseFactory.Parser parser) {
        TagParser tag_parser = ((HTMLTagParserThread) parser).getTagParser();
        LevelAnalyzer level_analyzer = content.getLevelAnalyzer();

        parse(new HTMLTree(level_analyzer, tag_parser));
    }

    @Override public ResponseFactory.Parser getParser() {
        content = new HTMLAnalyzerThread();
        HTMLTagParserThread parser = new HTMLTagParserThread(content);
        parser.bondWithAnalyzer(content);
        return parser;
    }

    @Override public void fetch(Cookies cookies, ResponseFactory.StatusListener statusListener) {
        super.fetch(cookies, statusListener);

    }
}
