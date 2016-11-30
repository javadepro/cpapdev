package com.esofa.crm.reports.impl;

import static com.esofa.crm.service.OfyService.ofy;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.esofa.crm.model.report.SnapshotReportReq;
import com.esofa.crm.reports.ReportUtils;

public class SnapshotReport extends BaseCrmReport<SnapshotReportReq> {

	protected String clazz;
	
	@Override
	protected List<Map<String, String>> getData(SnapshotReportReq req) {		
		Date endDate = ReportUtils.getDateFrom(req.getReportId());
		
		List rawResults;
		
		try {		
			rawResults = ofy().load().type(Class.forName(clazz)).filter("snapshotDate", endDate).list();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		List<Map<String,String>> resultList = new ArrayList<Map<String,String>>();		

		for (Object entry : rawResults) {		
			Map<String,String> row = new HashMap<String,String>();
			populate(row,entry);
			resultList.add(row);
		}
		
		return resultList;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
}
