package com.esofa.crm.controller.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.esofa.crm.model.Product;
import com.esofa.crm.model.pos.Invoice;
import com.esofa.crm.model.pos.InvoiceItem;
import com.esofa.crm.service.ProductService;
import com.esofa.crm.service.SalesService;
import com.esofa.crm.util.InvoiceUtils;
import com.googlecode.objectify.Key;

public class SalesReturnUtils {

	

	public static void handleVoid(List<InvoiceItem> currentItems, Key<Invoice> originalInvoiceKey, SalesService salesService, ProductService productService) {
		
		List<InvoiceItem> originalItems = salesService.getInvoiceItems(originalInvoiceKey);
		
		//for each item in invoiceItems		
		for (InvoiceItem currItem : currentItems) {
			
			Key<Product> productKey = currItem.getProductKey();
			int qtyRequiredToVoid = currItem.getQty();
			
			//skip if a nonProduct.
			if (InvoiceUtils.isProductType(productService.getProductByKey(productKey), InvoiceUtils.ProductTypeCheckE.isNonProduct)) {
				continue;
			}
			
			//get the items in the original invoice and reverse the qtyReturned
			List<InvoiceItem> pItems = getInvoiceItemOfProduct(productKey,originalItems);			
			for (InvoiceItem pItem : pItems) {
				
				
				if (pItem.getQtyReturned() > 0 ) {
					
					//requiredToVoid = 5
					//avail = 2															
					int qtyReturnableForThisInvoiceItem = 0;  
					
					//ie: requiredToVoid = 5, qtyReturned = 2.  result: qtyReturnableForThisInvoiceItem=2
					if (qtyRequiredToVoid > pItem.getQtyReturned()) {
						qtyReturnableForThisInvoiceItem = pItem.getQtyReturned();
						
					//ie: requiredToVoid = 1, qtyReturned = 2.  result: qtyReturnableForThisInvoiceItem=1
					//and then iterate until done.
					} else if (qtyRequiredToVoid <= pItem.getQtyReturned()) {
						qtyReturnableForThisInvoiceItem = qtyRequiredToVoid;
					}
										
					qtyRequiredToVoid -= qtyReturnableForThisInvoiceItem;
					pItem.addQtyReturned(-qtyReturnableForThisInvoiceItem);
					
					if (qtyRequiredToVoid == 0) { break;}
				}
			}
			salesService.saveInvoiceItem(pItems);
		}
		
	}
	
	public static List<InvoiceItem> adjOriginalForSalesReturn(List<InvoiceItem> currentItems, Key<Invoice> originalInvoiceKey, 
			SalesService salesService, ProductService productService, BindingResult result) {
		

		List<InvoiceItem> originalItems = salesService.getInvoiceItems(originalInvoiceKey);	
		return adjOriginalForSalesReturn(currentItems, originalItems, productService, result);
	}
	

	public static List<InvoiceItem> adjOriginalForSalesReturn(List<InvoiceItem> currentItems, InvoiceItem itemToAdd, List<InvoiceItem> originalItems, 
		 ProductService productService, BindingResult result) {
		
		List<InvoiceItem> copyWithProductWishingToAdd = new ArrayList<InvoiceItem>(currentItems);
		copyWithProductWishingToAdd.add(itemToAdd);
		return adjOriginalForSalesReturn(copyWithProductWishingToAdd,originalItems,productService,result);
	}
	
	
	public static List<InvoiceItem> adjOriginalForSalesReturn(List<InvoiceItem> currentItems, List<InvoiceItem> originalItems, 
		 ProductService productService, BindingResult result) {
		
		//for each item in invoiceItems		
		for (InvoiceItem currItem : currentItems) {
			
			Key<Product> productKey = currItem.getProductKey();
			int qtyRequiredToMarkAsReturned = currItem.getQty();
			
			//skip if a nonProduct.
			if (InvoiceUtils.isProductType(productService.getProductByKey(productKey), InvoiceUtils.ProductTypeCheckE.isNonProduct)) {
				continue;
			}
			
			//get the items in the original invoice and reverse the qtyReturned
			List<InvoiceItem> pItems = getInvoiceItemOfProduct(productKey,originalItems);			
			for (InvoiceItem pItem : pItems) {
				
				
				if (pItem.getQtyReturned() < pItem.getQty() ) {
																	
					int qtyReturnableForThisInvoiceItem = pItem.getQty() - pItem.getQtyReturned();  
					
					//ie: requiredToMark = 5, qty= 3, qtyReturned = 2.  result: qtyReturnableForThisInvoiceItem=1.
					if (qtyRequiredToMarkAsReturned >= qtyReturnableForThisInvoiceItem) {
						
						//then qtyReturnableForThisInvoiceItem is already correct.
						
					//ie: requiredToMark = 1, qtyReturned = 2.  result: qtyReturnableForThisInvoiceItem=1
					//and then iterate until done.
					} else if (qtyRequiredToMarkAsReturned < qtyReturnableForThisInvoiceItem) {
						qtyReturnableForThisInvoiceItem = qtyRequiredToMarkAsReturned;
					}
					
					qtyRequiredToMarkAsReturned -= qtyReturnableForThisInvoiceItem;
					pItem.addQtyReturned(qtyReturnableForThisInvoiceItem);
					currItem.setPriceAsBD(pItem.getPriceAsBD().negate());
					
					if (qtyRequiredToMarkAsReturned == 0) { break; }
				}
			}
			
			if (qtyRequiredToMarkAsReturned !=0) {
				
				result.addError(new ObjectError("invoiceForm","insufficient quantity in original invoice to mark " + currItem.getProductName() + " as returned."));
			}
		}
		
		//adjusted items from original invoice
		return originalItems;
	}
	

	
	private static List<InvoiceItem> getInvoiceItemOfProduct(Key<Product> productKey,List<InvoiceItem> items) {
		
		List<InvoiceItem> itemsToReturn = new ArrayList<InvoiceItem>();
		
		for ( InvoiceItem i : items) {
			if (productKey.getId()== i.getProductKey().getId()) {
				itemsToReturn.add(i);
			}
		}
		
		return itemsToReturn;
	}
	
}
