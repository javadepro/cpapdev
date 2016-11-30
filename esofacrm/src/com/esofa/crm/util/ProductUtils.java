package com.esofa.crm.util;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.esofa.crm.model.Product;
import com.esofa.crm.refdata.model.Manufacturer;
import com.esofa.crm.refdata.model.ProductSubType;
import com.esofa.crm.refdata.model.ProductType;
import com.googlecode.objectify.Key;

public class ProductUtils {

	public static Manufacturer getManufacturer(Product p, Map<Key<Manufacturer>, Manufacturer> manufacturerMap) {
		
		Key<Manufacturer> manuKey = p.getManufacturer();
		return manufacturerMap.get(manuKey);
	}

	public static ProductType getProductType(Product p, Map<Key<ProductType>, ProductType> productTypeMap, Map<Key<ProductSubType>, ProductSubType> productSubTypeMap ) {
		
		ProductType pt = null;
		ProductSubType pst = getProductSubType(p, productSubTypeMap); 		
		if (pst !=null) {
			
			pt = productTypeMap.get(pst.getParentType());
		}
		
		return pt;
	}
	
	public static ProductSubType getProductSubType(Product p, Map<Key<ProductSubType>, ProductSubType> productSubTypeMap ) {
		
		Key<ProductSubType> productSubTypeKey = p.getProductSubType();
		return productSubTypeMap.get(productSubTypeKey);
		
	}

	public static Product  getProductByBarcode(String barcode, Map<Key<Product>,Product> productMap) {
		
		Product productToReturn=null;
		for (Product p : productMap.values()) {
			
			if (StringUtils.equalsIgnoreCase(barcode, p.getProductBarCode())) {
				productToReturn = p;
				break;
			}
		}		
		
		return productToReturn;
	}
}
