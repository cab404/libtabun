package com.cab404.libtabun.pages;

import com.cab404.libtabun.data.Profile;
import com.cab404.libtabun.modules.ProfileModule;
import com.cab404.moonlight.framework.ModularBlockParser;
import com.cab404.moonlight.util.SU;

/**
 * @author cab404
 */
public class ProfilePage extends TabunPage {
    public final String username;
    public Profile user_info;

    public ProfilePage(String username) {
        this.username = username;
    }

    @Override
    public String getURL() {
        return "/profile/" + SU.rl(username);
    }

    @Override
    protected void bindParsers(ModularBlockParser base) {
        super.bindParsers(base);
        base.bind(new ProfileModule(), BLOCK_USER_INFO);
    }

    @Override
    public void handle(Object object, int key) {
        switch (key) {
            case BLOCK_USER_INFO:
                user_info = (Profile) object;
                break;
            default:
                super.handle(object, key);
        }
    }
}
