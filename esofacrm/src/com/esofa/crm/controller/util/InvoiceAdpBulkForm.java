package com.esofa.crm.controller.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.FactoryUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;


public class InvoiceAdpBulkForm  {

	private String actionType;
	
	//search fields
	private int fromNumMths;
	private int toNumMths;
	private String adpStatus;
	
	//processing
	private Date processedDate;
	

	private List<String> invoiceSelected;

	public InvoiceAdpBulkForm() {
	
		initInvoiceSelected();
	}
		
	public void initInvoiceSelected() {
		invoiceSelected = ListUtils.lazyList(new ArrayList<String>(), FactoryUtils.instantiateFactory(String.class));
	}
	
	public void setInvoiceSelected(List<String> invoiceSelected) {
		this.invoiceSelected = invoiceSelected;
	}
	
	public List<String> getInvoiceSelected() {
		return invoiceSelected;
	}
	
	public void initInvoiceSelected(int size) {
		
		invoiceSelected = new ArrayList<String>(size);
		for (int i =0; i < size; i++) {
			invoiceSelected.add(i, StringUtils.EMPTY);
		}
	}
	
	
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public int getFromNumMths() {
		return fromNumMths;
	}
	public void setFromNumMths(int fromNumMths) {
		this.fromNumMths = fromNumMths;
	}
	public int getToNumMths() {
		return toNumMths;
	}
	public void setToNumMths(int toNumMths) {
		this.toNumMths = toNumMths;
	}
	public String getAdpStatus() {
		return adpStatus;
	}
	public void setAdpStatus(String adpStatus) {
		this.adpStatus = adpStatus;
	}
	
	public void setProcessedDate(Date processedDate) {
		this.processedDate = processedDate;
	}
	
	public Date getProcessedDate() {
		return processedDate;
	}

	
	
}
