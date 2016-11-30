package com.esofa.crm.reports.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.esofa.crm.model.pos.Invoice;
import com.esofa.crm.model.pos.InvoicePayment;
import com.esofa.crm.model.report.DateRangeReportReq;
import com.esofa.crm.service.SalesService;
import com.googlecode.objectify.Key;

public class CollectionReport extends BaseCrmReport<DateRangeReportReq> {

	private SalesService salesService;
	
	List<String> descriptionList;

	@Override
	protected List<Map<String,String>> getData(DateRangeReportReq req) {
		Date fromDate = req.getFromDate();
		Date endDate = req.getEndDate();
		
		List<InvoicePayment> rawResults = salesService.getInvoicePayments(descriptionList, fromDate, endDate);
		
		List<Map<String,String>> resultList = new ArrayList<Map<String,String>>();
		Map<Key<Invoice>,String> invoiceNumberCache = new HashMap<Key<Invoice>,String>();
		Map<Key<Invoice>,String> invoiceStatusCache = new HashMap<Key<Invoice>,String>();
		Map<Key<Invoice>,String> invoiceStoreCache = new HashMap<Key<Invoice>,String>();
		
		//JH TODO: change to issue batch query instead
		for (InvoicePayment ip : rawResults) {
		
			Map<String,String> row = new HashMap<String,String>();
			String invoiceNumber= StringUtils.EMPTY;
			String invoiceStatus = StringUtils.EMPTY;
			String storeName = StringUtils.EMPTY;
			
			if (!invoiceNumberCache.containsKey(ip.getInvoiceKey())) {
				Invoice i = null;
						
				try {
				i =salesService.getInvoice(ip.getInvoiceKey());
				} catch (com.googlecode.objectify.NotFoundException nfe) {
					log.fine("unable to find invoiceKey:" + ip.getInvoiceKey());
					i = null;
				}
				
				if ( i != null) { 
					
					invoiceNumber = i.getInvoiceNumber();
					invoiceStatus = i.getStatus();
					storeName=i.getShopName();
					invoiceNumberCache.put(ip.getInvoiceKey(), invoiceNumber);
					invoiceStatusCache.put(ip.getInvoiceKey(), invoiceStatus);
					invoiceStoreCache.put(ip.getInvoiceKey(), storeName);
					
				}
			} else {
				invoiceNumber= invoiceNumberCache.get(ip.getInvoiceKey());	
				invoiceStatus = invoiceStatusCache.get(ip.getInvoiceKey());
				storeName = invoiceStoreCache.get(ip.getInvoiceKey());
				
			}
			
			row.put("Invoice Number", invoiceNumber);
			row.put("Invoice Status", invoiceStatus);
			row.put("Store Name", storeName);
			
			populate(row,ip);
			resultList.add(row);
		}
		
		return resultList;
	}

	public void setDescriptionList(List<String> descriptionList) {
		this.descriptionList = descriptionList;
	}
	
	
	public void setSalesService(SalesService salesService) {
		this.salesService = salesService;
	}
}

