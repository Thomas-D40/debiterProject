package com.project.debiterProject.writer;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.project.debiterProject.entity.Client;
import com.project.debiterProject.entity.PaidInvoice;

@Component
public class JpaWriter {
	
	@Autowired
	@Qualifier("bankEntityManagerFactory")
	private EntityManagerFactory bankEntityManagerFactory;
	
	
	public JpaItemWriter<PaidInvoice> invoiceJpaItemWriter() {
		JpaItemWriter<PaidInvoice> jpaItemWriter = new JpaItemWriter<>();
		jpaItemWriter.setEntityManagerFactory(bankEntityManagerFactory);
		
		return jpaItemWriter;
	}
	
	public JpaItemWriter<Client> clientJpaItemWriter() {
		JpaItemWriter<Client> jpaItemWriter = new JpaItemWriter<>();
		jpaItemWriter.setEntityManagerFactory(bankEntityManagerFactory);
		
		return jpaItemWriter;
	}

}
