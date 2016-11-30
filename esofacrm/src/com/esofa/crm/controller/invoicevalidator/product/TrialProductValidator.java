package com.esofa.crm.controller.invoicevalidator.product;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.esofa.crm.controller.util.InvoiceForm;
import com.esofa.crm.model.Product;
import com.esofa.crm.model.pos.InvoiceItem;
import com.esofa.crm.util.InvoiceUtils;
import com.esofa.crm.util.InvoiceUtils.ProductSubTypeCheckE;

public class TrialProductValidator extends AbstractInvoiceProductValidator {

	private String trialDepositBarCode;
	
	@Override
	protected boolean doValidate(InvoiceForm invoiceForm, Product p,
			BindingResult result) {

		
		if (!validatePricingTier(invoiceForm,p,result)) {
			
			return false;
		}
		
		
		if (InvoiceUtils.isProductSubType(p, ProductSubTypeCheckE.isFee)) {
			
			result.addError(new FieldError("invoiceForm", "product","In trial mode, only deposit and products are allowed to be entered"));
			return false;
		}
		;
		
		if (StringUtils.equalsIgnoreCase(p.getProductBarCode(), trialDepositBarCode)) {
			
			Long trialProductId = p.getId();
			
			//if trying to add trialDeposit, check to see if we have already used it.
			for (InvoiceItem item : invoiceForm.getInvoiceItems()) {
				
				if (item.getProductKey().getId() == trialProductId) {
					
					//error
					result.addError(new FieldError("invoiceForm", "product","In trial mode, only deposit can only be used once."));
					return false;
				}
				
			}
		}
		return true;
	}
	
	public void setTrialDepositBarCode(String trialDepositBarCode) {
		this.trialDepositBarCode = trialDepositBarCode;
	}

}
