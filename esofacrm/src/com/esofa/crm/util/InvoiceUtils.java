package com.esofa.crm.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.esofa.crm.model.CompanyTypeE;
import com.esofa.crm.model.Product;
import com.esofa.crm.model.pos.Invoice;
import com.esofa.crm.model.pos.Invoice.InvoiceStatusTypeE;
import com.esofa.crm.model.pos.InvoiceItem;
import com.esofa.crm.refdata.model.ProductSubType;
import com.esofa.crm.refdata.model.ProductType;
import com.esofa.crm.service.ConfigService;
import com.googlecode.objectify.Key;

public class InvoiceUtils  {

	private static final Key<ProductType> PACKAGE_PRODUCT_TYPE=Key.create(ProductType.class, 500000);
	private static final Key<ProductType> MACHINE_PRODUCT_TYPE=Key.create(ProductType.class, 500001);
	private static final Key<ProductType> MASK_PRODUCT_TYPE=Key.create(ProductType.class, 500010);
	private static final Key<ProductType> NONPRODUCT_PRODUCT_TYPE=Key.create(ProductType.class, 500016);
	
	private static final Key<ProductSubType> CREDIT_PRODUCT_SUBTYPE=Key.create(NONPRODUCT_PRODUCT_TYPE,ProductSubType.class, 510041);
	private static final Key<ProductSubType> DEPOSIT_PRODUCT_SUBTYPE=Key.create(NONPRODUCT_PRODUCT_TYPE,ProductSubType.class, 510042);
	private static final Key<ProductSubType> FEE_PRODUCT_SUBTYPE=Key.create(NONPRODUCT_PRODUCT_TYPE,ProductSubType.class, 510043);

	private static final float FLOAT_ZERO = new Float(0).floatValue();
	
	private static Map<ProductTypeCheckE,Set<Key<ProductType>>> productTypeCheckMap;
	private static Map<ProductSubTypeCheckE,Set<Key<ProductSubType>>> productSubTypeCheckMap;
	private static List<String> defaultInvoiceTypeList;
	private static List<String> sleepMedPriceTierList;
	
	
	public enum ProductTypeCheckE {
		
		isMachineOrPackage,isPackage,isMask,isNonProduct
	}
	
	public enum ProductSubTypeCheckE {
		
		isCredit,
		isFee,
		isDeposit,
		allowInTrialRefundInvoice
	}
	
	static {
		
		initProducTypeCheckMap();
		initProductSubTypeCheckMap();
		initDefaultInvoiceTypeList();
		
		sleepMedPriceTierList = new ArrayList<String>();
		sleepMedPriceTierList.add(InvoiceItem.ItemPriceTier.TIER_1.name());
		
	}
	
	private static void initProducTypeCheckMap() {
		
		productTypeCheckMap = new HashMap<ProductTypeCheckE,Set<Key<ProductType>>> ();
		
		Set<Key<ProductType>> s = new HashSet<Key<ProductType>>();
		s.add(PACKAGE_PRODUCT_TYPE);
		s.add(MACHINE_PRODUCT_TYPE);
		productTypeCheckMap.put(ProductTypeCheckE.isMachineOrPackage, s);
		
		s = new HashSet<Key<ProductType>>();
		s.add(PACKAGE_PRODUCT_TYPE);
		productTypeCheckMap.put(ProductTypeCheckE.isPackage, s);
		
		s = new HashSet<Key<ProductType>>();
		s.add(MASK_PRODUCT_TYPE);
		productTypeCheckMap.put(ProductTypeCheckE.isMask, s);
		
		s = new HashSet<Key<ProductType>>();
		s.add(NONPRODUCT_PRODUCT_TYPE);
		productTypeCheckMap.put(ProductTypeCheckE.isNonProduct, s);
	}
	
	private static void initProductSubTypeCheckMap() {
		productSubTypeCheckMap= new HashMap<ProductSubTypeCheckE,Set<Key<ProductSubType>>> ();
		
		Set<Key<ProductSubType>> s2 = new HashSet<Key<ProductSubType>>();
		s2.add(CREDIT_PRODUCT_SUBTYPE);
		productSubTypeCheckMap.put(ProductSubTypeCheckE.isCredit, s2);
		
		s2 = new HashSet<Key<ProductSubType>>();
		s2.add(CREDIT_PRODUCT_SUBTYPE);
		s2.add(FEE_PRODUCT_SUBTYPE);		
		productSubTypeCheckMap.put(ProductSubTypeCheckE.allowInTrialRefundInvoice, s2);
		
		s2 = new HashSet<Key<ProductSubType>>();
		s2.add(FEE_PRODUCT_SUBTYPE);		
		productSubTypeCheckMap.put(ProductSubTypeCheckE.isFee, s2);
		
		s2 = new HashSet<Key<ProductSubType>>();
		s2.add(DEPOSIT_PRODUCT_SUBTYPE);		
		productSubTypeCheckMap.put(ProductSubTypeCheckE.isDeposit, s2);
	}

	public static boolean isSleepMed(Invoice invoice) {
		
		return isSleepMed(invoice.getCompany());
	}

	public static boolean isSleepMed(String companyName) {
		
		boolean isSleepMed = false;
		if (StringUtils.equalsIgnoreCase(companyName, CompanyTypeE.SLEEPMED.name())) {
			
			isSleepMed=true;
		}
		
		return isSleepMed;
	
	}

	public static boolean useTier1Pricing(String priceTier,Product p) {
		
		boolean useTier1Price= false;
		if ( isPriceTier1(priceTier) || p.getPriceNonAdp().floatValue() == FLOAT_ZERO) {
			
			useTier1Price=true;
		}
		
		return useTier1Price;
	}
	
	//provides the appropriate price given the pricing mode.
	public static BigDecimal getPrice(String priceTier,Product p) {		
		
		boolean usePriceTier1= useTier1Pricing(priceTier,p);
		BigDecimal price= new BigDecimal(p.getPrice().toString());
		
		if (!usePriceTier1) {

			price = new BigDecimal(p.getPriceNonAdp().toString());
		} 
		
		price = price.setScale(Invoice.SCALE,BigDecimal.ROUND_HALF_UP);		
		return price;
	}
	
	public static boolean isPriceTier1(String priceTier) {
		
		if (StringUtils.equalsIgnoreCase(priceTier,InvoiceItem.ItemPriceTier.TIER_1.toString()) 
				|| StringUtils.isEmpty(priceTier) ){
					
			return true;
		}
		
		return false;
	}
	
	public static boolean isProductType(Product p, ProductTypeCheckE productTypeCheckKey) {
		
		Key<ProductType> productTypeKey = null;
		
		if (p == null) {
			return false;
		}
		
		if (p.getProductSubType() != null) {
			productTypeKey = p.getProductSubType().getParent(); 
		}
		
		if (productTypeCheckMap.get(productTypeCheckKey).contains(productTypeKey)) {
			return true;
		}
		
		return false;
	}
	
	public static boolean isProductSubType (Product p, ProductSubTypeCheckE productSubTypeCheckKey) {
		
		Key<ProductSubType> productSubTypeKey = null;
		
		if (p == null) { return false; } 
		
		productSubTypeKey = p.getProductSubType() ;
			
		if (productSubTypeCheckMap.get(productSubTypeCheckKey).contains(productSubTypeKey)) {
			return true;
		}
		
		return false;
		
	}
	
	public static boolean isMachineOrPackage( Product p) {
		
		return isProductType(p, ProductTypeCheckE.isMachineOrPackage);
	}	
	
	public static boolean isPackage(Product p) {
		
		return isProductType(p, ProductTypeCheckE.isPackage);
	}
	
	public static boolean isMask(Product p) {
		
		return isProductType(p, ProductTypeCheckE.isMask);
	}

	public static boolean isSameProduct(Key<Product> pKey, ConfigService configService, String productConfigKey) {
		
		Key<Product> productKey = configService.getConfigProductKey(productConfigKey);		
		
		if (productKey == null) { return false; }		
		return productKey.getId() == pKey.getId();	
	}
	
	public static boolean isTrialRefund(Key<Product> pKey, ConfigService configService) 
	{
		
		return isSameProduct(pKey,configService, "PRODUCT.TRIALREFUND.ID");
	}
	
	public static boolean isTrialDeposit(Key<Product> pKey, ConfigService configService) {
		
		return isSameProduct(pKey,configService, "PRODUCT.TRIALDEPOSIT.ID");	
	}
	
	public static boolean isTrialDeposit(Product p,ConfigService configService) {
		
		Key<Product> productKey = Key.create(Product.class,p.getId());
		return isTrialDeposit(productKey, configService);
	}
	
	public static boolean isInvoiceType (Invoice i, Invoice.InvoiceTypeE invoiceType) {
		
		if (i == null ) { return false; }
		return StringUtils.equalsIgnoreCase(i.getInvoiceType(),invoiceType.toString());
	}
	
	public static void initDefaultInvoiceTypeList() {
		
		defaultInvoiceTypeList= new ArrayList<String>();
		defaultInvoiceTypeList.add(Invoice.InvoiceTypeE.STANDARD.toString());
		defaultInvoiceTypeList.add(Invoice.InvoiceTypeE.OTHER.toString());
	}
	
	public static List<String>getDefaultInvoiceTypeList () {
		
		if (defaultInvoiceTypeList == null) {
			initDefaultInvoiceTypeList();
		}
		return defaultInvoiceTypeList;
	}
	
	public static List<String> getSleepMedPriceTierList () {
		
		return sleepMedPriceTierList;
	}
	
	
	public static boolean isInvoiceStatusType (Invoice i, Invoice.InvoiceStatusTypeE invoiceStatusType) {
		
		if (i == null ) { return false; }
		return StringUtils.equalsIgnoreCase(i.getStatus(),invoiceStatusType.toString());
	}
	
	public static boolean isInvoiceAdpStatusType (Invoice i, Invoice.InvoiceAdpStatusTypeE invoiceAdpStatusType) {
		
		if (i == null ) { return false; }
		return StringUtils.equalsIgnoreCase(i.getAdpStatus(),invoiceAdpStatusType.toString());
	}
	
	public static boolean allowAdpPortion(Invoice i) {
		
		if ((isInvoiceType(i, Invoice.InvoiceTypeE.STANDARD) || isInvoiceType(i,Invoice.InvoiceTypeE.SALES_RETURN))
				&& !hasSpecialPricing(i) ) {
			return true;
		}
		
		return false;
	}
	
	public static boolean hasSpecialPricing(Invoice i) {
		return i.getSpecialPricing();
	}
	
	public static boolean isFullPayment(Invoice i) {
		
		if (i == null) { return false;}
		return StringUtils.equalsIgnoreCase(i.getStatus(), Invoice.InvoiceStatusTypeE.FULL_PAYMENT.toString());
	}

	public static Key<Invoice> getInvoiceKey(Long id) {		
		Key<Invoice> invoiceKey = Key.create(Invoice.class,id);
		return invoiceKey;
	}
	
	public static List<String> notVoidInvoiceStatus() {
		
		List<String> list = new ArrayList<String>();
		
		for (InvoiceStatusTypeE t : Invoice.InvoiceStatusTypeE.values()) {
			
			if (!InvoiceStatusTypeE.VOID.equals(t)) {
				list.add(t.toString());
			}
		}
		
		return list;
	}
}
