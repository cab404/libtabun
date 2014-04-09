package com.cab404.libtabun.pages;

import com.cab404.libtabun.data.TopicLabel;
import com.cab404.libtabun.modules.CommentModule;
import com.cab404.libtabun.parts.Comment;
import com.cab404.libtabun.util.U;
import com.cab404.libtabun.util.modular.ModularBlockParser;

/**
 * @author cab404
 */
public class TopicPage extends TabunPage {

    private int id;

    TopicLabel header;

    public TopicPage(int id) {
        this.id = id;
    }

    @Override public String getURL() {
        return "/blog/" + id + ".html";
    }

    @Override protected void bindParsers(ModularBlockParser base) {
        super.bindParsers(base);
        base.bind(new CommentModule(), 12);
    }

    //    @Override protected void parse(HTMLTree page, AccessProfile profile) {
//        try {
//            FileWriter writer = new FileWriter("post");
//            writer.write(page.toString());
//            writer.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
    @Override public void handle(Object object, int key) {
        super.handle(object, key);
        if (key == 12) U.v(((Comment) object).author);
    }
}
