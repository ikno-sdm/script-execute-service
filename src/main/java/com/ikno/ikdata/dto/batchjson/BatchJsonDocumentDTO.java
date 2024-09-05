package com.ikno.ikdata.dto.batchjson;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BatchJsonDocumentDTO {
    private int docId;
    private String docType;
    private long docTypeId;
    private double confidence;
    private List<BatchJsonSinglePageDTO> pages;
    private List<BatchJsonDataFieldDTO> dataFields;
    private BatchJsonTableDTO table;
    private boolean needsReview;
    private String validationMessage;
    private String businessRulesMessage;
    private double configConfidence;
    private boolean filledMarkField;
    private String colorMarkField;
    private String docunetSerie;
    private String docunetSubSerie;

}