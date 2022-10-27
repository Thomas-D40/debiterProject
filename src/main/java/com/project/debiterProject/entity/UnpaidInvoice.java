package com.project.debiterProject.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="unpaid_invoices")
public class UnpaidInvoice {
	
	@Id
	private Long id;
	
	
	@Column(name ="client_id")
	private Long idClient;
	
	private Long amount;
	
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdClient() {
		return idClient;
	}

	public void setIdClient(Long idClient) {
		this.idClient = idClient;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}


	@Override
	public String toString() {
		return "Invoice [id=" + id + ", idClient=" + idClient + ", amount=" + amount + "]";
	}

	
	
	
	
	

}
