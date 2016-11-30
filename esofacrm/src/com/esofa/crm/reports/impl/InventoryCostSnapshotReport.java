package com.esofa.crm.reports.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.esofa.crm.model.report.InventoryCostRE;
import com.esofa.crm.model.report.ShopCount;
import com.esofa.crm.model.report.SnapshotReportReq;
import com.esofa.crm.refdata.model.Shop;
import com.esofa.crm.reports.ReportUtils;
public class InventoryCostSnapshotReport extends SnapshotReport {

	private List<String> shopNames;
	
	@Override
	protected List<Map<String, String>> getData(SnapshotReportReq req) {
		
		//get the shop info and save it.
		 Collection<Shop> shops = refdataService.getShopMap().values();
		
		shopNames = new ArrayList<String>();
		for (Shop s : shops) {
			shopNames.add(s.getShortName());
		}
		//add for trn
		shopNames.add("Trn");
				
		return super.getData(req);
	}
	
	@Override
	protected void populate(Map<String, String> row, Object obj) {

		//add additional populate store logic.
		InventoryCostRE icre = (InventoryCostRE) obj;
		
		//make a stupid hashmap because gae can't store hashmaps
		Map<String,Integer> shopInvCountMap = new HashMap<String,Integer>();
		
		for (ShopCount sc : icre.getShopInvCountSet()) {
		
			shopInvCountMap.put(sc.getName(),  sc.getCount());
		}
		
		
		for (String s : shopNames) {
			
			Integer countAtStore = shopInvCountMap.get(s);
			
			if (countAtStore == null) { countAtStore = new Integer(0);}
			row.put(s, countAtStore.toString());			
		}
		
		super.populate(row, obj);
	}
	
	public void setMapping(Map<String, String> mapping) {
		
		this.mapping = ReportUtils.addShopsToMapping(mapping, refdataService.getShopMap().values());		
		setHeader(this.mapping.keySet().toArray(new String[0]));
	}
	
}
