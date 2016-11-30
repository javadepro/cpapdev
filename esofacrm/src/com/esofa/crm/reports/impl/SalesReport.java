package com.esofa.crm.reports.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.esofa.crm.model.Customer;
import com.esofa.crm.model.CustomerMedicalInfo;
import com.esofa.crm.model.pos.Invoice;
import com.esofa.crm.model.report.DateRangeReportReq;
import com.esofa.crm.refdata.model.DiscountReason;
import com.esofa.crm.refdata.model.Doctor;
import com.esofa.crm.refdata.model.FamilyDoctor;
import com.esofa.crm.refdata.model.SleepDoctor;
import com.esofa.crm.service.CustomerService;
import com.esofa.crm.service.SalesService;
import com.googlecode.objectify.Key;

public class SalesReport extends BaseCrmReport<DateRangeReportReq> {

	private SalesService salesService;
	private CustomerService customerService;
	
	@Override
	protected List<Map<String,String>> getData(DateRangeReportReq req) {
		Date fromDate = req.getFromDate();
		Date endDate = req.getEndDate();
		
		List<Invoice> rawResults = salesService.getInvoices(fromDate, endDate,null,true);
		
		List<Map<String,String>> resultList = new ArrayList<Map<String,String>>();
		
		Map<Key<DiscountReason>, DiscountReason> discountReasonMap = refdataService.getDiscountReasonMap();
		Map<Key<SleepDoctor>,SleepDoctor> sleepDocMap = refdataService.getSleepDoctorMap();
		Map<Key<FamilyDoctor>,FamilyDoctor> familyDocMap =refdataService.getFamilyDoctorMap();
		Map<Key<Customer>,CustomerMedicalInfo> cmiMap =getCustomerMedicalInfoMap(rawResults);
		
		for (Invoice i : rawResults) {
		
			Map<String,String> row = new HashMap<String,String>();
			
			row.put("Create Timestamp", String.format("%1$tc", i.getInsertDateTime()));
			
			populateDiscountReason(row,i,discountReasonMap);		
			populateDoctorInfo(row,i,sleepDocMap,familyDocMap,cmiMap);
		
			
			populate(row,i);
			resultList.add(row);
		}
		
		return resultList;
	}
	
	private void populateDiscountReason(Map<String,String> row , Invoice i, Map<Key<DiscountReason>, DiscountReason> discountReasonMap ){
		
		Key<DiscountReason> discountReasonKey = i.getDiscountReason();
		if( discountReasonKey != null ) {
			DiscountReason discountReason = discountReasonMap.get(discountReasonKey);
			if( discountReason != null ) {
				row.put("Discount Reason", discountReason.getReason());
			}
		}
	}
	
	
	private void populateDoctorInfo(Map<String,String> row , Invoice i,
			Map<Key<SleepDoctor>,SleepDoctor> sleepDocMap, Map<Key<FamilyDoctor>,FamilyDoctor> familyDocMap,
			Map<Key<Customer>,CustomerMedicalInfo> cmiMap) {
		
		if (i.getCustomerKey() != null) {
			
			CustomerMedicalInfo cmi = cmiMap.get(i.getCustomerKey());			
			
			if (cmi !=null) {
				row.put("Referred By Type",cmi.getReferredBy());
				Doctor doc = null;
				
				if (StringUtils.equalsIgnoreCase(cmi.getReferredBy(),"Family Doctor")) {
					
					Key<FamilyDoctor> docKey = cmi.getFamilyDoctor();
					
					if (docKey !=null) {
						doc = familyDocMap.get(docKey);
					}
				} else if  (StringUtils.equalsIgnoreCase(cmi.getReferredBy(),"Sleep Doctor")) {
					
					Key<SleepDoctor> docKey = cmi.getSleepDoctor();
					
					if (docKey !=null) {
						doc = sleepDocMap.get(docKey);
					}
				}
				
				if (doc !=null) {
					row.put("Referred Doctor First Name", doc.getFirstName());
					row.put("Referred Doctor Last Name", doc.getLastName());
				}
			}
			
		}
	}
	
	private Map<Key<Customer>,CustomerMedicalInfo> getCustomerMedicalInfoMap(List<Invoice> invoices) {
	
		Set<Key<Customer>> customerKeys = new HashSet<Key<Customer>>(); 
		
		for (Invoice i: invoices) {
			
			Key<Customer> c = i.getCustomerKey();
			if (c !=null) {
				customerKeys.add(c);
			}
		}
		
		return customerService.getCustomerMedicalInfoByKey(customerKeys);
	}

	
	public void setSalesService(SalesService salesService) {
		this.salesService = salesService;
	}
	

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
}

