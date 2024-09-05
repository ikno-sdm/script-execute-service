package com.ikno.ikdata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDTO<T> {
    private boolean success;
    private T data;
    private String message;

    public ApiResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
