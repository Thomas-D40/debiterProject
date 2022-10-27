package com.project.debiterProject.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;

import com.project.debiterProject.entity.Client;
import com.project.debiterProject.entity.Invoice;
import com.project.debiterProject.processor.Processors;
import com.project.debiterProject.reader.JpaCursorReader;
import com.project.debiterProject.writer.JpaWriter;

@Component
public class DefineDebiterJob {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	

	
	@Autowired
	@Qualifier("dataSource")
	private DataSource dataSource;
	
	@Autowired
	@Qualifier("bankDataSource")
	private DataSource bankDataSource;
	
	@Autowired
	private JpaTransactionManager jpaTransactionManager;
	
	@Autowired
	private JpaCursorReader jpaCursorReader;
	
	@Autowired
	private Processors processors;
	
	@Autowired
	private JpaWriter jpaWriter;
	
		
	
	
	
	@Bean
	public Job debiterJob() {
		return jobBuilderFactory.get("Debiter Job")
				.incrementer(new RunIdIncrementer())
				.start(retrievingFacture())
				.next(debitionStep())
				.build();
	}
	
	// Storing facture in an Hashmap
	
	public Step retrievingFacture() {
		return stepBuilderFactory.get("retrieving factures")
				.<Invoice, Invoice>chunk(10)
				.reader(jpaCursorReader.factureJpaCursorItemReader())
				.processor(processors.storingInvoice())
				.writer(jpaWriter.invoiceJpaItemWriter())
				.transactionManager(jpaTransactionManager)
				.build();
	}
	

	


	
	// Debiting 
	public Step debitionStep() {
		return stepBuilderFactory.get("debiting account")
				.<Client, Client>chunk(10)
				.reader(jpaCursorReader.clientJpaCursorItemReader())				
				.processor(processors.soldingAccount())
				.writer(jpaWriter.clientJpaItemWriter())
				.transactionManager(jpaTransactionManager)
				.build();
	}


}
