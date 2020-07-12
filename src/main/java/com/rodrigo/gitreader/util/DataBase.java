package com.rodrigo.gitreader.util;

import com.rodrigo.gitreader.model.Information;

import java.util.HashMap;
import java.util.Map;

public class DataBase {

    private static final Map<String, Information> DATA = new HashMap<>();

    public static final Information get(String text) {
        return DATA.get(text);
    }

    public static final void put(String text, Information information) {
        DATA.put(text, information);
    }
}
