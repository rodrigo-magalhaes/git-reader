package com.rodrigo.gitreader.util;

import com.rodrigo.gitreader.model.HtmlInfo;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
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
        commonFilter(line)
                .filter(ln -> ln.contains(" lines") && ln.contains("("))
                .ifPresent(ln -> htmlInfo.setLines(ln.trim()));
    }

    private void getBytes(String line, HtmlInfo htmlInfo) {
        commonFilter(line)
                .filter(ln -> ln.contains(" Byte") || ln.contains(" KB") || ln.contains(" MB") || ln.contains(" GB"))
                .map(this::getBytesInformation)
                .ifPresent(ln -> htmlInfo.setBytes(ln.trim()));
    }

    private Optional<String> commonFilter(String line) {
        return Optional.of(line)
                .filter(ln -> !ln.isEmpty())
                .filter(ln -> !ln.contains("class=\"pl-"))
                .filter(ln -> !ln.contains("class=pl-"));
    }

    private String getBytesInformation(String ln) {
        int indexType = Arrays.asList(" Byte", " KB", " MB", " GB").stream()
                .mapToInt(ln::lastIndexOf)
                .filter(index -> index > 0)
                .findFirst()
                .getAsInt();
        int indexNumber = ln.substring(0, indexType).lastIndexOf('>') + 1;
        String blurryInformation = ln.substring(indexNumber).trim();
        int index = 0;
        for (; index < blurryInformation.length(); index++) {
            if (Character.isDigit(blurryInformation.charAt(index)))
                break;
        }
        return blurryInformation.substring(index);
    }
}
