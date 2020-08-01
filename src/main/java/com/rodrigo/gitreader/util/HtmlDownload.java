package com.rodrigo.gitreader.util;

import com.rodrigo.gitreader.exception.TooManyRequestsException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HtmlDownload {

    private HtmlDownload() {
    }

    public static List<String> getHTML(String pageUrl) throws IOException {

        List<String> htmlLines = new ArrayList<>();
        HttpURLConnection connection = getHttpURLConnection(pageUrl);

        try (InputStream is = connection.getInputStream(); BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            br.lines().forEach(htmlLines::add);
        }

        connection.disconnect();

        return htmlLines;
    }

    private static HttpURLConnection getHttpURLConnection(String pageUrl) throws IOException {

        URL url = new URL(pageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() == 429) {
            Logger.getLogger("HtmlDownload").log(Level.WARNING, "connection refused: {0}", pageUrl);
            throw new TooManyRequestsException(pageUrl);
        }

        return connection;
    }

}
