package com.rodrigo.gitreader.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HtmlDownload {

    private HtmlDownload() {
    }

    public static List<String> getHTML(String pageUrl) throws IOException {
        List<String> htmlLines = new ArrayList<>();
        try (InputStream is = new URL(pageUrl).openStream(); BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            br.lines().forEach(htmlLines::add);
        }
        return htmlLines;
    }

}
