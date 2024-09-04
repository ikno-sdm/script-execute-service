package com.ikno.ikdata.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ikno.ikdata.dto.BatchJsonDTO;

@RestController
@RequestMapping("/api/scripts")
public class ScriptController {

    @PostMapping("/execute")
    public ResponseEntity<BatchJsonDTO> executeScript(@RequestParam long projectId,
            @RequestBody BatchJsonDTO batchJsonDTO) {
                
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
