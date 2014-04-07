package com.cab404.libtabun.util.loaders;

import com.cab404.libtabun.facility.RequestFactory;
import com.cab404.libtabun.facility.ResponseFactory;
import com.cab404.libtabun.util.html_parser.*;
import com.cab404.libtabun.util.modular.AccessProfile;
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

    @Override public HttpRequestBase getRequest(AccessProfile accessProfile) {
        return RequestFactory.get(getURL(), accessProfile).build();
    }
    /**
     * Занимается данными.
     */
    protected abstract void parse(HTMLTree page, AccessProfile profile);

    @Override public void response(ResponseFactory.Parser parser, AccessProfile profile) {
        TagParser tag_parser = null;
        while (tag_parser == null)
            tag_parser = ((HTMLTagParserThread) parser).getTagParser();
        LevelAnalyzer level_analyzer = null;
        while (level_analyzer == null) level_analyzer = content.getLevelAnalyzer();

        parse(new HTMLTree(level_analyzer, tag_parser), profile);
    }

    @Override public ResponseFactory.Parser getParser() {
        content = new HTMLAnalyzerThread();
        HTMLTagParserThread parser = new HTMLTagParserThread(content);
        content.start();
        parser.start();

        content.setName(getURL() + " analyzer thread.");
        parser.setName(getURL() + " parser thread.");

        parser.bondWithAnalyzer(content);

        return parser;
    }

    @Override public void fetch(AccessProfile accessProfile, ResponseFactory.StatusListener statusListener) {
        super.fetch(accessProfile, statusListener);
    }
}
