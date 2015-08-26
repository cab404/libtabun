package com.cab404.libtabun.pages;

import com.cab404.libtabun.modules.QuoteModule;
import com.cab404.moonlight.framework.ModularBlockParser;

/**
 * @author cab404
 */
public class MainPage extends TabunPage {
    public String quote;

    @Override
    public String getURL() {
        return "/";
    }

    @Override
    protected void bindParsers(ModularBlockParser base) {
        super.bindParsers(base);
        base.bind(new QuoteModule(), BLOCK_QUOTE);
    }

    @Override
    public void handle(Object object, int key) {
        switch (key) {
            case BLOCK_QUOTE:
                this.quote = (String) object;
                break;
            default:
                super.handle(object, key);
        }
    }
}
