package com.esofa.crm.controller.util;

import java.io.Serializable;

import com.esofa.crm.model.pos.Invoice;
import com.esofa.crm.model.pos.InvoicePayment;

public class InvoicePaymentWrapper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1630293101340072961L;

	private Invoice invoice;
	private InvoicePayment invoicePayment;
	public Invoice getInvoice() {
		return invoice;
	}
	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}
	public InvoicePayment getInvoicePayment() {
		return invoicePayment;
	}
	public void setInvoicePayment(InvoicePayment invoicePayment) {
		this.invoicePayment = invoicePayment;
	}
	
	
}
