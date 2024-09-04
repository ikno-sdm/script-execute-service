package com.ikno.ikdata.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ikno.ikdata.common.Enums.MethodType;
import com.ikno.ikdata.dto.BatchJsonDTO;
import com.ikno.ikdata.service.ScriptService;

@RestController
@RequestMapping("/api/scripts")
public class ScriptController {

    private final ScriptService scriptService;

    public ScriptController(ScriptService scriptService) {
        this.scriptService = scriptService;
    }

    @PostMapping("/execute")
    public ResponseEntity<BatchJsonDTO> executeScript(@RequestParam long projectId, @RequestParam MethodType method,
            @RequestBody BatchJsonDTO batchJsonDTO) {
        return scriptService.executeScript(projectId, method, batchJsonDTO);
    }
}
