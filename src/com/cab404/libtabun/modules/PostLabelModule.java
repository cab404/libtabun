package com.cab404.libtabun.modules;

import com.cab404.libtabun.data.PostLabel;
import com.cab404.libtabun.util.html_parser.HTMLTree;
import com.cab404.libtabun.util.modular.Module;

/**
 * Парсер заголовков топиков.
 *
 * @author cab404
 */
public class PostLabelModule implements Module<PostLabel> {

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

    @Override public PostLabel extractData(HTMLTree page, String url) {
        return null;
    }

}
