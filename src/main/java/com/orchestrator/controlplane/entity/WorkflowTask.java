package com.orchestrator.controlplane.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "workflow_tasks")
@Data // Lombok annotation: ye apne aap getters, setters, aur toString bana dega
public class WorkflowTask {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // Har task ka ek unique ID hoga

    private String taskType; // Konsa task hai? Jaise "IMPORT_CLUSTER"

    @Enumerated(EnumType.STRING)
    private TaskStatus status; // PENDING, TIMED_OUT, etc.
    @Column(columnDefinition = "TEXT")
    private String terminalLogs = "";

    // Helper method to add logs with exact time
    public void addLog(String message) {
        String time = java.time.LocalTime.now().toString().substring(0, 8); // Gets HH:mm:ss
        this.terminalLogs += "> [" + time + "] " + message + "\n";
    }

    // Getters and Setters (agar Lombok @Data use kar rahe ho toh inki zaroorat nahi)
    // Getter aur Setter zaroori hain JSON serialization ke liye
    public String getTerminalLogs() {
        return terminalLogs;
    }

    public void setTerminalLogs(String terminalLogs) {
        this.terminalLogs = terminalLogs;
    }


    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt; // Kab start hua

    @UpdateTimestamp
    private LocalDateTime updatedAt; // Last kab update hua
}