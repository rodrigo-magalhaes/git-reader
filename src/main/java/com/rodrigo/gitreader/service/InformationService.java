package com.rodrigo.gitreader.service;

import com.rodrigo.gitreader.model.HtmlInfo;
import com.rodrigo.gitreader.model.RepoInfo;
import com.rodrigo.gitreader.util.DataBase;
import com.rodrigo.gitreader.util.HtmlReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static com.rodrigo.gitreader.util.Constants.GIT_URL;

@Slf4j
@Service
public class InformationService {

    private HtmlReader htmlReader;
    private AtomicLong lines = new AtomicLong(0);
    private AtomicLong size = new AtomicLong(0);

    public InformationService(HtmlReader htmlReader) {
        this.htmlReader = htmlReader;
    }

    public RepoInfo getRepositoryInformation(String repoName) {
        RepoInfo information = DataBase.get(repoName);
        if (information != null) {
            return information;
        }
        getInformation(GIT_URL + repoName);
        return saveRepoInfo(repoName);
    }

    private RepoInfo saveRepoInfo(String repoName) {
        RepoInfo repoInfo = new RepoInfo(repoName, lines.get(), size.get());
        DataBase.put(repoInfo.getRepoName(), repoInfo);
        return repoInfo;
    }

    private void getInformation(String pageUrl) {
        try {
            HtmlInfo htmlInfo = htmlReader.getInformation(pageUrl);
            if (htmlInfo.getLinks().isEmpty()) {
                gatherFileInformation(htmlInfo);
            }
            htmlInfo.getLinks().parallelStream().forEach(this::getInformation);
        } catch (IOException e) {
            log.error("Error when processing " + pageUrl + ": " + e.getMessage());
        }
    }

    private void gatherFileInformation(HtmlInfo details) {
        lines.addAndGet(getLines(details.getLines()));
        size.addAndGet(getBytes(details.getBytes()).longValue());
    }

    private long getLines(String text) {
        if (text == null)
            return 0L;
        String substring = text.substring(0, text.indexOf(' '));
        return Long.parseLong(substring);
    }

    private Float getBytes(String text) {
        if (text == null)
            return (float) 0;
        Float fileSize = Optional.ofNullable(text)
                .map(text2 -> Float.valueOf(text.substring(0, text.indexOf(' '))))
                .orElse((float) 0);
        if (text.contains("KB"))
            return fileSize * 1024;
        if (text.contains("MB"))
            return fileSize * (1024 * 1024);
        if (text.contains("GB"))
            return fileSize * (1024 * 1024 * 1024);
        return fileSize;
    }
}
