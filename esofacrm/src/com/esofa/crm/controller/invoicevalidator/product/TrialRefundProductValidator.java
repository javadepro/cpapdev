package com.esofa.crm.controller.invoicevalidator.product;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.esofa.crm.controller.util.InvoiceForm;
import com.esofa.crm.model.Product;
import com.esofa.crm.util.InvoiceUtils;
import com.esofa.crm.util.InvoiceUtils.ProductSubTypeCheckE;

public class TrialRefundProductValidator extends
		AbstractInvoiceProductValidator {
	
	@Override
	protected boolean doValidate(InvoiceForm invoiceForm, Product p,
			BindingResult result) {

		if (!validatePricingTier(invoiceForm,p,result)) {
			
			return false;
		}
		
		if (!InvoiceUtils.isProductSubType(p, ProductSubTypeCheckE.allowInTrialRefundInvoice)) {
		
			result.addError(new FieldError("invoiceForm","invoiceType"," trial refund should only have trial credits and trial fees"));
			return false;
		}
				
		return true;
	}

}
