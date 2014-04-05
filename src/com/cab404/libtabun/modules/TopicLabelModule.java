package com.cab404.libtabun.modules;

import com.cab404.libtabun.data.TopicLabel;
import com.cab404.libtabun.util.html_parser.HTMLTree;
import com.cab404.libtabun.util.modular.Module;

/**
 * Парсер заголовков топиков.
 *
 * @author cab404
 */
public class TopicLabelModule implements Module<TopicLabel> {

    private Mode mode;
    public TopicLabelModule(Mode mode) {
        this.mode = mode;
    }

    /**
     * Варианты разделения дерева.
     */
    public static enum Mode {
        /**
         * Использовать для парсирования постов
         */
        POST,
        /**
         * Использовать для парсирования списков
         */
        LIST,
        /**
         * Использовать для парсирования писем
         */
        LETTER
    }

    @Override public TopicLabel extractData(HTMLTree page, String url) {

        switch (mode) {
            case POST:
                page = page.getTree(page.xPathFirstTag("html/body/" +
                        "div&id=container/" +
                        "div&id=wrapper/" +
                        "div&id=content-wrapper/" +
                        "div&id=content/" +
                        "article"));
                break;
            case LETTER:

                break;

        }

        return null;
    }

}
