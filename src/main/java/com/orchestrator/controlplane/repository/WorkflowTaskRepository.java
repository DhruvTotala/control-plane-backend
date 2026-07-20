package com.orchestrator.controlplane.repository;

import com.orchestrator.controlplane.entity.WorkflowTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WorkflowTaskRepository extends JpaRepository<WorkflowTask, UUID> {
    // Spring Data JPA apne aap saare basic save, find, delete methods de dega.
}