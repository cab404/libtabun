package com.cab404.libtabun.pages;

import com.cab404.libtabun.modules.QuoteModule;
import com.cab404.libtabun.util.modular.ModularBlockParser;

/**
 * @author cab404
 */
public class MainPage extends TabunPage {
    public String quote;
    public static final int QUOTE_BLOCK = 278;

    @Override public String getURL() {
        return "/";
    }

    @Override protected void bindParsers(ModularBlockParser base) {
        super.bindParsers(base);
        base.bind(new QuoteModule(), QUOTE_BLOCK);
    }

    @Override public void handle(Object object, int key) {
        super.handle(object, key);
        switch (key) {
            case QUOTE_BLOCK:
                this.quote = (String) object;
                break;
        }
    }
}
