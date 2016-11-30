package com.esofa.crm.model.util;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.esofa.crm.model.Customer;
import com.esofa.crm.model.CustomerExtendedInfo;
import com.esofa.crm.model.CustomerMedicalInfo;
import com.esofa.crm.refdata.model.ContactPreference;
import com.esofa.crm.refdata.model.Shop;
import com.esofa.crm.refdata.model.SleepClinic;
import com.esofa.crm.refdata.model.SleepDoctor;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.crm.security.user.model.CrmUser;
import com.esofa.crm.service.CustomerService;
import com.esofa.crm.util.EsofaUtils;
import com.esofa.crm.util.PhoneUtil;
import com.googlecode.objectify.Key;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;

@Component
public class PdfMapper_PatientProgress extends PdfMapper<Long> {
	private CustomerService customerService;
	private RefdataService refdataService;
		
	
	@Override
	public void mapFields(AcroFields fields, Long itemId) throws IOException, DocumentException { 
		
		
		Key<Customer> customerKey = Key.create(Customer.class,itemId);

		if (customerKey == null ) { return; }
		
		Customer customer = customerService.getCustomerByKey(customerKey);		
		CustomerExtendedInfo custExtInfo = customerService.getCustomerExtendedByKey(customerKey);		
		CustomerMedicalInfo custMedInfo = customerService.getCustomerMedicalInfoByKey(customerKey);	

		fields.setField("Patient Name", customer.getName());
		fields.setField("Address",custExtInfo.getAddressAsOneLine());
		fields.setField("HC", customer.getFullHealthCardNumber());
		fields.setField("dob", custExtInfo.getFormattedDateOfBirth());

		
		SleepDoctor sleepDoctor = null;
		SleepClinic sleepClinic = null;
		
		if( custMedInfo != null) {
			
			if(custMedInfo.getSleepDoctor() !=null) {
				sleepDoctor = refdataService.getSleepDoctorMap().get(custMedInfo.getSleepDoctor());
			}
			
			if(custMedInfo.getClinic() !=null) {
				sleepClinic = refdataService.getSleepClinicMap().get(custMedInfo.getClinic());
			}
			
			if (sleepClinic != null) {
				fields.setField("Sleep Lab",sleepClinic.getName());
			}
			
			if (sleepDoctor !=null) {
				fields.setField("Sleep Doctor",sleepDoctor.getName());
			}
		}

		
		/**phone **/
		ContactPreference pref =null;
		if (custExtInfo != null && custExtInfo.getContactPreference() != null) {
			Key<ContactPreference> prefKey = custExtInfo.getContactPreference();			
			pref =refdataService.getContactPreferenceById(prefKey.getId());
		}
		
		String phoneToUse = PhoneUtil.getCustomerPhone(customer, pref);
		if (StringUtils.isNotEmpty(phoneToUse)) {
			fields.setField("Tel", phoneToUse.replaceAll("-", " "));
		}
		


		if (customer.getClinician() !=null) {
			CrmUser clinician = refdataService.getClinicianMap().get(customer.getClinician());

			if (clinician !=null) {
				fields.setField("Report made by",clinician.getName());
			}
		}
		
		fields.setField("contactDate",EsofaUtils.getDateFormatted(new Date()));
		
		
		
		if (customer.getPreferredLocation() !=null) {
		
			Shop s = refdataService.getShopMap().get(customer.getPreferredLocation());
			
			if (s !=null) {
				StringBuilder shopAddress=new StringBuilder();
				shopAddress.append(s.getAddress().getLine1()).append("\r\n");
				shopAddress.append(s.getAddress().getLine2()).append("\r\n");
				shopAddress.append(s.getAddress().getCity()).append(" ").append(s.getAddress().getProvince()).append("\r\n");
				shopAddress.append(s.getAddress().getPostalCode()).append("\r\n");
				shopAddress.append("P: " + s.getPhone()).append("\r\n");
				shopAddress.append("F: " + s.getFax()).append("\r\n");
				fields.setField("cpapAddress",shopAddress.toString());
			}
		}
		
		
		
	}

	
	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
		

	public void setRefdataService(RefdataService refdataService) {
		this.refdataService = refdataService;
	}	
}