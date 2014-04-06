package com.cab404.libtabun.modules;

import com.cab404.libtabun.data.CommonInfo;
import com.cab404.libtabun.util.SU;
import com.cab404.libtabun.util.U;
import com.cab404.libtabun.util.html_parser.HTMLTree;
import com.cab404.libtabun.util.modular.Module;

/**
 * Окошко юзера сверху страницы.
 *
 * @author cab404
 */
public class CommonInfoModule implements Module<CommonInfo> {

    @Override public CommonInfo extractData(HTMLTree page, String url) {
        CommonInfo info = new CommonInfo();

        try {
            page = page.getTree(page.xPathFirstTag("html/body/div&id=container/header/div&class=dropdown-user"));
            info.isLoggedIn = true;
        } catch (NullPointerException e) {
            info.isLoggedIn = false;
            return info;
        }

        U.v(page);

        info.username = page.xPathStr("a&class=username");
        info.avatar = page.xPathFirstTag("a/img&alt=avatar").get("src");
        info.strength = U.parseFloat(
                SU.removeAllTags(
                        page.xPathStr("ul/li/span&class=strength")
                ).trim()
        );
        info.rating = U.parseFloat(
                SU.removeAllTags(
                        page.xPathStr("ul/li/span&class=rating ") // ПРОБЕЛ ВАЖЕН!
                ).trim()
        );

        return info;
    }


}
