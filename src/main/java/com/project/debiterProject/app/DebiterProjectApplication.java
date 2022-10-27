package com.project.debiterProject.app;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableBatchProcessing
@EnableScheduling
@ComponentScan({"com.project.debiterProject.config","com.project.debiterProject.processor","com.project.debiterProject.entity",
	"com.project.debiterProject.reader", "com.project.debiterProject.writer", "com.project.debiterProject.service"})
public class DebiterProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(DebiterProjectApplication.class, args);
	}

}
