package com.orchestrator.controlplane.service;

import com.orchestrator.controlplane.entity.TaskStatus;
import com.orchestrator.controlplane.entity.WorkflowTask;
import com.orchestrator.controlplane.repository.WorkflowTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class WorkflowAsyncWorker {

    private final WorkflowTaskRepository repository;

    @Async
    public CompletableFuture<Void> pollForCsrApproval(UUID taskId) {
        // Fetch current task and add initial log
        WorkflowTask task = repository.findById(taskId).orElseThrow();
        task.addLog("Control Plane Initiated: Starting Import Cluster workflow...");
        repository.save(task);

        long startTime = System.currentTimeMillis();
        long timeoutLimit = 5 * 60 * 1000;
        RestTemplate restTemplate = new RestTemplate();
        // Purana code:
// String agentUrl = "http://localhost:8081/api/csr-status?taskId=" + taskId;

// Naya code:
        String agentUrl = "https://go-agent-jd1m.onrender.com/api/csr-status?taskId=" + taskId;
        boolean taskFinished = false;

        while ((System.currentTimeMillis() - startTime) < timeoutLimit) {
            try {
                // Fetch fresh task to avoid overwriting issues
                task = repository.findById(taskId).get();
                task.addLog("Polling Go Agent for CSR status...");
                repository.save(task);

                Map<String, String> response = restTemplate.getForObject(agentUrl, Map.class);
                if (response != null) {
                    String status = response.get("status");

                    if ("APPROVED".equals(status) || "FAILED".equals(status)) {
                        task = repository.findById(taskId).get();
                        task.setStatus("APPROVED".equals(status) ? TaskStatus.COMPLETED : TaskStatus.FAILED);
                        task.addLog("Agent replied with final status: " + status);
                        task.addLog("Workflow execution finalized.");
                        repository.save(task);
                        taskFinished = true;
                        break;
                    }
                }
                Thread.sleep(2000);
            } catch (Exception e) {
                task = repository.findById(taskId).get();
                task.addLog("ERROR: Go Agent not reachable. Retrying...");
                repository.save(task);
                try { Thread.sleep(2000); } catch (InterruptedException ie) { break; }
            }
        }

        if (!taskFinished) {
            task = repository.findById(taskId).get();
            task.setStatus(TaskStatus.TIMED_OUT);
            task.addLog("FATAL: Operation timed out after 5 minutes.");
            repository.save(task);
        }

        return CompletableFuture.completedFuture(null);
    }
}