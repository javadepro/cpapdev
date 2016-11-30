package com.esofa.crm.controller.invoicevalidator.product;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.esofa.crm.controller.util.InvoiceControllerUtil;
import com.esofa.crm.controller.util.InvoiceForm;
import com.esofa.crm.controller.util.SalesReturnUtils;
import com.esofa.crm.model.Product;
import com.esofa.crm.model.pos.Invoice;
import com.esofa.crm.model.pos.InvoiceItem;
import com.esofa.crm.service.ProductService;
import com.esofa.crm.service.SalesService;
import com.esofa.crm.util.InvoiceUtils;
import com.esofa.crm.util.InvoiceUtils.ProductTypeCheckE;
import com.esofa.crm.util.MathUtils;
import com.googlecode.objectify.Key;


public class SalesReturnProductValidator extends AbstractInvoiceProductValidator {

	private SalesService salesService;
	private ProductService productService;
	
	@Override
	protected boolean doValidate(InvoiceForm invoiceForm, Product p, BindingResult result) {
			
		
		if (StringUtils.isEmpty(invoiceForm.getInvoice().getReferencedInvoiceNumber())) {
			result.addError(new FieldError("invoiceForm","referencedInvoiceNumber","referenced invoice number cannot be null"));
			return false;
		}
		
		//ensure reference invoice exists
		Invoice refInvoice = salesService.getInvoice(invoiceForm.getInvoice().getReferencedInvoiceNumber());
		if (refInvoice == null) {

			result.addError(new FieldError("invoiceForm","referencedInvoiceNumber","can not find invoice for referenced invoice number"));
			return false;
		} else if  (!(InvoiceUtils.isInvoiceType(refInvoice, Invoice.InvoiceTypeE.STANDARD)
			|| InvoiceUtils.isInvoiceType(refInvoice, Invoice.InvoiceTypeE.OTHER))) {
				
			result.addError(new FieldError("invoiceForm","referencedInvoiceNumber","referenced invoice type not valid for return"));
			return false;			
		}
		
		//ensure product is in referenced invoiced and has qty avail.
		List<InvoiceItem> refItems = salesService.getInvoiceItems(Key.create(Invoice.class,refInvoice.getId()));		
		List<InvoiceItem> currentItems = invoiceForm.getInvoiceItems();
		String refPriceTier = InvoiceItem.ItemPriceTier.TIER_1.toString();

		//check to make sure that the newly added item is in the list of originals
		if (!InvoiceUtils.isProductType(p, ProductTypeCheckE.isNonProduct)) {
			
			boolean doesContain=false;
			for (InvoiceItem refItem: refItems) {
				Long refItemKey = Long.valueOf(refItem.getProductKey().getId());
				
				if (p.getId().equals(refItemKey)) {
					doesContain=true;
					refPriceTier = refItem.getPriceTier();
					break;
				}
			}
			if (!doesContain) {
				result.addError(new FieldError("invoiceForm","product","product being added was not from original invoice"));
				return false;
			}
			
		}
		
		InvoiceItem itemToAdd = InvoiceControllerUtil.getInvoiceItemFrom(invoiceForm.getInvoice(), p, invoiceForm.getSerial(), invoiceForm.getQty(), null,refPriceTier);		
		SalesReturnUtils.adjOriginalForSalesReturn(currentItems, itemToAdd, refItems,  productService, result);

		if (result.hasErrors()) { return false; }
		
		
		if (!MathUtils.isZero(itemToAdd.getPriceAsBD()) ) {
			
			invoiceForm.setServicePrice(itemToAdd.getPriceAsBD().floatValue());
			invoiceForm.setPriceTier(refPriceTier);
			
		}
		
		return true;
	}
	
	public void setSalesService(SalesService salesService) {
		this.salesService = salesService;
	}
	
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
}
