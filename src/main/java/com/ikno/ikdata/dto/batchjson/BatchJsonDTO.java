package com.ikno.ikdata.dto.batchjson;

import java.util.List;

import lombok.Data;

@Data
public class BatchJsonDTO {
    private String batchId;
    private String batchName;
    private String status;
    private int priority;
    private String projectId;
    private String projectName;
    private String importDate;
    private String modifiedDate;
    private List<BatchJsonDocumentDTO> documents;
    private List<BatchJsonMultiPageDocumentDTO> multiPageDocuments;
    private String exportPath;
    private String validateConfidenceView;
}