package com.orchestrator.controlplane;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync // Ye annotation background threads allow karta hai
public class ControlPlaneApplication {
	public static void main(String[] args) {
		SpringApplication.run(ControlPlaneApplication.class, args);
	}
}