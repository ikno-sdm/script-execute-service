package com.ikno.ikdata.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ikno.ikdata.common.Enums;
import com.ikno.ikdata.dto.ApiResponseDTO;
import com.ikno.ikdata.dto.batchjson.BatchJsonDTO;

public class ScriptServiceTest {

    private ScriptService scriptService;

    @BeforeEach
    public void setUp() {
        scriptService = new ScriptService();
    }

    @Test
    public void testExecuteScriptWithInvalidProjectId() {
        // Setup
        long invalidProjectId = -1L;
        Enums.MethodType methodType = Enums.MethodType.PRECLASSIFY;
        BatchJsonDTO batchJsonDTO = new BatchJsonDTO();

        // Act
        ResponseEntity<ApiResponseDTO<BatchJsonDTO>> response = scriptService.executeScript(invalidProjectId, methodType, batchJsonDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(false, response.getBody().isSuccess());
        assertEquals("projectId must be an integer bigger than 0", response.getBody().getMessage());
    }

    @Test
    public void testExecuteScriptWhenScriptFileDoesNotExist() throws Exception {
        // Setup
        long validProjectId = 1L;
        Enums.MethodType methodType = Enums.MethodType.PRECLASSIFY;
        BatchJsonDTO batchJsonDTO = new BatchJsonDTO();

        // Act
        ResponseEntity<ApiResponseDTO<BatchJsonDTO>> response = scriptService.executeScript(validProjectId, methodType, batchJsonDTO);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(false, response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("does not exist"));
    }  

    @Test
    public void testExecuteScriptInterrupted() {
        long validProjectId = 1L;
        Enums.MethodType methodType = Enums.MethodType.PRECLASSIFY;
        BatchJsonDTO batchJsonDTO = new BatchJsonDTO();

        ResponseEntity<ApiResponseDTO<BatchJsonDTO>> response = scriptService.executeScript(validProjectId, methodType, batchJsonDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(false, response.getBody().isSuccess());
        assertNotNull(response.getBody().getMessage(), "Error message should not be null on interruption.");
    }

}
