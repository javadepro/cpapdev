package com.esofa.crm.reports.queuejob;


import static com.esofa.crm.service.OfyService.ofy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.esofa.crm.model.Inventory;
import com.esofa.crm.model.InventoryCostEntry;
import com.esofa.crm.model.InventoryCostQueue;
import com.esofa.crm.model.Product;
import com.esofa.crm.model.report.InventoryCostRE;
import com.esofa.crm.model.report.ShopCount;
import com.esofa.crm.queuejob.AbstractOnlineReportTask;
import com.esofa.crm.refdata.model.Manufacturer;
import com.esofa.crm.refdata.model.Shop;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.crm.service.InventoryService;
import com.esofa.crm.service.InventoryTransferService;
import com.esofa.crm.service.ProductService;
import com.esofa.crm.util.MathUtils;
import com.googlecode.objectify.Key;

/**
 * for each product, attempt to get the inventory cost info
 * sum and calculate inventory cost
 * @author JHa
 * @param <T>
 *
 */
public class InventoryCostReportTask extends AbstractOnlineReportTask<InventoryCostRE> {

	private RefdataService refdataService;
	private ProductService productService;
	private InventoryTransferService inventoryTransferService;
	private InventoryService inventoryService;
	
	private Map<Key<Shop>, Shop> shopMap;
	private Map<Key<Manufacturer>, Manufacturer> manuMap;

	private int pageSize=100;

	
	@Override
	protected void initTask(Map<String, String> params) {

		
	}

	@Override
	protected Map<String, String> doTask(Map<String, String> params) {

		initReferenceMaps();
		String startCursor = params.get(PARAM_CURSOR);
		Map<Product, Map<Key<Shop>, Inventory>> resultsHolder = new HashMap<>(pageSize);
		
		boolean atLeast1=false;
		
		String newCursor = productService.getProductMapWithInventory(startCursor, resultsHolder, pageSize);


		for (Product p : resultsHolder.keySet()) {
			
			Manufacturer manu = manuMap.get(p.getManufacturer());
			
			InventoryCostRE entry = new InventoryCostRE();
			setReInfo(entry,params);
			entry.setProductName(p.getName());
			entry.setProductBarcode(p.getProductBarCode());
			if (manu != null) { entry.setManufacturerName(manu.getName());}
			
	
			calcCounts(entry,p,resultsHolder.get(p));
			calcAvgCost(entry,p);
			calcInventoryCost(entry);		
			
			saveRE(entry);
			atLeast1=true;
		}
		
		if (!atLeast1 || StringUtils.isEmpty(newCursor)) {
			params.put(PARAM_END,PARAM_END);

		}
		params.put(PARAM_CURSOR, newCursor);
		return params;
	}

	private void calcCounts(InventoryCostRE entry, Product p,Map<Key<Shop>, Inventory> inventoryMap ) {
		
		Set<ShopCount> shopInvCountMap = entry.getShopInvCountSet();
		
		int totalCount = 0;
		
		for (Key<Shop> sKey : shopMap.keySet()) {
			
			Inventory i = inventoryMap.get(sKey);
			int qty =0; 
			if (i != null) {
				qty = i.getQty();
			}
			totalCount = totalCount + qty;
			
			Shop s = shopMap.get(sKey);			
			shopInvCountMap.add(new ShopCount(s.getShortName(),qty));			
		}
		
		//handle transfers
		int trnCount = inventoryTransferService.getInventoryTransferQty(p.getId());
		totalCount = totalCount + trnCount;
		shopInvCountMap.add(new ShopCount("Trn",trnCount));		
		
		entry.setShopInvCountSet(shopInvCountMap);
		entry.setTotalCount(totalCount);
	}
	
	private void calcAvgCost(InventoryCostRE entry, Product p) {
		
		BigDecimal avgCost = MathUtils.toBD(p.getDefaultCost());
		InventoryCostQueue icq = inventoryService.getInventoryCostQueue(p.getId());
		
		if (icq == null) {
			entry.setAvgUnitCost(avgCost.toString());
			return;
		}
		
		LinkedList<Key<InventoryCostEntry>> icqLinkedList = icq.getCostEntryQueue();
		
		if (icqLinkedList == null) {
			entry.setAvgUnitCost(avgCost.toString());
			return;
		}
		
		List<Key<InventoryCostEntry>> keyIcqList = new ArrayList<Key<InventoryCostEntry>>(icqLinkedList.size());
		keyIcqList.addAll(icqLinkedList);
				
		Map<Key<InventoryCostEntry>, InventoryCostEntry> iceMap = ofy().load().keys(keyIcqList);
		
		int count=0;
		BigDecimal cost = MathUtils.ZERO;
		
		for (InventoryCostEntry ice : iceMap.values()) {
			
			count = count + ice.getQty() - ice.getQtySold();
			cost = cost.add(MathUtils.toBD(ice.getCost()));			
		}
		
		//if count in the queue is less than total...then the rest should be
		//according to default cost.
		if (count < entry.getTotalCount()) {
		
			//adjust for delta by using default values
			int deltaCount = entry.getTotalCount() - count;
			BigDecimal deltaCost = new BigDecimal(deltaCount);
			deltaCost = deltaCost.multiply(MathUtils.toBD(p.getDefaultCost()));
			
			//update count and cost
			count = entry.getTotalCount();
			cost = cost.add(deltaCost);
		}
		
		//if the inventorycostentry has empty cost queue.
		if (count != 0) {
			
			avgCost = cost.divide(new BigDecimal(count), MathUtils.ROUND_MODE);	
		}
		
		entry.setAvgUnitCost(avgCost.toString());
				
	}
	
	private void calcInventoryCost(InventoryCostRE entry) {
		
		BigDecimal inventoryCost = MathUtils.ZERO;
		BigDecimal totalCount = new BigDecimal(entry.getTotalCount());
		BigDecimal avgCost = new BigDecimal(entry.getAvgUnitCost());
		
		
		inventoryCost = MathUtils.setBDScale(totalCount.multiply(avgCost));
		entry.setTotalInventoryCost(inventoryCost.toString());
	}
	
	@Override
	protected void executeOnComplete(Map<String, String> params) {
		shopMap =null;
		manuMap =null;
	}
	
	private void initReferenceMaps() {
		
		//if one map is null, assume all are.
				
		if (shopMap == null) { 
			
			shopMap = refdataService.getShopMap();
			manuMap= refdataService.getManufacturerMap();
		}
		
		
	}
	
	
	public void setRefdataService(RefdataService refdataService) {
		this.refdataService = refdataService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
	
	public void setInventoryTransferService(
			InventoryTransferService inventoryTransferService) {
		this.inventoryTransferService = inventoryTransferService;
	}
	
	public void setInventoryService(InventoryService inventoryService) {
		this.inventoryService = inventoryService;
	}
}
