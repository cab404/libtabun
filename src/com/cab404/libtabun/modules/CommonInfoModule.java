package com.cab404.libtabun.modules;

import com.cab404.libtabun.data.CommonInfo;
import com.cab404.libtabun.parts.LivestreetKey;
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

    @Override public CommonInfo extractData(HTMLTree tree, String url) {
        CommonInfo info = new CommonInfo();

        try {
            tree = tree.getTree(tree.xPathFirstTag("html/body/div&id=container/header/div&class=dropdown-user"));
            info.isLoggedIn = true;
        } catch (NullPointerException e) {
            info.isLoggedIn = false;
            U.w(e);
            return info;
        }


        info.username = tree.xPathStr("a&class=username");
        info.avatar = tree.xPathFirstTag("a/img&alt=avatar").get("src");
        info.strength = U.parseFloat(
                SU.removeAllTags(
                        tree.xPathStr("ul/li/span&class=strength")
                ).trim()
        );
        info.rating = U.parseFloat(
                SU.removeAllTags(
                        tree.xPathStr("ul/li/span&class=rating ") // ПРОБЕЛ ВАЖЕН!
                ).trim()
        );

        info.key = new LivestreetKey(url,
                SU.bsub(tree.xPathFirstTag("ul/li&class=item-signout/a").get("href"), "", "=")
        );

        return info;
    }


}
