package com.project.debiterProject.processor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.project.debiterProject.entity.Client;
import com.project.debiterProject.entity.Facture;

@Component
public class Processors {
	
	// Variable de stockage des factures pour déduction dans la second step	
		private Map<Long, Long> facturesMap = new HashMap<Long, Long>();
		
		
		public ItemProcessor<Facture, Void> storingFacture() {
			
			return new ItemProcessor<Facture, Void>() {

				@Override
				public Void process(Facture item) throws Exception {
					facturesMap.put(item.getIdClient(), item.getAmount());
					return null;
				}
			};

			
		}
		
		public ItemProcessor<Client, Client> soldingAccount() {
			return new ItemProcessor<Client, Client>() {

				@Override
				public Client process(Client item) throws Exception {
					Client client = new Client();
					client.setId(item.getId());
					client.setName(item.getName());
					
					Long amount = item.getAmount();
					
					Iterator iterator = facturesMap.entrySet().iterator();
					
					while (iterator.hasNext()) {
						Map.Entry<Long, Long> m = (Map.Entry<Long, Long>) iterator.next();
						if (m.getKey() == item.getId()) {
							amount = amount - m.getValue();
						}
					}
					
					
					client.setAmount(amount);
					
					System.out.println(("Le client " + client.getName() + " a désormais " + client.getAmount() + "€ sur son compte."));
					
					
					return client;
				}
			};
		}
		

}
