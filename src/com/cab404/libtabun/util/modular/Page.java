package com.cab404.libtabun.util.modular;

import com.cab404.libtabun.facility.RequestFactory;
import com.cab404.libtabun.facility.ResponseFactory;
import com.cab404.libtabun.util.RU;
import com.cab404.libtabun.util.html_parser.HTMLTree;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * Represents web->raw-data step, and wrapper for raw-data->data.
 *
 * @author cab404
 */
public abstract class Page {

    /**
     * Возвращает url страницы.
     */
    public abstract String getURL();

    /**
     * Занимается данными.
     */
    protected abstract void parse(HTMLTree page);

    /**
     * Загружает страницу.
     */
    public void fetch(Cookies cookies, ResponseFactory.StatusListener statusListener) {

        HttpRequestBase request = RequestFactory.get(getURL()).build();

        statusListener.onResponseStart();
        HttpResponse response = RU.exec(request, cookies);
        if (response == null) statusListener.onFail();
        statusListener.onResponseFinished();

        statusListener.onLoadingStarted();
        PageParser parser = new PageParser();
        ResponseFactory.read(response, parser, statusListener);
        statusListener.onLoadingFinished();

        statusListener.onParseStarted();
        parse(parser.getPage());
        statusListener.onParseFinished();

        statusListener.onFinish();

    }

    public void fetch(Cookies cookies) {
        fetch(cookies, new ResponseFactory.StatusListener() {
            @Override public void onResponseStart() {}
            @Override public void onResponseFinished() {}
            @Override public void onLoadingStarted() {}
            @Override public void onProgressChange(float progress) {}
            @Override public void onLoadingFinished() {}
            @Override public void onParseStarted() {}
            @Override public void onParseFinished() {}
            @Override public void onFail() {}
            @Override public void onFinish() {}
        });
    }
}
