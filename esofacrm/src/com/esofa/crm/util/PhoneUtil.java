package com.esofa.crm.util;

import org.apache.commons.lang3.StringUtils;

import com.esofa.crm.model.Customer;
import com.esofa.crm.refdata.model.ContactPreference;

public class PhoneUtil {
	
	public static String getCustomerPhone(Customer c, ContactPreference pref) {
		
		String phone=StringUtils.EMPTY;
		if (pref != null) {
			
			if (StringUtils.equalsIgnoreCase(pref.getPreference(), "Phone (Home)")) {
				phone = c.getPhoneHome();
			} else if (StringUtils.equalsIgnoreCase(pref.getPreference(), "Phone (Cell)")) {

				phone = c.getPhoneMobile();
			}
		}
		
		if (StringUtils.isEmpty(phone) && StringUtils.isNotEmpty(c.getPhoneHome())) {
			phone = c.getPhoneHome();
			
		} else if (StringUtils.isEmpty(phone) && StringUtils.isNotEmpty(c.getPhoneMobile())) {
			phone = c.getPhoneMobile();
		}
		
		return phone;
	}	

}
