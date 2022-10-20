package com.project.debiterProject.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;

import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;

import com.project.debiterProject.entity.Client;
import com.project.debiterProject.entity.Facture;
import com.project.debiterProject.processor.CreditingProcessor;

import antlr.collections.List;

@Component
public class TestDebiterJob {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	@Qualifier("bankEntityManagerFactory")
	private EntityManagerFactory bankEntityManagerFactory;
	
	@Autowired
	private CreditingProcessor creditingProcessor;
	
	@Autowired
	@Qualifier("dataSource")
	private DataSource dataSource;
	
	@Autowired
	@Qualifier("bankDataSource")
	private DataSource bankDataSource;
	
	@Autowired
	private JpaTransactionManager jpaTransactionManager;
	
	private Map<Long, Long> facturesMap = new HashMap<Long, Long>();
	
	
	
	
	
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
				.<Facture, Void>chunk(10)
				.reader(factureJpaCursorItemReader())
				.processor(storingFacture())
				.writer(nothingToDo())
				.build();
	}
	
	public JpaCursorItemReader<Facture> factureJpaCursorItemReader() {
		JpaCursorItemReader<Facture> factureJpaCursorItemReader = new JpaCursorItemReader<>();
		
		factureJpaCursorItemReader.setEntityManagerFactory(bankEntityManagerFactory);
		factureJpaCursorItemReader.setQueryString("From Facture");
		
		System.out.println("Je suis là");
		
		return factureJpaCursorItemReader;
	}
	
	public ItemProcessor<Facture, Void> storingFacture() {
		
		ItemProcessor<Facture, Void> storingProcessor = new ItemProcessor<Facture, Void>() {

			@Override
			public Void process(Facture item) throws Exception {
				facturesMap.put(item.getIdClient(), item.getAmount());
				return null;
			}
		};
		return null;
		
	}
	
	public ItemWriter<Void> nothingToDo() {
		ItemWriter<Void> nothingToDo = new ItemWriter<Void>() {

			@Override
			public void write(java.util.List<? extends Void> items) throws Exception {
				// TODO Auto-generated method stub
				return;
			}
		};
		return nothingToDo;
	}
	

	
	// Debiting 
	public Step debitionStep() {
		return stepBuilderFactory.get("first step")
				.<Client, Client>chunk(10)
				.reader(clientJpaCursorItemReader())				
				.processor(soldingAccount())
				.writer(jpaItemWriter())
				.transactionManager(jpaTransactionManager)
				.build();
	}
	

	public JpaCursorItemReader<Client> clientJpaCursorItemReader() {
		JpaCursorItemReader<Client> jpaCursorItemReader = new JpaCursorItemReader<>();
		
		jpaCursorItemReader.setEntityManagerFactory(bankEntityManagerFactory);
		jpaCursorItemReader.setQueryString("From Client");
		
		System.out.println("Je suis là");
		
		return jpaCursorItemReader;
	}
	
	public ItemProcessor<Client, Client> soldingAccount() {
		ItemProcessor<Client, Client> soldingAccount = new ItemProcessor<Client, Client>() {

			@Override
			public Client process(Client item) throws Exception {
				Client client = new Client();
				client.setId(item.getId());
				client.setName(item.getName());
				
				Long amount = item.getAmount();
				System.out.println(facturesMap.get(amount));
				
				Iterator iterator = facturesMap.entrySet().iterator();
				
				while (iterator.hasNext()) {
					Map.Entry<Long, Long> m = (Map.Entry<Long, Long>) iterator.next();
					System.out.println(m.getValue());
					if (m.getKey() == item.getId()) {
						amount = amount - m.getValue();
					}
				}
				
				
				client.setAmount(amount);
				
				
				System.out.println("I'm here!");
				return client;
			}
		};
		
		return soldingAccount;
	}
	
	public JpaCursorItemReader<Client> facturesJpaCursorItemReader() {
		JpaCursorItemReader<Client> jpaCursorItemReader = new JpaCursorItemReader<>();
		
		jpaCursorItemReader.setEntityManagerFactory(bankEntityManagerFactory);
		jpaCursorItemReader.setQueryString("From Facture");
		
		System.out.println("Je suis là");
		
		return jpaCursorItemReader;
	}
	
	public JpaItemWriter<Client> jpaItemWriter() {
		JpaItemWriter<Client> jpaItemWriter = new JpaItemWriter<>();
		jpaItemWriter.setEntityManagerFactory(bankEntityManagerFactory);
		
		return jpaItemWriter;
	}
	

}