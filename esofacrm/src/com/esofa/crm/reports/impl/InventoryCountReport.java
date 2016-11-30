package com.esofa.crm.reports.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.esofa.crm.model.Inventory;
import com.esofa.crm.model.Product;
import com.esofa.crm.model.ProductSearch;
import com.esofa.crm.model.report.DataDumpReportReq;
import com.esofa.crm.refdata.model.ProductSubType;
import com.esofa.crm.refdata.model.ProductType;
import com.esofa.crm.refdata.model.Shop;
import com.esofa.crm.reports.ReportUtils;
import com.esofa.crm.service.InventoryTransferService;
import com.esofa.crm.service.ProductService;
import com.googlecode.objectify.Key;

public class InventoryCountReport extends BaseCrmReport<DataDumpReportReq> {
	

	
	private ProductService productService;
	private InventoryTransferService inventoryTransferService;
	
	
	
	@Override
	protected List<Map<String, String>> getData(DataDumpReportReq req) {
		
		List<Map<String,String>> resultList = new ArrayList<Map<String,String>>();
		
		Map<Product, Map<Key<Shop>,Inventory>> inventories = productService.search(new ProductSearch());
		Map<Long, Integer> transferQtyMap = inventoryTransferService.getInventoryTransferQty();		
		Map<Key<Shop>, Shop> shopMap = refdataService.getShopMap();
		Map<Key<ProductType>, ProductType> productTypeMap = refdataService.getProductTypeMap();
		Map<Key<ProductSubType>, ProductSubType> productSubTypeMap = refdataService.getProductSubTypeMap();
		
		for( Product p : inventories.keySet() ) {
			int productTotal = 0;
			Map<String,String> row = new HashMap<String,String>();
			Map<Key<Shop>,Inventory> shopInventory = inventories.get(p);			
			for( Key<Shop> invShopkey : shopInventory.keySet() ) {
				Inventory i = shopInventory.get(invShopkey);
				Shop s  = shopMap.get(invShopkey);
				String shopShortName=ReportUtils.STORE_DEFAULT;
				
				if (s != null) {  shopShortName = s.getShortName(); }
				
				row.put(shopShortName, Integer.toString(i.getQty()));
				productTotal += i.getQty();
			}
			Integer transferQty = transferQtyMap.get(p.getId());
			if( transferQty != null ) {
				row.put("Trn", transferQty.toString());
				productTotal += transferQty;
			} else {
				row.put("Trn", "0");
			}
			row.put("Total", Integer.toString(productTotal));
			ProductSubType subtype = productSubTypeMap.get(p.getProductSubType());
			ProductType type = productTypeMap.get(subtype.getParentType());
			row.put("Type", type.getType());
			row.put("Subtype", subtype.getType());
			populate(row,p);			
			resultList.add(row);
		}

		return resultList;
	}
	
	public void setMapping(Map<String, String> mapping) {
		
		this.mapping = ReportUtils.addShopsToMapping(mapping, refdataService.getShopMap().values());		
		setHeader(this.mapping.keySet().toArray(new String[0]));
	}
	

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public void setInventoryTransferService(
			InventoryTransferService inventoryTransferService) {
		this.inventoryTransferService = inventoryTransferService;
	}	
}
