package com.ikno.ikdata.dto.batchjson;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BatchJsonClassificationDTO {
    private String classificationName;
    private long docTypeId;
    private String segment;
    private double confidence;
    private String imageFileName;
    private double configConfidence;
}