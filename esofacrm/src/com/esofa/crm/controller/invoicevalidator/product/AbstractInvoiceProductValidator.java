package com.esofa.crm.controller.invoicevalidator.product;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.esofa.crm.controller.util.InvoiceForm;
import com.esofa.crm.model.Product;
import com.esofa.crm.model.pos.Invoice;
import com.esofa.crm.util.InvoiceUtils;

public abstract class AbstractInvoiceProductValidator implements InvoiceProductValidator {

	private Invoice.InvoiceTypeE invoiceType; 
	
	@Override
	public boolean validateProduct(InvoiceForm invoiceForm, Product p,
			BindingResult result) {
	
		if ( invoiceType != null && !InvoiceUtils.isInvoiceType(invoiceForm.getInvoice(), invoiceType)) {
			return true;
		}
		
		return doValidate(invoiceForm,p,result);
	}
	
	abstract protected boolean doValidate(InvoiceForm invoiceForm, Product p, BindingResult result);

	
	protected boolean validatePricingTier(InvoiceForm invoiceForm, Product p,
			BindingResult result) {
		if (!InvoiceUtils.isPriceTier1(invoiceForm.getPriceTier())) {
			
			result.addError(new FieldError("invoiceForm","priceTier","Only standard mode supports tier 2 pricing."));
			return false;
		}
		return true;
	}
	
	public void setInvoiceType(Invoice.InvoiceTypeE invoiceType) {
		this.invoiceType = invoiceType;
	}
}
