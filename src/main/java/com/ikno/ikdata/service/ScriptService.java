package com.ikno.ikdata.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ikno.ikdata.dto.BatchJsonDTO;

@Service
public class ScriptService {

    public ResponseEntity<BatchJsonDTO> executeScript(long projectId, BatchJsonDTO batchJsonDTO){
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
