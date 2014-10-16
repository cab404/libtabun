package com.cab404.libph.pages;

import com.cab404.libph.data.StreamItem;
import com.cab404.libph.modules.StreamItemModule;
import com.cab404.moonlight.framework.ModularBlockParser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cab404
 */
public class StreamPage extends TabunPage {

    public List<StreamItem> stream;

    public StreamPage() {
        stream = new ArrayList<>();
    }


    @Override public String getURL() {
        return "/stream/all";
    }

    @Override protected void bindParsers(ModularBlockParser base) {
        super.bindParsers(base);
        base.bind(new StreamItemModule(), BLOCK_STREAM_ITEM);
    }

    @Override public void handle(Object object, int key) {
        switch (key) {
            case BLOCK_STREAM_ITEM:
                stream.add((StreamItem) object);
                break;

            default:
                super.handle(object, key);
        }
    }
}
