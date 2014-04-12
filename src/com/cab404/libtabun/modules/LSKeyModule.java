package com.cab404.libtabun.modules;

import com.cab404.libtabun.data.LivestreetKey;
import com.cab404.moonlight.util.SU;
import com.cab404.moonlight.parser.HTMLTree;
import com.cab404.moonlight.parser.Tag;
import com.cab404.moonlight.framework.AccessProfile;

/**
 * @author cab404
 */
public class LSKeyModule extends ModuleImpl<LivestreetKey> {

    @Override public LivestreetKey extractData(HTMLTree page, AccessProfile profile) {
        String js = page.getContents(page.get(0));
        if (!js.contains("LIVESTREET_SECURITY_KEY")) return null;

        LivestreetKey key = new LivestreetKey(profile.getHost().getHostName(), SU.sub(
                js,
                "LIVESTREET_SECURITY_KEY = '",
                "'"
        ));
        finish();
        return key;
    }

    @Override public boolean doYouLikeIt(Tag tag) {
        return "script".equals(tag.name);
    }

}
