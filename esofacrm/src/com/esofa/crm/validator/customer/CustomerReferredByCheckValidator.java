package com.esofa.crm.validator.customer;

import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import com.esofa.crm.model.CustomerMedicalInfo;
import com.esofa.crm.validator.ApplicationContextAwareConstraintValidator;

public class CustomerReferredByCheckValidator extends ApplicationContextAwareConstraintValidator<CustomerReferredByCheck,CustomerMedicalInfo> {

	
	public void initialize(CustomerReferredByCheck constraintAnnotation) {
	}

	public boolean isValid(CustomerMedicalInfo cmi, ConstraintValidatorContext context) {
		
		String errorMsg = null;
		
		//referral is blank but sleep doc and family doc is not.
		if (StringUtils.isBlank(cmi.getReferredBy())) {
			if (cmi.getFamilyDoctor() != null && cmi.getSleepDoctor() != null) {
				errorMsg = "{customer.medical.referredBy.notfilled}";
			}
		} else {
			//referral filled. make sure the corresponding type is also filled in.
			
			if (StringUtils.equalsIgnoreCase("Family Doctor", cmi.getReferredBy()) && cmi.getFamilyDoctor() == null ) {
				
				errorMsg = "{customer.medical.familyDoctor.blank}";
				
			} else if (StringUtils.equalsIgnoreCase("Sleep Doctor", cmi.getReferredBy()) && cmi.getSleepDoctor() == null) {
				
				errorMsg = "{customer.medical.sleepDoctor.blank}";
			} 
		}	
	 
	 if(errorMsg != null) {
		 context.disableDefaultConstraintViolation();
		 context.buildConstraintViolationWithTemplate( errorMsg  ).addConstraintViolation();
        }
		
		 //if error message is null, then must be valid.
		return errorMsg == null;
	}

}
