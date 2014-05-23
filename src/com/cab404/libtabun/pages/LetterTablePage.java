package com.cab404.libtabun.pages;

import com.cab404.libtabun.data.LetterLabel;
import com.cab404.libtabun.modules.LetterLabelModule;
import com.cab404.moonlight.framework.ModularBlockParser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cab404
 */
public class LetterTablePage extends TabunPage {
    int page;
    public List<LetterLabel> letters;

    public LetterTablePage(int page) {
        this();
        this.page = page;
    }

    public LetterTablePage() {
        this.page = 1;
        letters = new ArrayList<>();
    }

    @Override public String getURL() {
        return "/talk/inbox/page" + page;
    }

    @Override protected void bindParsers(ModularBlockParser base) {
        super.bindParsers(base);
        base.bind(new LetterLabelModule(), BLOCK_LETTER_LABEL);
    }

    @Override public void handle(Object object, int key) {
        switch (key) {
            case BLOCK_LETTER_LABEL:
                //noinspection unchecked
                letters.add((LetterLabel) object);
                break;
            default:
                super.handle(object, key);
        }
    }

}
