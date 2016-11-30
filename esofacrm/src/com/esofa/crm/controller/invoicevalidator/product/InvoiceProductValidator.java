package com.esofa.crm.controller.invoicevalidator.product;

import org.springframework.validation.BindingResult;

import com.esofa.crm.controller.util.InvoiceForm;
import com.esofa.crm.model.Product;

public interface InvoiceProductValidator {

	
	public boolean validateProduct(InvoiceForm invoiceForm, Product p, BindingResult result);
	
}
