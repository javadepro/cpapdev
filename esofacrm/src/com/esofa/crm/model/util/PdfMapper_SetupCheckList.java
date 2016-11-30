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
public class PdfMapper_SetupCheckList extends PdfMapper_SetupNotificationBase {
	private CustomerService customerService;
	private RefdataService refdataService;
		
	
	@Override
	public void mapFields(AcroFields fields, Long itemId) throws IOException, DocumentException { 
		Key<Customer> customerKey = Key.create(Customer.class,itemId);

		if (customerKey == null ) { return; }
		
		Customer customer = customerService.getCustomerByKey(customerKey);		
		CustomerExtendedInfo custExtInfo = customerService.getCustomerExtendedByKey(customerKey);		

		fields.setField("Name", customer.getName());
		fields.setField("page2ClientName", customer.getName());
		fields.setField("page3ClientName", customer.getName());
		

		if (custExtInfo != null && custExtInfo.getAddress() !=null) {
			fields.setField("clientAddr1",custExtInfo.getAddress().getLine1());
			fields.setField("clientAddr2",custExtInfo.getAddress().getLine2());
			fields.setField("clientAddr3",custExtInfo.getAddress().getCity() +  " " + custExtInfo.getAddress().getProvince() + " " + custExtInfo.getAddress().getCountry() + " " + custExtInfo.getAddress().getPostalCode());
		}
		
		/**phone **/
		ContactPreference pref =null;
		if (custExtInfo != null && custExtInfo.getContactPreference() != null) {
			Key<ContactPreference> prefKey = custExtInfo.getContactPreference();			
			pref =refdataService.getContactPreferenceById(prefKey.getId());
		}
		
		String phoneToUse = PhoneUtil.getCustomerPhone(customer, pref);
		if (StringUtils.isNotEmpty(phoneToUse)) {
			fields.setField("pg3ClientHomeTel", phoneToUse.replaceAll("-", " "));
		}
		
		fields.setField("pg3ClientWkTel", customer.getPhoneOffice() + " " + customer.getPhoneOfficeExt());
		
		String today = EsofaUtils.getDateFormatted(new Date());
		fields.setField("setupDate",today);
		fields.setField("clientDate1",today);
		fields.setField("witnessDate1",today);
		fields.setField("clientDate2",today);
		fields.setField("witnessDate2",today);		
		fields.setField("page3Date",today);
		

		Shop s = refdataService.getShop(customer.getPreferredLocation());
		
		if (s !=null) {
			
			fields.setField("witnessAddr1",s.getAddress().getLine1());
			fields.setField("witnessAddr2",s.getAddress().getLine2());
			fields.setField("witnessAddr3",s.getAddress().getCity() +  " " + s.getAddress().getProvince() + " " + s.getAddress().getPostalCode());
			fields.setField("witnessWkTel",s.getPhone());
		}
		
	}

	
	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
		

	public void setRefdataService(RefdataService refdataService) {
		this.refdataService = refdataService;
	}	
}