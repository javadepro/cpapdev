package com.esofa.crm.controller.util;

import com.esofa.crm.refdata.model.Shop;
import com.googlecode.objectify.Key;


public class InvoiceSearchForm  {

	private String invoiceNumber;
	private int numMths;
	private Key<Shop> shop;
	
	

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public int getNumMths() {
		return numMths;
	}

	public void setNumMths(int numMths) {
		this.numMths = numMths;
	}

	public Key<Shop> getShop() {
		return shop;
	}

	public void setShop(Key<Shop> shop) {
		this.shop = shop;
	}
	
	
}
