package com.rodrigo.gitreader.dto;

import com.rodrigo.gitreader.model.Information;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class InformationDTO {
    private Long lines;
    private Long bytes;

    public InformationDTO(Information information) {
        this.lines = information.getLines();
        this.bytes = information.getBytes();
    }
}
