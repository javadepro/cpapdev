package com.esofa.crm.model.pos;

import java.math.BigDecimal;
import java.util.Date;

import com.googlecode.objectify.Key;

public interface InvoiceHistoryItem {

	public  Long getId();
	
	public  Key<Invoice> getInvoiceKey();

	public  Date getPaymentDate();

	public  String getDescription();

	public  String getAmt();

	public  String getBalance();

	public  BigDecimal getAmtAsBD();

	public  BigDecimal getBalanceAsBD();

}