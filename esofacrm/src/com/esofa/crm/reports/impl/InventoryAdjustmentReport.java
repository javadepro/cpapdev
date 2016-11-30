package com.esofa.crm.reports.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.esofa.crm.model.AuditEntry;
import com.esofa.crm.model.AuditEntryTypeE;
import com.esofa.crm.model.report.DateRangeReportReq;
import com.esofa.crm.security.user.model.CrmUser;
import com.esofa.crm.service.InventoryAuditService;
import com.esofa.crm.service.UserService;
import com.googlecode.objectify.Key;

public class InventoryAdjustmentReport extends BaseCrmReport<DateRangeReportReq> {

	private InventoryAuditService inventoryAuditService;
	private UserService userService;
	
	@Override
	protected List<Map<String, String>> getData(DateRangeReportReq req) {
		

		Date fromDate = req.getFromDate();
		Date endDate = req.getEndDate();
		List<String> entryTypes = new ArrayList<String>();
		entryTypes.add(AuditEntryTypeE.INVENTORY_UPDATE.toString());
		
		List<AuditEntry> rawResults = inventoryAuditService.getAll(entryTypes, fromDate, endDate);
		Map<Key<CrmUser>,CrmUser> userMap = userService.getCrmUserMap(false);
		List<Map<String,String>> resultList = new ArrayList<Map<String,String>>();
		
		for (AuditEntry i : rawResults) {
		
			Map<String,String> row = new HashMap<String,String>();					
			
			populate(row,i);
			
			if (userMap.containsKey(i.getUser())) {
				
				CrmUser user = userMap.get(i.getUser());
				
				row.put("User Name", user.getName());
				row.put("User First Name", user.getFirstname());
				row.put("User Last Name", user.getLastname());
			}
			
			
			resultList.add(row);
		}
		
		return resultList;
	}


	public void setInventoryAuditService(
			InventoryAuditService inventoryAuditService) {
		this.inventoryAuditService = inventoryAuditService;
	}
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	

}
