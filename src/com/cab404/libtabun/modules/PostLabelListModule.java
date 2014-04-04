package com.cab404.libtabun.modules;

import com.cab404.libtabun.data.PostLabel;
import com.cab404.libtabun.util.html_parser.HTMLTree;
import com.cab404.libtabun.util.modular.Module;

import java.util.List;

/**
 * @author cab404
 */
public class PostLabelListModule implements Module<List<PostLabel>> {

    @Override public List<PostLabel> extractData(HTMLTree parser, String url) {
        return null;
    }

}
