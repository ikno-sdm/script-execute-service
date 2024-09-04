package com.ikno.ikdata.dto.batchjson;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BatchJsonDataFieldDTO {
    private long fieldId;
    private String label;
    private String key;
    private double kFuzziness;
    private double kX0;
    private double kY0;
    private double kX1;
    private double kY1;
    private String value;
    private double vConfidence;
    private double vX0;
    private double vY0;
    private double vX1;
    private double vY1;
    private boolean required;
    private boolean multiLine;
    private int kRotation;
    private int extractionPriority;
    private String replacementList;
    private String confirmationValue;
    private String fieldMessage;
    private String validateRegExp;
    private boolean hidden;
    private String docunetIndex;

    private double limiter_x0;
    private double limiter_y0;
    private double limiter_x1;
    private double limiter_y1;
    private String limiter;

    private double top_limiter_x0;
    private double top_limiter_y0;
    private double top_limiter_x1;
    private double top_limiter_y1;
    private String top_limiter;

    private double configConfidence;
    private boolean markField;
    private String extractionUsed;
    private boolean skipReport;
    private boolean skipExport;
    private boolean doubleExtraction;
}