package com.orchestrator.controlplane.service;

import com.orchestrator.controlplane.entity.TaskStatus;
import com.orchestrator.controlplane.entity.WorkflowTask;
import com.orchestrator.controlplane.repository.WorkflowTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkflowService {

    private final WorkflowTaskRepository repository;
    private final WorkflowAsyncWorker asyncWorker; // Injecting the new worker

    public WorkflowTask createTask(String taskType) {
        WorkflowTask task = new WorkflowTask();
        task.setTaskType(taskType);
        task.setStatus(TaskStatus.PENDING);
        task = repository.save(task);

        if ("IMPORT_CLUSTER".equals(taskType)) {
            // Ab yeh dusri class ka method hai, toh completely Async run hoga!
            asyncWorker.pollForCsrApproval(task.getId());
        }

        return task;
    }

    public Optional<WorkflowTask> getTask(UUID id) {
        return repository.findById(id);
    }
}