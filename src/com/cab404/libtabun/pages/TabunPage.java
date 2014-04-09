package com.cab404.libtabun.pages;

import com.cab404.libtabun.data.CommonInfo;
import com.cab404.libtabun.facility.ResponseFactory;
import com.cab404.libtabun.modules.CommonInfoModule;
import com.cab404.libtabun.modules.LSKeyModule;
import com.cab404.libtabun.parts.LivestreetKey;
import com.cab404.libtabun.util.loaders.Page;
import com.cab404.libtabun.util.modular.AccessProfile;
import com.cab404.libtabun.util.modular.ModularBlockParser;

/**
 * @author cab404
 */
public class TabunPage extends Page {
    /**
     * Информация о текущем пользователе.
     */
    public CommonInfo c_inf;
    public LivestreetKey key;
    public static final int COMMON_INFO_BLOCK = 279, LS_KEY_BLOCK = 280;

    @Override
    public String getURL() {
        return "/404";
    }

    @Override protected void bindParsers(ModularBlockParser base) {
        base.bind(new LSKeyModule(), LS_KEY_BLOCK);
        base.bind(new CommonInfoModule(), COMMON_INFO_BLOCK);
    }

    @Override public void fetch(AccessProfile accessProfile) {
        super.fetch(accessProfile, new ResponseFactory.StatusListener() {
            @Override public void onResponseFail(Throwable t) {
                if (t instanceof ErrorResponse)
                    if (((ErrorResponse) t).getStatusLine().getStatusCode() != 404)
                        throw new RuntimeException(t);
            }
        });
    }

    @Override public void handle(Object object, int key) {
        switch (key) {
            case COMMON_INFO_BLOCK:
                this.c_inf = (CommonInfo) object;
                break;
            case LS_KEY_BLOCK:
                this.key = (LivestreetKey) object;
                break;
        }
    }
}
