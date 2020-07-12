package com.rodrigo.gitreader.dto;

import com.rodrigo.gitreader.model.RepoInfo;
import lombok.Data;

@Data
public class RepoInfoDTO {
    private Long lines;
    private Long bytes;

    public RepoInfoDTO(RepoInfo information) {
        this.lines = information.getLines();
        this.bytes = information.getBytes();
    }
}
