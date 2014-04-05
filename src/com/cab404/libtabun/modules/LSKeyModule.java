package com.cab404.libtabun.modules;

import com.cab404.libtabun.parts.LivestreetKey;
import com.cab404.libtabun.util.SU;
import com.cab404.libtabun.util.html_parser.HTMLTree;
import com.cab404.libtabun.util.modular.Module;

/**
 * @author cab404
 */
public class LSKeyModule implements Module<LivestreetKey> {


    @Override public LivestreetKey extractData(HTMLTree page, String url) {
        return new LivestreetKey(url, SU.sub(
                page.getContents(page.xPath("html/head/script").get(1)),
                "LIVESTREET_SECURITY_KEY = '",
                "'"
        )
        );
    }


}
