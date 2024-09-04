package com.ikno.ikdata.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ikno.ikdata.common.Enums.MethodType;
import com.ikno.ikdata.dto.ApiResponseDTO;
import com.ikno.ikdata.dto.batchjson.BatchJsonDTO;

@Service
public class ScriptService {

    private static final String SEPARATOR = File.separator;

    @Value("${ikdata.projects.folder}")
    private String projectsFolderPath;

    public ResponseEntity<ApiResponseDTO<BatchJsonDTO>> executeScript(long projectId, MethodType method,
            BatchJsonDTO batchJsonDTO) {
        ObjectMapper objectMapper = new ObjectMapper();
        BatchJsonDTO batchJsonResultDTO = new BatchJsonDTO();

        String scriptPath = projectsFolderPath + SEPARATOR + "P" + projectId + SEPARATOR + "scripts" + SEPARATOR
                + method + ".js";

        try {
            if (projectId <= 0) {
                throw new InvalidParameterException("projectId must be an integer bigger than 0");
            }

            if (!new File(scriptPath).exists()) {
                throw new IOException("IkData project script " + scriptPath + " does not exist");
            }

            String batchJsonTempPath = projectsFolderPath + SEPARATOR + "P" + projectId + SEPARATOR + "batchJson-"
                    + new Date().getTime() + ".json";

            // Temporally saves the batchJson in a file
            String jsonString = objectMapper.writeValueAsString(batchJsonDTO);
            Path tempJsonFile = Paths.get(batchJsonTempPath);
            Files.write(tempJsonFile, jsonString.getBytes());

            // Build the command to execute the NodeJS script using the temp batchJson file
            // as argument
            ProcessBuilder processBuilder = new ProcessBuilder("node", scriptPath, batchJsonTempPath);
            Process process = processBuilder.start();

            // Read the standard and error output to get the object result in String
            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stderrReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            StringBuilder output = new StringBuilder();

            Thread stdoutThread = new Thread(() -> {
                try {
                    String line;
                    while ((line = stdoutReader.readLine()) != null) {
                        output.append(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            StringBuilder errorOutput = new StringBuilder();
            String errline;
            while ((errline = stderrReader.readLine()) != null) {
                errorOutput.append(errline);
            }

            stdoutThread.start();

            // Wait execution and deserialize the output to create the BatchJson result
            int exitCode = process.waitFor();
            stdoutThread.join();
            if (exitCode == 0) {
                String batchJsonResultString = output.toString();
                batchJsonResultDTO = objectMapper.readValue(batchJsonResultString, BatchJsonDTO.class);
                Files.deleteIfExists(tempJsonFile);
            } else {
                Files.deleteIfExists(tempJsonFile);
                throw new IOException(errorOutput.toString());
            }

        } catch (InvalidParameterException e) {
            e.printStackTrace();
            ApiResponseDTO<BatchJsonDTO> apiResponseDTO = new ApiResponseDTO<>(false, e.getMessage());
            return new ResponseEntity<>(apiResponseDTO, HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
            ApiResponseDTO<BatchJsonDTO> apiResponseDTO = new ApiResponseDTO<>(false, e.getMessage());
            return new ResponseEntity<>(apiResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
            ApiResponseDTO<BatchJsonDTO> apiResponseDTO = new ApiResponseDTO<>(false, e.getMessage());
            return new ResponseEntity<>(apiResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // If no exceptions return success response
        ApiResponseDTO<BatchJsonDTO> apiResponseDTO = new ApiResponseDTO<>(true, batchJsonResultDTO,
                "Script successfully executed");
        return new ResponseEntity<>(apiResponseDTO, HttpStatus.OK);
    }
}
