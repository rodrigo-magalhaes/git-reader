package com.rodrigo.gitreader.util;

import com.rodrigo.gitreader.model.RepoInfo;

import java.util.HashMap;
import java.util.Map;

public abstract class DataBase {

    private DataBase() {
    }

    private static final Map<String, RepoInfo> DATA = new HashMap<>();

    public static final RepoInfo get(String text) {
        return DATA.get(text);
    }

    public static final void put(String text, RepoInfo information) {
        DATA.put(text, information);
    }
}
