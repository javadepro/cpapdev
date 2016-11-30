package com.esofa.crm.model.util.sleepmed;

import java.io.IOException;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.esofa.crm.model.Customer;
import com.esofa.crm.model.CustomerExtendedInfo;
import com.esofa.crm.model.CustomerMedicalInfo;
import com.esofa.crm.model.util.PdfMapper;
import com.esofa.crm.refdata.model.ContactPreference;
import com.esofa.crm.refdata.model.Dentist;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.crm.service.CustomerService;
import com.esofa.crm.util.EsofaUtils;
import com.esofa.crm.util.PhoneUtil;
import com.googlecode.objectify.Key;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;

@Component
public class PdfMapper_HmSleepTestAgreement extends PdfMapper<Long> {
	private CustomerService customerService;
	private RefdataService refdataService;
		
	
	@Override
	public void mapFields(AcroFields fields, Long itemId) throws IOException, DocumentException { 
		
		
		Key<Customer> customerKey = Key.create(Customer.class,itemId);

		if (customerKey == null ) { return; }		
		Customer customer = customerService.getCustomerByKey(customerKey);	
		CustomerExtendedInfo extInfo =  customerService.getCustomerExtendedByKey(customerKey);
		CustomerMedicalInfo medInfo = customerService.getCustomerMedicalInfoByKey(customerKey);
		ContactPreference contactPref = refdataService.getContactPreferenceMap().get(extInfo.getContactPreference());		
		
						
		fields.setField("patientName", customer.getName());		
		fields.setField("patientPhone", PhoneUtil.getCustomerPhone(customer, contactPref));

		if (extInfo !=null) {
			
			fields.setField("dob",EsofaUtils.getDateFormatted(extInfo.getDateOfBirth()));
			fields.setField("patientAddr", extInfo.getAddressAsOneLine());
		}
		
		if (medInfo != null) {
			Dentist d = refdataService.getDentistMap().get(medInfo.getDentist());
			
			if (d != null) {
				fields.setField("dentist", d.getName());
			}
		}
		
		String today = EsofaUtils.getDateFormatted(new Date());
		fields.setField("checkOutDate",today);
		fields.setField("clinicianDate", today);

	}

	
	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
		

	public void setRefdataService(RefdataService refdataService) {
		this.refdataService = refdataService;
	}	
}