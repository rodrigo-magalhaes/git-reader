package com.rodrigo.gitreader.model;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class HtmlInfo {
    private Set<String> links;
    private String lines;
    private String bytes;

    public HtmlInfo() {
        links = new HashSet<>();
    }
}
