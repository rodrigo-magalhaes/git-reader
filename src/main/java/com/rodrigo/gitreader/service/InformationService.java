package com.rodrigo.gitreader.service;

import com.rodrigo.gitreader.model.Information;
import com.rodrigo.gitreader.util.DataBase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class InformationService {

    private Set<String> knownLinks = new HashSet<>();
    private AtomicLong lines = new AtomicLong(0);
    private AtomicLong size = new AtomicLong(0);

    public Information getRepositoryInformation(String repoName) {
        Information information = DataBase.get(repoName);
        if(information != null) {
            return information;
        }
        getInformation("https://github.com/"+repoName);
        information = new Information(lines.get(), size.get());
        DataBase.put(repoName, information);
        return information;
    }

    private void getInformation(String URL) {
        if (!knownLinks.contains(URL)) {
            try {
                knownLinks.add(URL);

                Document document = Jsoup.connect(URL).get();
                Elements links = document.select(".js-details-container.Details")
                        .select("div.js-navigation-item")
                        .select("a:not([rel])")
                        .select("a.js-navigation-open");

                if (links.isEmpty()) {
                    gatherFileInformation(document.select("div.text-mono").text());
                }

                links.parallelStream().forEach(page -> getInformation(page.attr("abs:href")));

            } catch (IOException e) {
                System.err.println("For '" + URL + "': " + e.getMessage());
            }
        }
    }

    private void gatherFileInformation(String information) {
        lines.addAndGet(getLines(information));
        size.addAndGet(getBytes(information).longValue());
    }

    private long getLines(String text) {
        if (!text.contains("line"))
            return 0L;
        String substring = text.substring(0, text.indexOf(" "));
        return Long.parseLong(substring);
    }

    private Float getBytes(String text) {
        int initialIndexOfSize = text.indexOf(")") + 1;
        Float size = Optional.ofNullable(text)
                .map(text2 -> Float.valueOf(text2.substring(initialIndexOfSize, text2.lastIndexOf(" "))))
                .orElse((float) 0);
        if (text.contains("KB"))
            return size * 1024;
        if (text.contains("MB"))
            return size * (1024 * 1024);
        if (text.contains("GB"))
            return size * (1024 * 1024 * 1024);
        return size;
    }

//    public String gatherFileInformation(String repositoryInformation) throws IOException {
//
//        String link = String.format("https://github.com/%s/archive/master.zip", repositoryInformation);
//        URL url  = new URL( link );
//        HttpURLConnection http = (HttpURLConnection)url.openConnection();
//        Map< String, List< String >> header = http.getHeaderFields();
//        while( isRedirected( header )) {
//            link = header.get( "Location" ).get( 0 );
//            url    = new URL( link );
//            http   = (HttpURLConnection)url.openConnection();
//            header = http.getHeaderFields();
//        }
//        InputStream input  = http.getInputStream();
//        byte[]       buffer = new byte[4096];
//        int          n      = -1;
//        OutputStream output = new FileOutputStream( new File( fileName ));
//        while ((n = input.read(buffer)) != -1) {
//            output.write( buffer, 0, n );
//        }
//        output.close();
//        return "";
//    }

//    private boolean isRedirected( Map<String, List<String>> header ) {
//        for( String hv : header.get( null )) {
//            if(   hv.contains( " 301 " )
//                    || hv.contains( " 302 " )) return true;
//        }
//        return false;
//    }

}
