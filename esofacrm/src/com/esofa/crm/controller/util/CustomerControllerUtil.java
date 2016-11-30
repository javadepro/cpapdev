package com.esofa.crm.controller.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.esofa.crm.model.CustomerWrapper;

public class CustomerControllerUtil {

	private static Map<Boolean, String> yesNo = new HashMap<Boolean, String>();
	
	static {
		
		yesNo.put(true, "Yes");
		yesNo.put(false, "No");
	}
	
	public static void verifyEmailConsent(CustomerWrapper cw, BindingResult result) {
		
		if (cw.getCustomerExtendedInfo() != null && 
				cw.getCustomerExtendedInfo().getConsentEmail() && StringUtils.isEmpty(cw.getCustomerExtendedInfo().getEmail())) {
			
			result.addError(new FieldError("email","customerWrapper","email is required if consent emailing customer is provided"));
			
		}
	}
	
	public static  Map<Boolean, String> yesNo() {
		
		return yesNo;
	}
}
