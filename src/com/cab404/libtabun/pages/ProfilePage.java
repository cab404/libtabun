package com.cab404.libtabun.pages;

import com.cab404.libtabun.data.Profile;
import com.cab404.libtabun.modules.ProfileModule;
import com.cab404.libtabun.util.SU;
import com.cab404.libtabun.util.modular.ModularBlockParser;

/**
 * @author cab404
 */
public class ProfilePage extends TabunPage {
    public final String username;
    public Profile user_info;
    public static final int USER_INFO_BLOCK = 281;

    public ProfilePage(String username) {
        this.username = username;
    }

    @Override public String getURL() {
        return "/profile/" + SU.rl(username);
    }

    @Override protected void bindParsers(ModularBlockParser base) {
        super.bindParsers(base);
        base.bind(new ProfileModule(), USER_INFO_BLOCK);
    }

    @Override public void handle(Object object, int key) {
        super.handle(object, key);
        switch (key) {
            case USER_INFO_BLOCK:
                user_info = (Profile) object;
                break;
        }
    }
}
