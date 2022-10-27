package com.project.debiterProject.processor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.project.debiterProject.entity.Client;
import com.project.debiterProject.entity.Invoice;

@Component
public class Processors {
	
	// Variable de stockage des factures pour déduction dans la second step	
		private Map<Long, Long> invoicesMap = new HashMap<Long, Long>();
		
		
		public ItemProcessor<Invoice, Invoice> storingInvoice() {
			
			return new ItemProcessor<Invoice, Invoice>() {

				@Override
				public Invoice process(Invoice item) throws Exception {
					System.out.println(item.toString());
					Invoice invoice = new Invoice();
					invoice.setAmount(item.getAmount());
					invoice.setId(item.getId());
					invoice.setIdClient(item.getIdClient());
					invoice.setPaid(item.isPaid());
					
					if (invoice.isPaid() == false) {
					invoicesMap.put(item.getIdClient(), item.getAmount());
					invoice.setPaid(true);
					}
					System.out.println(invoice);
					return invoice;
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
					
					Iterator iterator = invoicesMap.entrySet().iterator();
					
					while (iterator.hasNext()) {
						Map.Entry<Long, Long> m = (Map.Entry<Long, Long>) iterator.next();
						if (m.getKey() == item.getId()) {
							amount = amount - m.getValue();
							iterator.remove();
						}
					}
					
					
					client.setAmount(amount);
					
					System.out.println(("Le client " + client.getName() + " a désormais " + client.getAmount() + "€ sur son compte."));
					
					
					return client;
				}
			};
		}
		

}
