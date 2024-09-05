package com.ikno.ikdata.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.ikno.ikdata.dto.ApiResponseDTO;

@ControllerAdvice
public class ControllerExceptionHandler {

    // Exception manager when a param is missing
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponseDTO<Object>> handleMissingParams(MissingServletRequestParameterException ex) {
        String paramName = ex.getParameterName();
        String errorMessage = "The param '" + paramName + "' is required.";

        ApiResponseDTO<Object> response = new ApiResponseDTO<>(false, errorMessage, "MISSING_PARAMETER");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Exception manager when some validation fails
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponseDTO<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiResponseDTO<Object> response = new ApiResponseDTO<>(false, "Validation failed", "VALIDATION_ERROR");
        response.setData(errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Exception manager when a param type is wrong
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponseDTO<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String paramName = ex.getName();
        Object value = ex.getValue();
        Class<?> requiredType = ex.getRequiredType();

        String errorMessage = "From the param '" + paramName + "'' the value '"
                + (value != null ? value.toString() : "null")
                +
                "' cannot be converted to '" +
                (requiredType != null ? requiredType.getSimpleName() : "desconocido") + "'.";

        ApiResponseDTO<Object> response = new ApiResponseDTO<>(false, errorMessage, "TYPE_MISMATCH");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
