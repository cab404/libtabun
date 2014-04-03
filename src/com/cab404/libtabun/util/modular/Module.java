package com.cab404.libtabun.util.modular;

import com.cab404.libtabun.util.html_parser.HTMLTree;

/**
 * This will replace parsers. And will slow everything down, yay :D
 * Represents raw-data->data step.
 *
 * @author cab404
 */
public interface Module<T> {
    public abstract T extractData(HTMLTree parser, String url);
}
