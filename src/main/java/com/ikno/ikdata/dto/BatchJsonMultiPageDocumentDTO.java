package com.ikno.ikdata.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BatchJsonMultiPageDocumentDTO {
    private String id;
    private String multiPageDocType;
    private List<BatchJsonDocumentDTO> documents;
}
