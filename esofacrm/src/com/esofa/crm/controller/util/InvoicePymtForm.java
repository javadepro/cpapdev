package com.esofa.crm.controller.util;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;


public class InvoicePymtForm {

	private String invoiceNumber;
	
	@NotNull
	@Digits( integer=8, fraction=2)
	private BigDecimal paymentAmount;

	@NotNull	
	private String paymentMethod;
	
	private String description;
	
	public InvoicePymtForm() {
		paymentAmount = BigDecimal.ZERO.setScale(2);
	}
	
	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(BigDecimal paymentAmount) {
		
		if (paymentAmount == null ) {
			
			paymentAmount = BigDecimal.ZERO;
		}
		
		if (paymentAmount  != null) {
			this.paymentAmount = paymentAmount.setScale(2);
		}
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
	

}
