package com.ikno.ikdata.service;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ikno.ikdata.common.Enums;
import com.ikno.ikdata.common.Enums.MethodType;
import com.ikno.ikdata.dto.ApiResponseDTO;
import com.ikno.ikdata.dto.batchjson.BatchJsonDTO;
import com.ikno.ikdata.external.FileService;

@ExtendWith(MockitoExtension.class)
class ScriptServiceTest {

    @Mock
    private ProcessBuilder processBuilder;

    @Mock
    private FileService fileService;

    @InjectMocks
    private ScriptService scriptService;

    @SuppressWarnings("null")
    @Test
    void testExecuteScriptWithInvalidProjectId() {
        // Setup
        long invalidProjectId = -1L;
        Enums.MethodType methodType = MethodType.PRECLASSIFY;
        BatchJsonDTO batchJsonDTO = new BatchJsonDTO();

        // Act
        ResponseEntity<ApiResponseDTO<BatchJsonDTO>> response = scriptService.executeScript(invalidProjectId,
                methodType, batchJsonDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(false, response.getBody().isSuccess());
        assertEquals("projectId must be an integer bigger than 0", response.getBody().getMessage());
    }

    @Test
    void testExecuteScriptWhenScriptFileDoesNotExist() {
        // Setup
        long validProjectId = 1L;
        Enums.MethodType methodType = Enums.MethodType.PRECLASSIFY;
        BatchJsonDTO batchJsonDTO = new BatchJsonDTO();

        // Simula la no existencia del archivo
        when(fileService.fileExists(anyString())).thenReturn(false);

        // Act
        ResponseEntity<ApiResponseDTO<BatchJsonDTO>> response = scriptService.executeScript(validProjectId, methodType,
                batchJsonDTO);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(false, response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("does not exist"));
    }

    @Test
    void testExecuteScriptWhenScriptFails() throws IOException, InterruptedException {
        //Setup
        long projectId = 1L;
        MethodType method = MethodType.VALIDATE;
        BatchJsonDTO batchJsonDTO = new BatchJsonDTO();
        ObjectMapper objectMapper = new ObjectMapper();

        // Simulate calls if any file exists
        when(fileService.fileExists(anyString())).thenReturn(true);

        // Simulates calls to write files
        doNothing().when(fileService).writeToFile(any(Path.class), any(byte[].class));

        // Simulates calls to delete files
        when(fileService.deleteFileIfExists(any(Path.class))).thenReturn(true);

        // Simulates a process return equal to 1 (error)
        Process mockProcess = mock(Process.class);
        when(processBuilder.start()).thenReturn(mockProcess);
        when(mockProcess.waitFor()).thenReturn(1);

       // Simulates output capture
        InputStream mockInputStream = new ByteArrayInputStream(
                objectMapper.writeValueAsString(batchJsonDTO).getBytes());
        InputStream mockErrorStream = new ByteArrayInputStream("".getBytes());
        when(mockProcess.getInputStream()).thenReturn(mockInputStream);
        when(mockProcess.getErrorStream()).thenReturn(mockErrorStream);

        // Act
        ResponseEntity<ApiResponseDTO<BatchJsonDTO>> response = scriptService.executeScript(projectId, method,
                batchJsonDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotEquals("Script successfully executed", response.getBody().getMessage());
    }

    @Test
    void testExecuteScriptWhenScriptSuccess() throws IOException, InterruptedException {
        // Setup
        long projectId = 1L;
        MethodType method = MethodType.VALIDATE;
        BatchJsonDTO batchJsonDTO = new BatchJsonDTO();
        ObjectMapper objectMapper = new ObjectMapper();

        // Simulate calls if any file exists
        when(fileService.fileExists(anyString())).thenReturn(true);

        // Simulates calls to write files
        doNothing().when(fileService).writeToFile(any(Path.class), any(byte[].class));

        // Simulates calls to delete files
        when(fileService.deleteFileIfExists(any(Path.class))).thenReturn(true);

        // Simulates a process return equal to 0 (success)
        Process mockProcess = mock(Process.class);
        when(processBuilder.start()).thenReturn(mockProcess);
        when(mockProcess.waitFor()).thenReturn(0);

       // Simulates output capture
        InputStream mockInputStream = new ByteArrayInputStream(
                objectMapper.writeValueAsString(batchJsonDTO).getBytes());
        InputStream mockErrorStream = new ByteArrayInputStream("".getBytes());
        when(mockProcess.getInputStream()).thenReturn(mockInputStream);
        when(mockProcess.getErrorStream()).thenReturn(mockErrorStream);

        // Act
        ResponseEntity<ApiResponseDTO<BatchJsonDTO>> response = scriptService.executeScript(projectId, method, batchJsonDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Script successfully executed", response.getBody().getMessage());
    }

}
