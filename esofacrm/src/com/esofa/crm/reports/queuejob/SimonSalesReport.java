package com.esofa.crm.reports.queuejob;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.esofa.crm.model.pos.Invoice;
import com.esofa.crm.model.pos.InvoiceItem;
import com.esofa.crm.queuejob.AbstractEmailReportTask;
import com.esofa.crm.refdata.model.Shop;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.crm.reports.MapToCsvWriter;
import com.esofa.crm.service.SalesService;
import com.esofa.crm.util.EsofaUtils;
import com.googlecode.objectify.Key;

public class SimonSalesReport extends AbstractEmailReportTask<Map<String,String>> {

	
	private static final String CUR_SHOP="CURRENT_SHOP_NAME";
	private static final String FROM_DATE = "FROM";
	private static final String END_DATE = "END";
	
	
	private SalesService salesService;
	private RefdataService refdataService;
	
	
	private List<Map<String,String>> resultList;
	
	Map<Key<Invoice>,Invoice> invoiceMap;
	private Iterator<InvoiceItem> itemsIter;
	
	private Iterator<Shop> shopIter;
	
	
	protected Date fromDate = null;
	protected Date endDate = null;
	
	public SimonSalesReport() throws IOException {
		super();
	}


	@Override
	public Map<String, String> executeTask(Map<String, String> params) {
	
		String rqId = params.get(REQUEST_ID_PARAM);
	
		// first iteration should not have rqId.
		// create one, and do initial setup.
		if (StringUtils.isEmpty(rqId)) {
	
			setRqId(params);
			initTask(params);
	
			// clean up and return null to end iteration
		} 
	
		// actually do the work.
		// return params for next iteration. if no more work, next iteration
		// will be the last
		Map<String,String> processedParams = doTask(params);
		
		if (processedParams == null) {executeOnComplete(null);} 
		return processedParams;
	}

	
	@Override
	protected void initTask(Map<String, String> params) {

		//handle dates
		if (params.containsKey(FROM_DATE)) {
			
			try {
				fromDate = EsofaUtils.getDateFrom(params.get(FROM_DATE));
			} catch (ParseException e) {
				log.severe("unable to parse date: " + params.get(FROM_DATE));;
				log.severe(ExceptionUtils.getStackTrace(e));
				fromDate = EsofaUtils.getMidnight(EsofaUtils.getDateAfterNDays(-1));
			}
		} else {
			fromDate = EsofaUtils.getMidnight(EsofaUtils.getDateAfterNDays(-1));
		}
		
		if (params.containsKey(END_DATE)) {
			
			try {
				endDate = EsofaUtils.getDateFrom(params.get(END_DATE));
			} catch (ParseException e) {
				log.severe("unable to parse date: " + params.get(END_DATE));;
				log.severe(ExceptionUtils.getStackTrace(e));
				endDate = EsofaUtils.getMidnight();
			}			
		} else {
			
			endDate = EsofaUtils.getMidnight();
		}
		
		
		shopIter = refdataService.getShopMap().values().iterator();
		initOneStore(params);
	}
	
	protected boolean initOneStore(Map<String,String> params) {
		
		if (shopIter == null || !shopIter.hasNext()) {
			return false;
		}
		
		Shop s = shopIter.next();		
		if (StringUtils.equalsIgnoreCase("WW", s.getShortName())) {
			
			if (!shopIter.hasNext()) {
				return false;
				
			} else {
				s = shopIter.next();
			}
		}
			
		Key<Shop> shopKey = Key.create(Shop.class,s.getId());		
		params.put(CUR_SHOP, s.getShortName());
		
		List<Invoice> rawResults = salesService.getInvoices(fromDate, endDate, shopKey,true);		
		invoiceMap = new HashMap<Key<Invoice>,Invoice>(rawResults.size());
		
		for (Invoice i : rawResults) {
			
			Key<Invoice> iKey =Key.create(Invoice.class,i.getId());
			invoiceMap.put(iKey,i);
		}
		
		//dont need you anymore
		rawResults = null;		
		
		if (invoiceMap.size() > 0) {
			//get invoice items based on invoicekeys.
			Key<Invoice>[] invoiceKeys = (Key<Invoice>[]) Array.newInstance(Key.class, 0);
					invoiceKeys = invoiceMap.keySet().toArray(invoiceKeys);	
							
			List<InvoiceItem> rawItems = salesService.getInvoiceItems(invoiceKeys );
			itemsIter = rawItems.iterator();
		}
		resultList = new ArrayList<Map<String,String>>();

		return true;
		
	}
	

	@Override
	protected Map<String, String> doTask(Map<String, String> params) {

		if (itemsIter == null || !itemsIter.hasNext()) {	//done iterating one store
			
			finishOneShop(params);		
			boolean initStoreSuccess = initOneStore(params);	//check if there are more stores
			if (!initStoreSuccess) {
				
				return null;
			} else {
				return params;
			}
		}

		InvoiceItem ii = itemsIter.next();
		Map<String,String> row = new HashMap<String,String>();
		
		Invoice i= invoiceMap.get(ii.getInvoiceKey());
		
		
		row.put("Product", ii.getProductName());		
		row.put("Clinician Name",i.getUserName());
		row.put("Clinician First Name",i.getUserFirstName());
		row.put("Clinician Last Name",i.getUserLastName());		
		row.put("Quantity",Integer.toString(ii.getQty()));
		row.put("Location", i.getShopName());
		MapToCsvWriter.populate(mapping,row,ii);
		resultList.add(row);
		
		return params;
	}
	
	protected void finishOneShop(Map<String, String> params) {
		

		MapToCsvWriter mapToCsvWriter = new MapToCsvWriter();
		String formattedDate = EsofaUtils.getDateFormatted(fromDate);
		String toEmail = configService.getConfigStringByName(configToEmailAddrKey);
		String fileId = "SimonSalesReport_" + formattedDate + "_" +params.get(CUR_SHOP) +".csv";
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Writer w = new OutputStreamWriter(new BufferedOutputStream(baos)); 
			
			mapToCsvWriter.setHeader(getHeader()).setMapping(mapping).setWriter(w).write(resultList);
			InputStream resultsInputStream = new ByteArrayInputStream(baos.toByteArray());
				
			mailUtils.sendMail(toEmail, fileId, "simon sales report",resultsInputStream ,fileId,"text/csv");
			
		} catch (IOException e) {
			log.severe("error sending email: " + e);
			mailUtils.sendMail("error generating simon sales report: " + fileId, ExceptionUtils.getStackTrace(e));
			new RuntimeException(e);
		}
		//super.executeOnComplete();		
	}
	
	public void setSalesService(SalesService salesService) {
		this.salesService = salesService;
	}
	
	public void setRefdataService(RefdataService refdataService) {
		this.refdataService = refdataService;
	}
	
}
