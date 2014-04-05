package com.cab404.libtabun.util.loaders;

import com.cab404.libtabun.facility.RequestFactory;
import com.cab404.libtabun.facility.ResponseFactory;
import com.cab404.libtabun.util.html_parser.HTMLTree;
import com.cab404.libtabun.util.html_parser.ParallelHTMLParser;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * Represents web->raw-data step, and wrapper for raw-data->data.
 *
 * @author cab404
 */
public abstract class Page extends Request {

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

    @Override public void finished(ResponseFactory.Parser parser) {
        parse(
                new HTMLTree(
                        ((ParallelHTMLParser) parser).getParser()
                )
        );
    }

    @Override public ResponseFactory.Parser getParser() {
        return new ParallelHTMLParser();
    }


}
