package com.orchestrator.controlplane.controller;

import com.orchestrator.controlplane.entity.WorkflowTask;
import com.orchestrator.controlplane.service.WorkflowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/workflows")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class WorkflowController {

    private final WorkflowService service;

    @PostMapping("/create")
    public ResponseEntity<WorkflowTask> startTask(@RequestParam String taskType) {
        WorkflowTask task = service.createTask(taskType);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkflowTask> getTaskStatus(@PathVariable UUID id) {
        return service.getTask(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}