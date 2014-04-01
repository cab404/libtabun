package com.cab404.libtabun.facility.html_parser;

import java.util.HashMap;
import java.util.Map;

/**
* @author cab404
*/
public class Tag {
    public int index;
    public int start, end;
    public String name, text;
    public boolean isClosing;    // Тег типа </x>
    public boolean isStandalone; // Тег типа <x/>
    public boolean isComment; // Тег типа <!-- x --> и <! x>
    public Map<String, String> props;

    public Tag() {
        props = new HashMap<>();
    }

    @Override public String toString() {
        return new StringBuilder()
                .append("== TAG ==").append("\n")
                .append("Code: '").append(text).append("' \n")
                .append("Name: '").append(name).append("' \n")
                .append("StA: ").append(isStandalone).append(" \n")
                .append("Cl: ").append(isClosing).append(" \n")
                .append("Cm: ").append(isComment).append(" \n")
                .toString();
    }
}
