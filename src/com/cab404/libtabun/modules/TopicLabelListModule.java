package com.cab404.libtabun.modules;

import com.cab404.libtabun.data.TopicLabel;
import com.cab404.libtabun.util.html_parser.HTMLTree;
import com.cab404.libtabun.util.modular.Module;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cab404
 */
public class TopicLabelListModule implements Module<List<TopicLabel>> {

    @Override public List<TopicLabel> extractData(HTMLTree page, String url) {
        return new ArrayList<>();
    }

}
