package com.esofa.crm.reports.queuejob;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.esofa.crm.model.Customer;
import com.esofa.crm.model.CustomerMedicalInfo;
import com.esofa.crm.queuejob.AbstractEmailReportTask;
import com.esofa.crm.refdata.model.Doctor;
import com.esofa.crm.refdata.model.FamilyDoctor;
import com.esofa.crm.refdata.model.Shop;
import com.esofa.crm.refdata.model.SleepClinic;
import com.esofa.crm.refdata.model.SleepDoctor;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.crm.reports.MapToCsvWriter;
import com.esofa.crm.service.CustomerService;
import com.esofa.crm.util.EsofaUtils;
import com.googlecode.objectify.Key;

public class SimonReferralReport extends AbstractEmailReportTask<Map<String,String>> {

	private static final String FROM_DATE = "FROM";
	private static final String END_DATE = "END";
	
	private CustomerService customerService;
	private RefdataService refdataService;
	
	private List<Map<String,String>> resultList;
		
	protected Date fromDate = null;
	protected Date endDate = null;
	
	
	protected Iterator<CustomerMedicalInfo> cmiIter;
	protected Map<Key<Customer>, Customer> customers ;
	protected Map<Key<Shop>,Shop> shopMap;
	protected Map<Key<SleepDoctor>,SleepDoctor> sleepDoctorMap;
	protected Map<Key<FamilyDoctor>, FamilyDoctor> familyDoctorMap;
	protected Map<Key<SleepClinic>, SleepClinic> sleepClinicMap;

	public SimonReferralReport() throws IOException {
		super();
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
		
		List<CustomerMedicalInfo> cmiList = customerService.getMedicalInfoByRefrralDate(fromDate, endDate);
		List<Key<Customer>> customerKeys = new  ArrayList<>();
		
		for (CustomerMedicalInfo medInfo : cmiList) {
			
			customerKeys.add(medInfo.getCustomer());
		}
				
		cmiIter = cmiList.iterator();
		customers = customerService.getCustomersByKey(customerKeys);
		shopMap = refdataService.getShopMap();
		sleepDoctorMap = refdataService.getSleepDoctorMap();
		familyDoctorMap = refdataService.getFamilyDoctorMap();
		sleepClinicMap = refdataService.getSleepClinicMap();
		resultList = new ArrayList<Map<String,String>>();
	}

	@Override
	protected Map<String, String> doTask(Map<String, String> params) {
		
		if (cmiIter == null || !cmiIter.hasNext()) {
			return null;
		}
		
		CustomerMedicalInfo cmi = cmiIter.next();
		
		Map<String,String> row = new HashMap<String,String>();
		Customer cust = customers.get(cmi.getCustomer());

		Doctor doc=null;
		String referralType=StringUtils.EMPTY;
		if (cmi.getSleepDoctor() != null) {
			doc = sleepDoctorMap.get(cmi.getSleepDoctor());
			referralType=cmi.getReferredBy();
			
		}
		
		if (doc == null && cmi.getFamilyDoctor() != null) {
			doc = familyDoctorMap.get(cmi.getFamilyDoctor());
			referralType=cmi.getReferredBy();
		}
		
		if(doc != null) {
			row.put("Doctor First Name", doc.getFirstName());		
			row.put("Doctor Last Name", doc.getLastName());
			row.put("Referred By",referralType);
			
		}
		
		SleepClinic sc = null;
		
		if (cmi.getClinic() !=null) {
			sc = sleepClinicMap.get(cmi.getClinic());
				
			if (sc !=null) {
				row.put("Sleep Clinic",sc.getName());
			}
		}
		
		if (cust != null) {
			row.put("Customer Name",cust.getName());
		}
		
		Key<Shop> prefLocationKey = cust.getPreferredLocation();
		String shopName=StringUtils.EMPTY;
		if (prefLocationKey !=null) {
			Shop s =shopMap.get(prefLocationKey);
			if (s != null) {
				shopName = s.getShortName();
			}
		}
		row.put("Location", shopName);
		resultList.add(row);
		
		return params;
	}
	
	@Override
	protected void executeOnComplete(Map<String, String> params) {

		MapToCsvWriter mapToCsvWriter = new MapToCsvWriter();
		String formattedDate = EsofaUtils.getDateFormatted(fromDate);
		String fileId = "SimonReferralReport_" + formattedDate+".csv";
		String toEmail = configService.getConfigStringByName(configToEmailAddrKey);
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Writer w = new OutputStreamWriter(new BufferedOutputStream(baos)); 
			
			mapToCsvWriter.setHeader(getHeader()).setMapping(mapping).setWriter(w).write(resultList);
			 InputStream resultsInputStream = new ByteArrayInputStream(baos.toByteArray());
				
			mailUtils.sendMail(toEmail, fileId, "Referral Report " + formattedDate,resultsInputStream ,fileId,"text/csv");
			
		} catch (IOException e) {
			log.severe("unable to generate simon's referral report: " + fileId);
			mailUtils.sendMail(toEmail,"error genearting simon's referral report " + fileId, ExceptionUtils.getStackTrace(e));
			new RuntimeException(e);
		}
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public void setRefdataService(RefdataService refdataService) {
		this.refdataService = refdataService;
	}
	
}
