package com.esofa.crm.controller.invoicevalidator.product;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.esofa.crm.controller.util.InvoiceForm;
import com.esofa.crm.model.Product;
import com.esofa.crm.model.pos.Invoice;
import com.esofa.crm.model.pos.Invoice.InvoiceTypeE;
import com.esofa.crm.model.pos.InvoiceItem;
import com.esofa.crm.service.ProductService;
import com.esofa.crm.util.InvoiceUtils;
import com.esofa.crm.util.InvoiceUtils.ProductSubTypeCheckE;
import com.esofa.crm.util.InvoiceUtils.ProductTypeCheckE;
import com.googlecode.objectify.Key;

public class DefaultProductValidator extends AbstractInvoiceProductValidator {

	private ProductService productService;
	
	@Override
	protected boolean doValidate(InvoiceForm invoiceForm, Product p,
			BindingResult result) {

		//ok to have empty serial.  if that is the case, no need to be unique.
				boolean checkSerial = false;
				checkSerial = StringUtils.isNotEmpty(invoiceForm.getSerial());
				
				//there is product
				List<InvoiceItem> items = invoiceForm.getInvoiceItems();
				Key<Product> addProductKey = Key.create(Product.class, p.getId());
				boolean addPoductIsMachineOrPackage = InvoiceUtils.isMachineOrPackage(p);					
				
				Invoice i = invoiceForm.getInvoice();
				
				//reset hasTier pricing flags in invoiceForm since it is onyl a transient field.
				invoiceForm.setHasATier1(false);
				invoiceForm.setHasATier2(false);
				
				//if standard mode, then cannot add fee (for now)
				if (InvoiceUtils.isInvoiceType(i, InvoiceTypeE.STANDARD)
						&& InvoiceUtils.isProductSubType(p, ProductSubTypeCheckE.isFee)) {
					
					result.addError(new FieldError("invoiceForm", "product","Not allowed to enter a fee type product in standard mode."));
					return false;					
				}
				
				if (InvoiceUtils.isProductType(p, ProductTypeCheckE.isNonProduct) && invoiceForm.getQty() > 1) {
					
					result.addError(new FieldError("invoiceForm", "product","for fee, credit or deposit barcodes, the quantity must be 1"));
					return false;
				}				
				
				
				for (InvoiceItem item : items) {
					
					//if same product, serial numbers must be different
					if (checkSerial && addProductKey.equals(item.getProductKey())
							&& StringUtils.equalsIgnoreCase(invoiceForm.getSerial(), item.getProductSerialNumber()) ) {
					
						result.addError(new FieldError("invoiceForm", "productSerialNumber","Serial Number is already scanned for the same product"));
						return false;
					}
					
					//if product to add is a machine or package.
					//and we already have one, then error
					Product itemProduct = productService.getProductMap().get(item.getProductKey());
					
					if (addPoductIsMachineOrPackage && InvoiceUtils.isMachineOrPackage(itemProduct)
							&& !InvoiceUtils.isInvoiceType(i, InvoiceTypeE.SALES_RETURN)) {

						result.addError(new FieldError("invoiceForm", "product","There is already a package or machine added to this invoice.  Only one machine/package allowed per invoice."));
						return false;
					}
					
						
					if (InvoiceUtils.isPriceTier1(item.getPriceTier())) {
						invoiceForm.setHasATier1(true);
					} else {
						invoiceForm.setHasATier2(true);
					}
				}
				
				
				//check new entry
				if (InvoiceUtils.isPriceTier1(invoiceForm.getPriceTier())) {
					invoiceForm.setHasATier1(true);
				} else { 
					invoiceForm.setHasATier2(true);
				}
				
				//anytime there is at least 1 tier two, there is special pricing.
				invoiceForm.getInvoice().setSpecialPricing(invoiceForm.getHasATier2());
				
				//check for serial number and humidifier number
				if (InvoiceUtils.isInvoiceType(i, InvoiceTypeE.STANDARD)) {
					if(addPoductIsMachineOrPackage) {
						
						if (StringUtils.isEmpty(invoiceForm.getSerial())) {
							
							result.addError(new FieldError("invoiceForm", "serial","Product Serial number is required for package or machine"));
							
						} 
						
						if (StringUtils.isEmpty(invoiceForm.getAdditionalSerial())) {
							
							result.addError(new FieldError("invoiceForm", "additionalSerial","Humidifier Serial is required for package or machine"));
							
						}
						
					}
				}
				//fails validation if there are errors
				return !result.hasErrors();

	}

	
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
	
}
