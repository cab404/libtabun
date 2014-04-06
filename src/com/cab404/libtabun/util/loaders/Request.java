package com.cab404.libtabun.util.loaders;

import com.cab404.libtabun.facility.ResponseFactory;
import com.cab404.libtabun.util.RU;
import com.cab404.libtabun.util.U;
import com.cab404.libtabun.util.modular.Cookies;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * @author cab404
 */
public abstract class Request {


    public abstract HttpRequestBase getRequest();

    public abstract void response(ResponseFactory.Parser parser);
    public abstract ResponseFactory.Parser getParser();


    public void fetch(Cookies cookies, ResponseFactory.StatusListener statusListener) {

        HttpRequestBase request = getRequest();

        statusListener.onResponseStart();
        HttpResponse response;
        try {
            response = RU.exec(request, cookies);
        } catch (Throwable e) {
            U.w("Page: Response fail");
            U.w(e);
            statusListener.onResponseFail(e);
            return;
        }
        U.v(response.getStatusLine());
        statusListener.onResponseFinished();

        statusListener.onLoadingStarted();
        ResponseFactory.Parser parser = getParser();
        try {
            ResponseFactory.read(response, parser, statusListener);
        } catch (Throwable e) {
            U.w("Page: Loading fail");
            U.w(e);
            statusListener.onLoadingFail(e);
        }
        statusListener.onLoadingFinished();

        statusListener.onParseStarted();
        try {
            response(parser);
        } catch (Throwable e) {
            U.w("Page: Parsing fail");
            U.w(e);
            statusListener.onParseFail(e);
            return;
        }
        statusListener.onParseFinished();

        statusListener.onFinish();

    }

    /**
     * Downloads and parses page.
     */
    public void fetch(Cookies cookies) {
        fetch(cookies, new ResponseFactory.StatusListener() { });
    }
}
