package com.cab404.libtabun.pages;

import com.cab404.libtabun.data.CommonInfo;
import com.cab404.libtabun.data.LivestreetKey;
import com.cab404.libtabun.modules.CommonInfoModule;
import com.cab404.libtabun.modules.LSKeyModule;
import com.cab404.moonlight.facility.ResponseFactory;
import com.cab404.moonlight.framework.AccessProfile;
import com.cab404.moonlight.framework.ModularBlockParser;
import com.cab404.moonlight.framework.Page;

/**
 * @author cab404
 */
public class TabunPage extends Page {
    /**
     * Информация о текущем пользователе.
     */
    public CommonInfo c_inf;
    public LivestreetKey key;

    public static final int
            BLOCK_QUOTE = 279,
            BLOCK_COMMON_INFO = 280,
            BLOCK_LS_KEY = 281,
            BLOCK_COMMENT = 282,
            BLOCK_TOPIC_HEADER = 283,
            BLOCK_USER_INFO = 284,
            BLOCK_COMMENT_NUM = 285,
            BLOCK_BLOG_INFO = 286,
            BLOCK_STREAM_ITEM = 287,
            BLOCK_LETTER_HEADER = 288,
            BLOCK_LETTER_TABLE = 289
    ;


    @Override
    public String getURL() {
        return "/404";
    }

    @Override protected void bindParsers(ModularBlockParser base) {
        base.bind(new LSKeyModule(), BLOCK_LS_KEY);
        base.bind(new CommonInfoModule(), BLOCK_COMMON_INFO);
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
            case BLOCK_COMMON_INFO:
                this.c_inf = (CommonInfo) object;
                break;
            case BLOCK_LS_KEY:
                this.key = (LivestreetKey) object;
                break;
        }
    }
}
