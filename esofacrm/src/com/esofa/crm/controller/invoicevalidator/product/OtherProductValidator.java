package com.esofa.crm.controller.invoicevalidator.product;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.esofa.crm.controller.util.InvoiceForm;
import com.esofa.crm.model.Product;
import com.esofa.crm.util.InvoiceUtils;
import com.esofa.crm.util.InvoiceUtils.ProductSubTypeCheckE;

public class OtherProductValidator extends AbstractInvoiceProductValidator {

	@Override
	protected boolean doValidate(InvoiceForm invoiceForm, Product p,
			BindingResult result) {

		if (!validatePricingTier(invoiceForm,p,result)) {
			
			return false;
		}
		
		
		//if other mode, then only can add fee
		if (!InvoiceUtils.isProductSubType(p, ProductSubTypeCheckE.isFee)) {
			
			result.addError(new FieldError("invoiceForm", "product","Other mode only supports products of type fee."));
			return false;
			
		}
		
		return true;
	}

}
