package com.rodrigo.gitreader.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Information {
    private Long lines;
    private Long bytes;
}
