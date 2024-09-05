package com.ikno.ikdata.dto.batchjson;

import java.util.List;

import lombok.Data;

@Data
public class BatchJsonTableRowDTO {
    private List<BatchJsonDataFieldDTO> columnFields;
}
