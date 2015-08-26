package com.cab404.libtabun.pages;

import com.cab404.libtabun.data.Comment;
import com.cab404.libtabun.data.Letter;
import com.cab404.libtabun.modules.CommentModule;
import com.cab404.libtabun.modules.LetterModule;
import com.cab404.moonlight.framework.ModularBlockParser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cab404
 */
public class LetterPage extends TabunPage {
    public Letter header;
    public List<Comment> comments;
    private final int id;

    public LetterPage(int id) {
        this.id = id;
        comments = new ArrayList<>();
    }

    @Override
    public String getURL() {
        return "/talk/read/" + id;
    }

    @Override
    protected void bindParsers(ModularBlockParser base) {
        super.bindParsers(base);
        base.bind(new LetterModule(), BLOCK_LETTER_HEADER);
        base.bind(new CommentModule(CommentModule.Mode.LETTER), BLOCK_COMMENT);
    }

    @Override
    public void handle(Object object, int key) {
        switch (key) {
            case BLOCK_LETTER_HEADER:
                header = (Letter) object;
                break;
            case BLOCK_COMMENT:
                comments.add((Comment) object);
                break;
            default:
                super.handle(object, key);
        }

    }

}
