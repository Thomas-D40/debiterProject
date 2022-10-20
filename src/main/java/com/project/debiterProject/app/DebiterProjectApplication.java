package com.project.debiterProject.app;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableBatchProcessing
@ComponentScan({"com.project.debiterProject.config","com.project.debiterProject.processor","com.project.debiterProject.entity"})
public class DebiterProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(DebiterProjectApplication.class, args);
	}

}
