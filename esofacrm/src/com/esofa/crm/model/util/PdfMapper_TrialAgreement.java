package com.esofa.crm.model.util;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.esofa.crm.model.Customer;
import com.esofa.crm.model.CustomerExtendedInfo;
import com.esofa.crm.refdata.model.ContactPreference;
import com.esofa.crm.refdata.model.Shop;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.crm.security.user.model.CrmUser;
import com.esofa.crm.service.CustomerService;
import com.esofa.crm.util.PhoneUtil;
import com.googlecode.objectify.Key;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;

@Component
public class PdfMapper_TrialAgreement extends PdfMapper<Long> {
	private CustomerService customerService;
	private RefdataService refdataService;
		
	
	@Override
	public void mapFields(AcroFields fields, Long itemId) throws IOException, DocumentException { 
		
		
		Key<Customer> customerKey = Key.create(Customer.class,itemId);

		if (customerKey == null ) { return; }
		
		Customer customer = customerService.getCustomerByKey(customerKey);		
		CustomerExtendedInfo custExtInfo = customerService.getCustomerExtendedByKey(customerKey);		

		fields.setField("Name", customer.getName());
		fields.setField("dob", custExtInfo.getFormattedDateOfBirth());
		

		
		/**phone **/
		ContactPreference pref =null;
		if (custExtInfo != null && custExtInfo.getContactPreference() != null) {
			Key<ContactPreference> prefKey = custExtInfo.getContactPreference();			
			pref =refdataService.getContactPreferenceById(prefKey.getId());
		}
		
		String phoneToUse = PhoneUtil.getCustomerPhone(customer, pref);
		if (StringUtils.isNotEmpty(phoneToUse)) {
			fields.setField("Phone", phoneToUse.replaceAll("-", " "));
		}
		


		if (customer.getClinician() !=null) {
			CrmUser clinician = refdataService.getClinicianMap().get(customer.getClinician());

			if (clinician !=null) {
				fields.setField("Report made by",clinician.getName());
			}
		}
		
		
		
		
		if (customer.getPreferredLocation() !=null) {
		
			Shop s = refdataService.getShopMap().get(customer.getPreferredLocation());
			
			if (s !=null) {
				StringBuilder shopAddress=new StringBuilder();
				shopAddress.append(s.getAddress().getCity()).append(", ").append(s.getAddress().getProvince()).append(", ");
				shopAddress.append(s.getAddress().getPostalCode());
				fields.setField("shopAddr1",s.getAddress().getLine1());
				fields.setField("shopAddr2",s.getAddress().getLine2());
				fields.setField("shopAddr3",shopAddress.toString());
				fields.setField("shopPhone",s.getPhone());
				fields.setField("shopFax",s.getFax());
				
				
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