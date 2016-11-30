package com.esofa.crm.util;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import com.esofa.crm.controller.util.InvoicePymtForm;
import com.esofa.crm.model.pos.Invoice;
import com.esofa.crm.model.pos.InvoicePayment;
import com.googlecode.objectify.Key;

public class InvoicePaymentUtils {

	public static InvoicePayment generateInvoicePayment(InvoicePymtForm invoicePymtForm, Key<Invoice> invoiceKey, BigDecimal originalBalance, String userFirstName, String userLastName) {
				
		InvoicePayment invoicePayment = createBasicInvoicePayment(invoiceKey,userFirstName,userLastName,EsofaUtils.getMidnight());
		
		invoicePayment.setPaymentMethod(invoicePymtForm.getPaymentMethod());
		invoicePayment.setAmtAsBD(invoicePymtForm.getPaymentAmount());

		BigDecimal newBalance = originalBalance.subtract(invoicePymtForm.getPaymentAmount()).setScale(Invoice.SCALE);
		invoicePayment.setBalanceAsBD(newBalance);
		invoicePayment.setDescription(invoicePymtForm.getDescription());
		
		return invoicePayment;
	}

	
	public static InvoicePayment createFromInvoice(Invoice invoice, Key<Invoice> invoiceKey ) {
		
		InvoicePayment invoicePayment = createBasicInvoicePayment(invoiceKey,
																  invoice.getUserFirstName(),
																  invoice.getUserLastName(),
																  invoice.getInvoiceDate());
		invoicePayment.setAmt(invoice.getAdjInvoiceClientPortion());				
		invoicePayment.setPaymentMethod(invoice.getPaymentMethod());				
		return invoicePayment;
	}
	
	public static InvoicePayment createForTrialDepositRefund(Invoice refInvoice, Invoice invoice) {
		
		InvoicePayment invoicePayment = createBasicInvoicePayment(Key.create(Invoice.class,invoice.getId()),
				  												  invoice.getUserFirstName(),
				  												  invoice.getUserLastName(),
				  												  invoice.getInvoiceDate());
		
		//adjust since usually saved as pairs. set this back one second so it would show first
		//stupid gae stores up to sec only.
		Date insertDT = invoicePayment.getInsertDateTime();
		invoicePayment.setInsertDateTime(DateUtils.addSeconds(insertDT, -1));
		
		invoicePayment.setAmtAsBD(refInvoice.getTotalAsBD().negate());
		invoicePayment.setPaymentMethod(refInvoice.getPaymentMethod());
		invoicePayment.setDescription(InvoicePayment.DESC_DEPOSIT_REFUND);
		return invoicePayment;
	}
	
	public static InvoicePayment  createForTrial2Purchase(Invoice refInvoice,Invoice invoice, BigDecimal amt, boolean isTransfer) {
		
		InvoicePayment invoicePayment = createBasicInvoicePayment(Key.create(Invoice.class,invoice.getId()),
				  												  invoice.getUserFirstName(),
				  												  invoice.getUserLastName(),
				  												  invoice.getInvoiceDate());		
		
		//credit subtracts from the total...but is considered ADDED to the payment bucket.
		invoicePayment.setAmtAsBD(amt);
		invoicePayment.setPaymentMethod(refInvoice.getPaymentMethod());
		if (isTransfer) {
			invoicePayment.setDescription(InvoicePayment.DESC_TRANSFER_TO_PAYMENT);
		} else {
			invoicePayment.setDescription(InvoicePayment.PAYMENT);
		}
		return invoicePayment;
	}
	
	public static InvoicePayment  createForOtherIncome(Invoice refInvoice,Invoice invoice, BigDecimal amt, boolean isTransfer) {
		
		InvoicePayment invoicePayment = createBasicInvoicePayment(Key.create(Invoice.class,invoice.getId()),
																  invoice.getUserFirstName(),
																  invoice.getUserLastName(),
																  invoice.getInvoiceDate());
		
		invoicePayment.setAmt(amt.toString());
		invoicePayment.setPaymentMethod(refInvoice.getPaymentMethod());
		if (isTransfer) {
			invoicePayment.setDescription(InvoicePayment.DESC_TRANSFER_TO_OTHERINCOME);
		} else {
			invoicePayment.setDescription(InvoicePayment.DESC_OTHERINCOME);
		}
		return invoicePayment;
	}
	

	private static InvoicePayment createBasicInvoicePayment(Key<Invoice> invoiceKey, String userFirstName, String userLastName, Date invoiceDate) {
		
		InvoicePayment invoicePayment = new InvoicePayment();
		invoicePayment.setInsertDateTime(new Date());
		invoicePayment.setUserFirstName(userFirstName);
		invoicePayment.setUserLastName(userLastName);
		invoicePayment.setPaymentDate(EsofaUtils.getMidnight(invoiceDate));
		invoicePayment.setInvoiceKey(invoiceKey);

		
		return invoicePayment;
	}
}
