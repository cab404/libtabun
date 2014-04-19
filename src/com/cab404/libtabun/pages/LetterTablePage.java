package com.cab404.libtabun.pages;

import com.cab404.libtabun.data.Letter;
import com.cab404.libtabun.modules.LetterTableModule;
import com.cab404.moonlight.framework.ModularBlockParser;

import java.util.List;

/**
 * @author cab404
 */
public class LetterTablePage extends TabunPage {
    int page;
    public List<Letter> letters;

    public LetterTablePage(int page) {
        this.page = page;
    }

    public LetterTablePage() {
        this.page = 1;
    }

    @Override public String getURL() {
        return "/talk/inbox/page" + page;
    }

    @Override protected void bindParsers(ModularBlockParser base) {
        super.bindParsers(base);
        base.bind(new LetterTableModule(), BLOCK_LETTER_TABLE);
    }

    @Override public void handle(Object object, int key) {
        super.handle(object, key);
        switch (key) {
            case BLOCK_LETTER_TABLE:
                //noinspection unchecked
                letters = (List<Letter>) object;
                break;
        }
    }

}
