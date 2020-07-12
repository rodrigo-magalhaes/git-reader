package com.rodrigo.gitreader.util;

import com.rodrigo.gitreader.model.HtmlInfo;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

import static com.rodrigo.gitreader.util.Constants.GIT_URL;

@Service
public class HtmlReader {

    public HtmlInfo getInformation(String pageUrl) throws IOException {
        HtmlInfo htmlInfo = new HtmlInfo();
        HtmlDownload.getHTML(pageUrl).forEach(line -> {
            getLinks(line, htmlInfo);
            getLines(line, htmlInfo);
            getBytes(line, htmlInfo);
        });
        return htmlInfo;
    }

    private void getLinks(String line, HtmlInfo htmlInfo) {
        Optional.of(line)
                .filter(ln -> !ln.isEmpty())
                .filter(ln -> ln.contains("<a class=\"js-navigation-open"))
                .filter(ln -> ln.contains("title="))
                .map(ln -> {
                    final int indexRef = ln.indexOf("href=") + 7;
                    return GIT_URL + ln.substring(indexRef, line.indexOf('\"', indexRef));
                })
                .ifPresent(ln -> htmlInfo.getLinks().add(ln));
    }

    private void getLines(String line, HtmlInfo htmlInfo) {
        Optional.of(line)
                .filter(ln -> !ln.isEmpty())
                .filter(ln -> ln.contains(" lines") && ln.contains("("))
                .ifPresent(ln -> htmlInfo.setLines(ln.trim()));
    }

    private void getBytes(String line, HtmlInfo htmlInfo) {
        Optional.of(line)
                .filter(ln -> !ln.isEmpty())
                .filter(ln -> ln.contains(" Bytes") || ln.contains(" KB") || ln.contains(" MB") || ln.contains(" GB"))
                .ifPresent(ln -> htmlInfo.setBytes(ln.trim()));
    }
}
