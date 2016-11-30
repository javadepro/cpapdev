package com.esofa.crm.controller.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.ModelAndView;

import com.esofa.crm.model.Product;
import com.esofa.crm.model.pos.Invoice;
import com.esofa.crm.model.pos.Invoice.InvoiceStatusTypeE;
import com.esofa.crm.model.pos.Invoice.InvoiceTypeE;
import com.esofa.crm.model.pos.InvoiceItem;
import com.esofa.crm.refdata.model.ProductSubType;
import com.esofa.crm.refdata.model.Setting;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.crm.refdata.service.RefdataService.SettingTypes;
import com.esofa.crm.security.user.model.CrmUser;
import com.esofa.crm.service.ConfigService;
import com.esofa.crm.service.ProductService;
import com.esofa.crm.service.SalesService;
import com.esofa.crm.util.InvoiceUtils;
import com.esofa.crm.util.MathUtils;
import com.esofa.crm.util.ProductUtils;
import com.googlecode.objectify.Key;

public class InvoiceControllerUtil {


	public static  boolean passMaskCheck(InvoiceForm invoiceForm,BindingResult result, ProductService productService) {

		//if there is a package, then we need check for at least one mask		
		if (!(InvoiceUtils.isInvoiceType(invoiceForm.getInvoice(), Invoice.InvoiceTypeE.STANDARD))
				|| InvoiceUtils.isInvoiceType(invoiceForm.getInvoice(), Invoice.InvoiceTypeE.SALES_RETURN)) {
			
			//if not ADP, then no need to check for mask anyways.  pass check.
			//if not Standard Invoice, no need to check.
			return true; 
		}
		
		List<InvoiceItem> items = invoiceForm.getInvoiceItems();
		boolean hasPackage=false;
		boolean hasMask=false;
		
		for (InvoiceItem item : items) {

			Product itemProduct = productService.getProductMap().get(
					item.getProductKey());

			if (!hasPackage && InvoiceUtils.isPackage(itemProduct)) {

				hasPackage=true;
			} else if (!hasMask && InvoiceUtils.isMask(itemProduct)) {
				hasMask = true;				
			}
		}
		
		//if has package, then must have mask
		//it is ok to have mask, but not package.
		if (hasPackage) {
			
			if (hasMask) {
				return true;
			} else {
				result.addError(new FieldError("invoiceForm", "product","If you select a package, then you must have a mask."));
				return false;
			}
		}
		return true;
	}
	
	public static void validateCpapInfoBlock(InvoiceForm invoiceForm, BindingResult result) {
		
		Invoice invoice = invoiceForm.getInvoice();
		
		if (StringUtils.isEmpty(invoice.getStatus())) {
			
			result.addError(new FieldError("invoiceForm","invoice.status","payment status is required"));
		}

		if (StringUtils.isEmpty(invoice.getMachineWarranty())) {
			
			result.addError(new FieldError("invoiceForm","invoice.machineWarranty","machine warranty is required"));
		}
		
		if (StringUtils.isEmpty(invoice.getMaskWarranty())) {
			result.addError(new FieldError("invoiceForm","invoice.maskWarranty","mask warranty is required"));
		}
		
		if (StringUtils.isEmpty(invoice.getPaymentMethod())) {
			
			result.addError(new FieldError("invoiceForm","invoice.paymentMethod","payment method is required"));
		}
		
		if (invoice.getCustomerKey() == null && !InvoiceUtils.isFullPayment(invoice)
				&& InvoiceUtils.isInvoiceType(invoice, Invoice.InvoiceTypeE.STANDARD)) {
			
			result.addError(new FieldError("invoiceForm","invoice.paymentMethod","generic invoices must be paid in full."));
		}

		if ((InvoiceUtils.isInvoiceType(invoice, Invoice.InvoiceTypeE.OTHER) 
				&& !InvoiceUtils.isFullPayment(invoice))) {
			
			result.addError(new FieldError("invoiceForm","invoice.paymentMethod","other mode invoices must be full payment"));
		}
		
		
		if ((InvoiceUtils.isInvoiceType(invoice, Invoice.InvoiceTypeE.TRIAL) 
				|| InvoiceUtils.isInvoiceType(invoice, InvoiceTypeE.TRIAL_REFUND))
				&& !InvoiceUtils.isFullPayment(invoice)) {
			
			result.addError(new FieldError("invoiceForm","invoice.paymentMethod","trial mode invoices must be full payment"));
		}
		
	}
	
	
	public static void validateHeaderInfo(InvoiceForm invoiceForm, BindingResult result) {
		

		Invoice invoice = invoiceForm.getInvoice();
		
		if(!InvoiceUtils.isSleepMed(invoice)
				&& StringUtils.isEmpty(invoice.getBenefitCode())) {
			result.addError(new FieldError("invoiceForm","invoice.benefitCode","benefit code is empty. update the customer profile first before generating an invoice. if generic invoice, benefit code is required."));
		}
		
		if (StringUtils.isEmpty(invoiceForm.getInvoice().getInvoiceType())) {
			result.addError(new FieldError("invoiceForm","invoice.invoiceType","you must first select an invoice type!"));
		}
	}

	public static boolean validateTrialInfo(InvoiceForm invoiceForm, BindingResult result, ProductService productService, ConfigService configService) {

		//no need to check if it is not type trial
		if (!InvoiceUtils.isInvoiceType(invoiceForm.getInvoice(), Invoice.InvoiceTypeE.TRIAL)) {
			
			return true;
		}
		
		List<InvoiceItem> items = invoiceForm.getInvoiceItems();
		boolean hasTrial=false;
		boolean hasMask=false;
		
		for (InvoiceItem item : items) {

			
			Product itemProduct = productService.getProductMap().get(
					item.getProductKey());

			if (!hasTrial && InvoiceUtils.isTrialDeposit(itemProduct,configService)) {

				hasTrial=true;
			} else if (!hasMask && InvoiceUtils.isMask(itemProduct)) {
				hasMask = true;				
			} else if (hasMask && InvoiceUtils.isMask(itemProduct)) {
				result.addError(new FieldError("invoiceForm", "product","If trial deposit, only allow one mask."));		
				return false;
			}
			
			//if we found both, no need to continue;
			if (hasTrial && hasMask) {
				break;
			}
		}
		
		//if has package, then must have mask
		//it is ok to have mask, but not package.
		if (hasTrial) {			
			if (!hasMask) {
				result.addError(new FieldError("invoiceForm", "product","If you select a trial deposit, then you must have a mask."));		
				return false;
			}
			
			if (!InvoiceUtils.isPriceTier1(invoiceForm.getPriceTier())) {
				result.addError(new FieldError("invoiceForm", "priceMode","you must be in Tier 1 pricing to process a trial deposit"));
				return false;
			}
		}
		
		return true;
	}
	
	public static void validateItemsforInvoiceType(InvoiceForm invoiceForm, BindingResult result, ConfigService configService) {
		
		Invoice i = invoiceForm.getInvoice();
		
		if (InvoiceUtils.isInvoiceType(i, InvoiceTypeE.STANDARD)) {
			for (InvoiceItem item: invoiceForm.getInvoiceItems()) {
			
				//standard invoice should not have trial deposit
				//or trial refund.
				if (InvoiceUtils.isTrialDeposit(item.getProductKey(), configService)
						|| InvoiceUtils.isTrialRefund(item.getProductKey(), configService)) {
					result.addError(new FieldError("invoiceForm","invoiceType","trial deposits and trial refunds should not be in a standard invoice."));
					return;
				}
			}
		}
				
		if (InvoiceUtils.isInvoiceType(i, InvoiceTypeE.TRIAL)) {
			for (InvoiceItem item : invoiceForm.getInvoiceItems()) {
				
				if (InvoiceUtils.isTrialRefund(item.getProductKey(),configService)) {
					result.addError(new FieldError("invoiceForm","invoiceType"," trial refunds should not be in a trial invoice."));
					return;
				}
			}
		}
		
	}
	
	public static BigDecimal getRedemCodeAsPercentage(Invoice i, BindingResult result) {

		BigDecimal redemCode=null;

		try {

			//dormant code
			// redem code is not allowed for cpapdirect
			//reference number is not used for cpapdirect
//			if (!InvoiceUtils.isSleepMed(invoiceForm.getInvoice())) {
//				// set redem code to zero
//				i.setRedemCodeAsBD(BigDecimal.ZERO);
//				i.setReferenceNumber(StringUtils.EMPTY);
//			}
	
			redemCode = i.getRedemCodeAsBD().setScale(MathUtils.NUM_SCALE);			
			
		} catch (Exception e) {

			result.addError(new FieldError("invoiceForm", "redemCode", "redem code is not valid. it must be a percentage. ie (10 for 10%)"));
			return  MathUtils.ZERO;
		}	
		
		//must be a percentage
		if (!(redemCode.compareTo(BigDecimal.ZERO.setScale(MathUtils.NUM_SCALE)) >= 0
				&& redemCode.compareTo(new BigDecimal(100).setScale(MathUtils.NUM_SCALE)) <= 0)) {
			result.addError(new FieldError("invoiceForm", "redemCode", "redem code must be between 0 to 100 percent"));
			return MathUtils.ZERO;
		}
		
		redemCode = redemCode.scaleByPowerOfTen(-2).setScale(MathUtils.NUM_SCALE,BigDecimal.ROUND_HALF_UP);
	
		return redemCode;
	}
	


	//need to find a better idea for this
	public static Invoice validateReferencedInvoice(InvoiceForm invoiceForm, BindingResult result,  SalesService salesService) {
	
		String invoiceNumber = invoiceForm.getInvoice().getReferencedInvoiceNumber();
		
		if (StringUtils.isEmpty(invoiceNumber)) {
			return null;
		}
		
		Invoice invoice = salesService.getInvoice(invoiceNumber);
		
		if (invoice == null) {
			//error
			result.addError(new ObjectError("invoiceForm", "Error.  Unable to find invoice"));
			
		} else if (InvoiceUtils.isInvoiceStatusType(invoice, Invoice.InvoiceStatusTypeE.VOID)) {
			result.addError(new ObjectError("invoiceForm", "Invoice has already been void."));
		} else if (InvoiceUtils.isInvoiceStatusType(invoice, Invoice.InvoiceStatusTypeE.TRIAL_ENDED)) {
			result.addError(new ObjectError("invoiceForm","Invoice is a trial invoice and has already ended"));
		}
		return invoice;
	}

	
	public static Invoice getInvoiceWithChecks(String invoiceNumber, ModelAndView mav,  SalesService salesService) {
	
		Invoice invoice = salesService.getInvoice(invoiceNumber);
		
		if (invoice == null) {
			//error
			mav.addObject("warning", "Error.  Unable to find invoice");
			
		} else if (InvoiceUtils.isInvoiceStatusType(invoice, Invoice.InvoiceStatusTypeE.VOID)) {
			mav.addObject("warning", "Invoice has already been void.");
			invoice= null;
		} else if (InvoiceUtils.isInvoiceStatusType(invoice, Invoice.InvoiceStatusTypeE.TRIAL_ENDED)) {
			mav.addObject("warning", "Invoice is a trial invoice and has already ended");
			invoice= null;
		}
		return invoice;
	}

	public static InvoiceItem findInvoiceItemFrom(List<InvoiceItem> items, Product productToFind) {
		
		InvoiceItem i = null;
		
		for (InvoiceItem item : items) {
			
			if (item.getProductKey().getId() == productToFind.getId()) {
				
				i = new InvoiceItem(item.getProductKey(),item.getProductName(),item.getQty(),item.getPrice(),item.getBypassInvAdj(),item.getTotal());				
				break;
			}
		}
		
		return i;
	}

	public static InvoiceItem getInvoiceItemFrom(Invoice invoice, Product p, String serial,int qty, BigDecimal servicePrice,
			String priceTier) {
		
		InvoiceItem i = new InvoiceItem();
		i.setProductKey(Key.create(Product.class,p.getId()));
		
		
		//i.setPriceAsBD(InvoiceUtils.getPrice(invoiceForm.getInvoice(), p));		
		//invoiceKey is set later after invoice is saved.
		
		i.setProductAdpNumber(p.getAdpCatalogNumber());
		i.setProductName(p.getName());
		i.setProductBarcode(p.getProductBarCode());
		i.setProductReference(p.getReferenceNumber());
		i.setProductSerialNumber(serial);
		i.addQty(qty);
		i.setHstApplicable(p.getHstApplicable());
		i.setPriceTier(priceTier);
		
		
		if (InvoiceUtils.isMachineOrPackage(p)
				|| InvoiceUtils.isSleepMed(invoice)) {
			
			//show description of product
			i.setDescription(p.getDescription());
		}

		//use service price if nonproduct
		//otherwise price is set later
		
		//if sales return, then service price is set by one of the validators.
		if (InvoiceUtils.isProductType(p, InvoiceUtils.ProductTypeCheckE.isNonProduct)
				|| InvoiceUtils.isInvoiceType(invoice, Invoice.InvoiceTypeE.SALES_RETURN)) {

			if (servicePrice == null) { servicePrice = MathUtils.ZERO;}
			i.setPriceAsBD(servicePrice);
			i.setProductSerialNumber(StringUtils.EMPTY);
			i.setBypassInvAdj(true);
		}
		
		
		
		return i;
	}
	
	public static List<InvoiceItem> copyAllRegularProducts(List<InvoiceItem> refInvoiceItems, ProductService productService) {
		
		List<InvoiceItem> copyItems =  new ArrayList<InvoiceItem>();
		Map<Key<Product>, Product> productMap = productService.getProductMap();
		
		for (InvoiceItem i : refInvoiceItems) {
			
			Product p = productMap.get(i.getProductKey());
			
			if (p == null) { continue; }
			else if (p != null && InvoiceUtils.isProductType(p, InvoiceUtils.ProductTypeCheckE.isNonProduct)) {
				continue;
			}
			
			InvoiceItem copy = new InvoiceItem();
			//must be real product. copy all properties.
			BeanUtils.copyProperties(i, copy);
			copy.setInvoiceKey(null);
			copy.setBypassInvAdj(true);
			copyItems.add(copy);
		}
		return copyItems;
	}
	

	public static void adjustTrialInvoice(Invoice referencedInvoice, SalesService salesService) {
		
		if (!(InvoiceUtils.isInvoiceType(referencedInvoice, InvoiceTypeE.TRIAL))) {
			
			return;
		}
		
		referencedInvoice.setStatus(InvoiceStatusTypeE.TRIAL_ENDED.toString());
		salesService.updateInvoice(referencedInvoice);
		
	}

	
	public static  boolean validateDiscountNote(InvoiceForm invoiceForm, BindingResult result) {
		
		if ((MathUtils.ZERO.compareTo(invoiceForm.getInvoice().getPromoCodeAsBD()) != 0
				|| MathUtils.ZERO.compareTo(invoiceForm.getInvoice().getRedemCodeAsBD()) != 0)
			//	&& StringUtils.isEmpty(invoiceForm.getInvoice().getDiscountNote())
				&& invoiceForm.getInvoice().getDiscountReason()== null
				) {
			

			result.addError(new FieldError("invoiceForm", "discountReason","Discount reason must be filled in when redem code or promo code is used."));
			return false;
		}
		
		return true;
	}
	
	public static void setPreparedByUser(InvoiceForm invoiceForm, BindingResult result, RefdataService refdataService ) {
	
		//set userName
		if (invoiceForm.getPreparedByUserKey() != null ) {
			CrmUser preparedBy = refdataService.getClinician(invoiceForm.getPreparedByUserKey());
			
			if (preparedBy != null) {				
				invoiceForm.getInvoice().setUserFirstName(preparedBy.getFirstname());
				invoiceForm.getInvoice().setUserLastName(preparedBy.getLastname());				
			}		
		}
	}
	

	public static boolean allReturned(List<InvoiceItem> invoiceItems) {
		

		if (invoiceItems != null) {
			
			int returnable=0;
			for (InvoiceItem ii: invoiceItems) {
				
				returnable = returnable + ii.getQty() - ii.getQtyReturned();				
			}
			
			if (returnable == 0 ) {
				return true;
			}
		}
		return false;
	}
	
	public static void calcAndSetMaxAdpAmt(InvoiceForm invoiceForm, Product p, ProductService productService, RefdataService refdataService ) {
		
		Invoice i = invoiceForm.getInvoice();					
		boolean addProductIsMachineOrPackage =false;
		
		if (p == null) {
			//then must be in delete mode and we should check all invoices.
			
			Map<Key<Product>,Product>productMap = productService.getProductMap();
			
			for (InvoiceItem ii : invoiceForm.getInvoiceItems()) {
				
				Product iiProduct = productMap.get(ii.getProductKey());
				addProductIsMachineOrPackage =  InvoiceUtils.isMachineOrPackage(iiProduct);
				p = iiProduct;
				break;
			}
			
		} else {
			
			//product is provided, so only need to check this particular product.
			addProductIsMachineOrPackage = InvoiceUtils.isMachineOrPackage(p);	
		}
				
				
		
		//by now the product should be a valid product
		//if package, then we need to find out the adp max
		if (InvoiceUtils.allowAdpPortion(i) && addProductIsMachineOrPackage) {
			
			//save a copy of the price of this items price.  will use to calculate
			//the max adp-able amount later
			
			ProductSubType pst = ProductUtils.getProductSubType(p, refdataService.getProductSubTypeMap());
			Setting adpFundingMaxSetting = refdataService.getSettingByType(SettingTypes.AdpFundingMax, pst.getType());	
			BigDecimal adpFundingMaxSettingBD = (new BigDecimal(adpFundingMaxSetting.getValue()));
			BigDecimal maxAmtToUse = new BigDecimal(p.getPrice());
			
			//if exceeds adp max...then use adp max as it is capped out at that.
			if (maxAmtToUse.compareTo(adpFundingMaxSettingBD) > 0) {
				maxAmtToUse = adpFundingMaxSettingBD;
			}
			
			invoiceForm.setMaxAdpAmtBeforeBenefitDiscount(maxAmtToUse);						
		} else if ( p == null) {
			invoiceForm.setMaxAdpAmtBeforeBenefitDiscount(MathUtils.ZERO);						

		}
		
		if (InvoiceUtils.isInvoiceType(i, InvoiceTypeE.SALES_RETURN)) {
			invoiceForm.setMaxAdpAmtBeforeBenefitDiscount(invoiceForm.getMaxAdpAmtBeforeBenefitDiscount().negate());
		}
	}
	
	
	public static void resetInputBoxes(InvoiceForm invoiceForm) {
		invoiceForm.setBarcode(StringUtils.EMPTY);
		invoiceForm.setSerial(StringUtils.EMPTY);
		invoiceForm.setLotNumber(StringUtils.EMPTY);
		invoiceForm.setAdditionalSerial(StringUtils.EMPTY);
		invoiceForm.setServicePrice(null);
		invoiceForm.setPriceTier(InvoiceItem.ItemPriceTier.TIER_1.toString());
		invoiceForm.setQty(1);
	}
	
}
