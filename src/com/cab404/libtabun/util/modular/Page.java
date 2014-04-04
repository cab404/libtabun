package com.cab404.libtabun.util.modular;

import com.cab404.libtabun.facility.RequestFactory;
import com.cab404.libtabun.util.html_parser.HTMLTree;
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

    @Override public void handleResponse(String response) {
        parse(new HTMLTree(response));
    }

}
