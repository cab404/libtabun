package com.cab404.libtabun.pages;

import com.cab404.libtabun.data.CommonInfo;
import com.cab404.libtabun.facility.ResponseFactory;
import com.cab404.libtabun.modules.CommonInfoModule;
import com.cab404.libtabun.modules.LSKeyModule;
import com.cab404.libtabun.parts.LivestreetKey;
import com.cab404.libtabun.util.html_parser.HTMLTree;
import com.cab404.libtabun.util.loaders.Page;
import com.cab404.libtabun.util.modular.Cookies;

/**
 * @author cab404
 */
public class TabunPage extends Page {
    /**
     * Информация о текущем пользователе.
     */
    public CommonInfo c_inf;
    public LivestreetKey key;

    @Override
    public String getURL() {
        return "/404";
    }

    @Override
    protected void parse(HTMLTree page) {
        key = new LSKeyModule().extractData(page, getURL());
        c_inf = new CommonInfoModule().extractData(page, getURL());
    }

    @Override public void fetch(Cookies cookies) {
        super.fetch(cookies, new ResponseFactory.StatusListener() {
            @Override public void onResponseFail(Throwable t) {
                if (t instanceof ErrorResponse)
                    if (((ErrorResponse) t).getStatusLine().getStatusCode() != 404)
                        throw new RuntimeException(t);
            }
        });
    }
}
