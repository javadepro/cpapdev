package com.esofa.crm.reports.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.esofa.crm.model.InventoryCostEntry;
import com.esofa.crm.model.Product;
import com.esofa.crm.model.report.DateRangeReportReq;
import com.esofa.crm.refdata.model.Manufacturer;
import com.esofa.crm.refdata.model.ProductSubType;
import com.esofa.crm.refdata.model.ProductType;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.crm.service.InventoryService;
import com.esofa.crm.service.ProductService;
import com.esofa.crm.util.ProductUtils;
import com.googlecode.objectify.Key;

public class InventoryPurchaseReport extends BaseCrmReport<DateRangeReportReq> {

	private static final Logger log = Logger.getLogger(InventoryPurchaseReport.class
			.getName());
	
	private InventoryService inventoryService;
	private ProductService productService;
	private RefdataService refdataService;
	

	@Override
	protected List<Map<String,String>> getData(DateRangeReportReq req) {
		Date fromDate = req.getFromDate();
		Date endDate = req.getEndDate();
		
		List<InventoryCostEntry> rawResults = inventoryService.getInventoryCost(fromDate, endDate);		
		List<Map<String,String>> resultList = new ArrayList<Map<String,String>>();
		
		Map<Key<Product>, Product> productMap = productService.getProductMap(false);
		Map<Key<ProductType>, ProductType> productTypeMap =  refdataService.getProductTypeMap();
		Map<Key<ProductSubType>, ProductSubType> productSubTypeMap = refdataService.getProductSubTypeMap();
		Map<Key<Manufacturer>, Manufacturer> manufacturerMap = refdataService.getManufacturerMap();
		
		for (InventoryCostEntry entry : rawResults) {
		
			Map<String,String> row = new HashMap<String,String>();
			

			Product p = productMap.get(entry.getProductKey());			

			if (entry == null || entry.getProductKey() == null || p == null) {
				log.severe(entry.toString());
			}
			
			row.put("Product Name", p.getName());
			row.put("Product Bar Code", p.getProductBarCode());
			row.put("Product Reference Number", p.getReferenceNumber());

			populateManufacturerName(row, p, manufacturerMap);
			populateProductTypeInfo(row, p, productTypeMap, productSubTypeMap);
			populateSubTypeInfo(row, p, productSubTypeMap);
						
			populate(row,entry);
			resultList.add(row);
		}
		
		return resultList;
	}

	private void populateManufacturerName(Map<String,String> row,Product p, Map<Key<Manufacturer>, Manufacturer> manufacturerMap) {

		Manufacturer manu = ProductUtils.getManufacturer(p, manufacturerMap);
		if (manu !=null) {
			row.put("Manufacturer", manu.getName());
		}		
	}
	
	private void populateProductTypeInfo (Map<String,String> row, Product p, Map<Key<ProductType>, ProductType> productTypeMap,  Map<Key<ProductSubType>, ProductSubType> productSubTypeMap) {
		
		ProductType pt = ProductUtils.getProductType(p, productTypeMap, productSubTypeMap);
		if (pt !=null) {
			row.put("Product Type",pt.getType());
		}
	}
	
	private void populateSubTypeInfo(Map<String,String> row, Product p, Map<Key<ProductSubType>, ProductSubType> productSubTypeMap) {
		
		ProductSubType pst = ProductUtils.getProductSubType(p, productSubTypeMap); 
		if (pst !=null) {
			
			row.put("Product SubType", pst.getType());
		}
	}
	
	
	public void setInventoryService(InventoryService inventoryService) {
		this.inventoryService = inventoryService;
	}
	
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
	
	public void setRefdataService(RefdataService refdataService) {
		this.refdataService = refdataService;
	}
}



