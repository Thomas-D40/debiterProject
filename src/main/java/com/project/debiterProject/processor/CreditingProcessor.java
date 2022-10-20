package com.project.debiterProject.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.project.debiterProject.entity.Client;

@Component
public class CreditingProcessor implements ItemProcessor<Client, Client>{
	
	

	@Override
	public Client process(Client item) throws Exception {
		Client client = new Client();
		client.setId(item.getId());
		client.setName(item.getName());
		client.setAmount(item.getAmount() + 1000);
		
		System.out.println("I'm here!");
		return client;
	}
	

}
