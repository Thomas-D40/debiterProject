package com.project.debiterProject.processor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.project.debiterProject.entity.Client;
import com.project.debiterProject.entity.PaidInvoice;
import com.project.debiterProject.entity.UnpaidInvoice;

@Component
public class Processors {
	
	// Variable de stockage des factures pour déduction dans la second step	
		private Map<Long, Long> invoicesMap = new HashMap<Long, Long>();
		
		
		public ItemProcessor<UnpaidInvoice, PaidInvoice> storingInvoice() {
			
			return new ItemProcessor<UnpaidInvoice, PaidInvoice>() {

				@Override
				public PaidInvoice process(UnpaidInvoice item) throws Exception {
					System.out.println(item.toString());
					PaidInvoice paidInvoice = new PaidInvoice();
					paidInvoice.setAmount(item.getAmount());
					paidInvoice.setId(item.getId());
					paidInvoice.setIdClient(item.getIdClient());								
				
					invoicesMap.put(item.getIdClient(), item.getAmount());
					
					System.out.println(paidInvoice);
					return paidInvoice;
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
