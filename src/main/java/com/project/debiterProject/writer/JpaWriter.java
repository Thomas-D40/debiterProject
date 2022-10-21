package com.project.debiterProject.writer;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.project.debiterProject.entity.Client;

@Component
public class JpaWriter {
	
	@Autowired
	@Qualifier("bankEntityManagerFactory")
	private EntityManagerFactory bankEntityManagerFactory;
	
	
	public ItemWriter<Void> nothingToDo() {
		return new ItemWriter<Void>() {

			@Override
			public void write(java.util.List<? extends Void> items) throws Exception {
				return;
			}
		};
	}
	
	public JpaItemWriter<Client> jpaItemWriter() {
		JpaItemWriter<Client> jpaItemWriter = new JpaItemWriter<>();
		jpaItemWriter.setEntityManagerFactory(bankEntityManagerFactory);
		
		return jpaItemWriter;
	}

}
