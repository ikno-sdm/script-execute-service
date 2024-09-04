package com.ikno.ikdata.dto;

import java.util.List;

import lombok.Data;

@Data
public class BatchJsonSinglePageDTO {
    private String imageFileName;
    private int pageID;
    private double originalImageWidth;
    private double originalImageHeight;
    private List<BatchJsonClassificationDTO> searchClassification;
}