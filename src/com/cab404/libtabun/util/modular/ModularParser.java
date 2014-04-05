package com.cab404.libtabun.util.modular;

import com.cab404.libtabun.facility.ResponseFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author cab404
 */
public class ModularParser implements ResponseFactory.Parser {
    List<HandledParser> parsers;
    int current = 0;

    public ModularParser(){
        this.parsers = new ArrayList<>();
    }


    public ModularParser(HandledParser... parsers) {
        this.parsers = new ArrayList<>(Arrays.asList(parsers));
    }

    @Override public boolean line(String line) {
        if (current < parsers.size()) {

            HandledParser current = parsers.get(this.current);

            if (current.line(line)) {
                current.onFinish();
                this.current++;
            }

            return true;
        }

        return false;
    }
    @Override public void finished() {

    }
}
