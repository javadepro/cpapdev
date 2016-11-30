package com.esofa.crm.model.util;

import java.io.IOException;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.esofa.crm.model.Customer;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.crm.security.user.model.CrmUser;
import com.esofa.crm.service.CustomerService;
import com.esofa.crm.util.EsofaUtils;
import com.googlecode.objectify.Key;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;

@Component
public class PdfMapper_BasicSignature extends PdfMapper<Long> {
	protected CustomerService customerService;
	protected RefdataService refdataService;
		
	
	@Override
	public void mapFields(AcroFields fields, Long itemId) throws IOException, DocumentException { 
		
		
		Key<Customer> customerKey = Key.create(Customer.class,itemId);

		if (customerKey == null ) { return; }		
		Customer customer = customerService.getCustomerByKey(customerKey);		
		fields.setField("patientName", customer.getName());
		

		if (customer.getClinician() !=null) {
			CrmUser clinician = refdataService.getClinicianMap().get(customer.getClinician());

			if (clinician !=null) {
				fields.setField("clinician",clinician.getName());
			}
		}
		
		fields.setField("today",EsofaUtils.getDateFormatted(new Date()));

	}

	
	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
		

	public void setRefdataService(RefdataService refdataService) {
		this.refdataService = refdataService;
	}	
}