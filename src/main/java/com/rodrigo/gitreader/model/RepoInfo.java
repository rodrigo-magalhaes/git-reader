package com.rodrigo.gitreader.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RepoInfo {
    private String repoName;
    private Long lines;
    private Long bytes;
}
