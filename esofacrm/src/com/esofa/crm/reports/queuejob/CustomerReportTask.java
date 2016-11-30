package com.esofa.crm.reports.queuejob;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.SequenceInputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.esofa.crm.common.model.Address;
import com.esofa.crm.model.Customer;
import com.esofa.crm.model.CustomerExtendedInfo;
import com.esofa.crm.model.CustomerInsuranceInfo;
import com.esofa.crm.queuejob.DataDumpReportTask;
import com.esofa.crm.refdata.model.AppointmentPreference;
import com.esofa.crm.refdata.model.ContactPreference;
import com.esofa.crm.refdata.model.FundingOption;
import com.esofa.crm.refdata.model.Shop;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.crm.reports.MapToCsvWriter;
import com.esofa.crm.security.user.model.CrmUser;
import com.esofa.crm.service.CustomerService;
import com.google.api.services.drive.model.File;
import com.googlecode.objectify.Key;

public class CustomerReportTask extends DataDumpReportTask<Map<String,String>> {

	private static final String separator = "_";
	
	private static final String PARAM_CURSOR="cursor";
	private static final String PARAM_FILENAME="fileName";
	private static final String PARAM_COUNTER = "iterCount";
	
	private CustomerService customerService;
	private RefdataService refdataService;
	
	private Map<Key<CrmUser>,CrmUser> clinicianMap;
	private Map<Key<Shop>,Shop> shopMap;

	private Map<Key<FundingOption>,FundingOption> fundingOptionMap ;
	private Map<Key<AppointmentPreference>,AppointmentPreference>apptPreferenceMap;
	private Map<Key<ContactPreference>,ContactPreference> contactPreferenceMap;
	private int pageSize=250;
	
	public CustomerReportTask() throws IOException {
		super();
	}

	@Override
	protected void initTask(Map<String, String> params) {
		
		Date d = new Date();
		String fileName = "CustomerReport_" + String.format("%tY-%tm-%td-%tH:%tM:%tS", d,d,d,d,d,d) + ".csv" + separator;
		params.put(PARAM_FILENAME,fileName);
		
		
	}

	@Override
	protected Map<String, String> doTask(Map<String, String> params) {
		
		initReferenceMaps();
		String startCursor = params.get(PARAM_CURSOR);
		List<Customer> customerList = new ArrayList<>(pageSize);
		boolean atLeast1=false;
		String counter = incrementCounter(params);
		
		String newCursor= customerService.getCustomersByQueryCursor(startCursor, customerList,pageSize);
		String fileName = getBaseFileName(params.get(PARAM_FILENAME)) + "_" + counter;

		List<Map<String,String>> resultList = new ArrayList<>(pageSize);

		
		for (Customer c : customerList) {
			Map<String,String> row = getRow(c);
			resultList.add(row);
			
			atLeast1=true;
		}
		
		if (atLeast1) {

			write(params,resultList,fileName, StringUtils.equalsIgnoreCase(counter, "0"));
		} else {
			params.put("END","END");
		}
		
		//update params for passback
		params.put(PARAM_CURSOR, newCursor);
		params.put(PARAM_FILENAME,fileName);
		
		return params;
	}
	
	
	private String getBaseFileName(String fileName) {
		
		String fullFileName = fileName;
		return StringUtils.substringBeforeLast(fullFileName, separator);
	}
	
	protected void write(Map<String,String> params, List<Map<String,String>> resultList, String fileName, boolean firstEntry) {
			MapToCsvWriter mapToCsvWriter = new MapToCsvWriter();

			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				Writer w = new OutputStreamWriter(new BufferedOutputStream(baos)); 
				
				mapToCsvWriter.setHeader(getHeader()).setMapping(mapping).setIncludeHeader(firstEntry).setWriter(w).write(resultList);

				
				InputStream resultsInputStream = new ByteArrayInputStream(baos.toByteArray());
				
				String fileId = tempFolder.addOrUpdateFile(resultsInputStream, "text/csv", fileName);
				log.severe("file has been saved: " + fileName + " " + fileId);
			} catch (IOException e) {
				new RuntimeException(e);
			}
		
	}

	protected String incrementCounter(Map<String,String> params) {
		
		String str_counter = params.get(PARAM_COUNTER);
		
		if (StringUtils.isEmpty(str_counter)) {
			str_counter = "0";
		} else {

			int int_counter = Integer.valueOf(str_counter).intValue();
			int_counter = int_counter+1;
			str_counter = String.valueOf(int_counter);
		}
		
		params.put(PARAM_COUNTER,str_counter);
		
		return str_counter;
	}

	@Override
	protected void executeOnComplete(Map<String, String> params) {
		
		String lastFileName = params.get(PARAM_FILENAME);
		String baseFileName = getBaseFileName(lastFileName);

		try {
		List<File> files = tempFolder.getFiles(baseFileName,separator);
		
		
		List<InputStream> streams = new ArrayList<>();
		
		for (File f : files) {
			
			InputStream s = tempFolder.getFileAsInputStream(f);
			streams.add(s);
		}

		SequenceInputStream resultsInputStream = new SequenceInputStream(Collections.enumeration(streams));
			
			String fileId = dataDumpFolder.addOrUpdateFile(resultsInputStream, "text/csv", baseFileName);
			log.severe("file has been saved: " + baseFileName + " " + fileId  + " " + streams.size());
						
		} catch (IOException e) {
			new RuntimeException(e);
		} finally {
			try {
				tempFolder.clearFolder(baseFileName, separator);
			} catch (IOException e) {
				log.severe(ExceptionUtils.getStackTrace(e));
			}
		}
	}
	
	
	private Map<String,String> getRow(Customer c) {
		
		Map<String,String> row = new HashMap<String,String>();
		
		row.put("Name", c.getName());
		row.put("Last Name", c.getLastname());
		row.put("First Name", c.getFirstname());
		row.put("Last Updated", c.getFormattedLastUpdated());
		row.put("Health Card Number", c.getFormattedHealthCardNumber());
		row.put("Home Phone", c.getPhoneHome());
		row.put("Office Phone", c.getPhoneOffice());
		row.put("Office Extension", c.getPhoneOfficeExt());
		row.put("Mobile Phone", c.getPhoneMobile());
		
		if(c.getClinician() != null) {
			CrmUser clinician = clinicianMap.get(c.getClinician());
			if(clinician != null) {
				row.put("Clinician Name", clinician.getName());
				row.put("Clinician Last Name", clinician.getLastname());
				row.put("Clinician First Name", clinician.getFirstname());
			}
		}
		
		if(c.getPreferredLocation() != null) {
			Shop shop = shopMap.get(c.getPreferredLocation());	
			if (shop != null ) {
				row.put("Preferred Location", shop.getName());
			}
		}
		
		CustomerExtendedInfo cei = customerService.getCustomerExtendedById(c.getId());
		if( cei != null ) populateExtendedInfo(cei, row);
		
		CustomerInsuranceInfo cii = customerService.getCustomerInsuranceInfoById(c.getId());
		if( cii != null ) populateInsuranceInfo(cii, row);
		
		return row;
	}
	

	private void populateExtendedInfo(CustomerExtendedInfo cei, Map<String, String> map) {		
		map.put("Gender",cei.getGender());
		if( cei.getDateOfBirth() != null ) {
			
			String dob = String.format("%td/%tm/%tY", cei.getDateOfBirth(),cei.getDateOfBirth(),cei.getDateOfBirth());
			map.put("DOB",dob);
		}
		Address addr = cei.getAddress();
		if( addr != null ) {
			map.put("Address 1", addr.getLine1());
			map.put("Address 2", addr.getLine2());
			map.put("City", addr.getCity());
			map.put("Province", addr.getProvince());
			map.put("Postal Code", addr.getPostalCode());
			map.put("Country", addr.getCountry());
		}
				
		
		ContactPreference contactPreference = contactPreferenceMap.get(cei.getContactPreference());
		if( contactPreference != null ) {
			map.put("Contact Preference", contactPreference.getPreference());
		}
		

		
		AppointmentPreference apptPreference = apptPreferenceMap.get(cei.getAppointmentPreference());
		if( apptPreference != null ) {
			map.put("Appointment Preference", apptPreference.getPreference());
		}
		
		map.put("Email", cei.getEmail());
		map.put("Language Preference", cei.getLanguage());
		map.put("Consent Phone", Boolean.toString(cei.getConsentPhone()));
		map.put("Consent Email", Boolean.toString(cei.getConsentEmail()));
		map.put("Consent Mail", Boolean.toString(cei.getConsentMail()));
	}
	
	private void populateInsuranceInfo(CustomerInsuranceInfo cii, Map<String, String> map) {

		if( cii.getFundingOptionGovernment() != null ) {
			FundingOption govFundingOption = fundingOptionMap.get(cii.getFundingOptionGovernment());
			if( govFundingOption != null ) {
				map.put("Gov Funding", govFundingOption.getOption());
			}
		}
		if( cii.getFundingOptionInsurance() != null ) {
			FundingOption insuranceFundingOption = fundingOptionMap.get(cii.getFundingOptionInsurance());
			if( insuranceFundingOption != null ) {
				map.put("Insurance Funding", insuranceFundingOption.getOption());
			}
		}
	}
	
	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public void setRefdataService(RefdataService refdataService) {
		this.refdataService = refdataService;
	}
	
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	private void initReferenceMaps() {
		
		//if one map is null, assume all are.
				
		if (shopMap == null) { 
			
			shopMap = refdataService.getShopMap();
			fundingOptionMap= refdataService.getFundingOptionMap();
			apptPreferenceMap = refdataService.getAppointmentPreferenceMap();
			contactPreferenceMap= refdataService.getContactPreferenceMap();
			clinicianMap = refdataService.getClinicianMap();
		}
		
		
	}
	
}
