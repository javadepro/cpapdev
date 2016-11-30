package com.esofa.crm.reports;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.esofa.crm.refdata.model.Shop;
import com.esofa.crm.util.EsofaUtils;

public class ReportUtils {

	public static final String STORE_DEFAULT = "DELETED_STORE";
	
	public static String generateReportId(String reportName, Date d ) {
		
		String val = StringUtils.replace(reportName, " ","");		
		return String.format("%s_%tY-%tm-%td", val,d,d,d);
		
	}
	
	public static String getReportName (String reportId) {
		return StringUtils.substringBefore(reportId, "_");
	}
	
	public static Date getDateFrom(String reportId) {
		
		String str_date = StringUtils.substringAfter(reportId, "_");
		
		return stringToDate(str_date);
	}
	
	/**
	 * support report format date only yyyy-MM-dd
	 * @param dateStr
	 * @return
	 */
	public static Date stringToDate(String str_date) {
		
		int year = Integer.parseInt(str_date.substring(0, 4));
		int month = Integer.parseInt(str_date.substring(5, 7));
		int day = Integer.parseInt(str_date.substring(8));
		Date d = DateUtils.setYears(new Date(), year);
		d = DateUtils.setMonths(d, month-1);
		d = DateUtils.setDays(d, day);
		return EsofaUtils.getMidnight(d);		
	}
	
	
	/**
	 * helper for adding shop names into the report col mappings
	 * @param mapping
	 */
	public static Map<String, String> addShopsToMapping(Map<String, String> mapping, Collection<Shop> shops) {
		
		Map<String,String> resultMap = new LinkedHashMap<String, String>(mapping.size());
		
		//handle shop names
		List<String> shopNames = new ArrayList<String>();
		for (Shop s : shops) {
			shopNames.add(s.getShortName());
		}
		shopNames.add(STORE_DEFAULT);
		//add for trn
		shopNames.add("Trn");
		
		//add shop names to mapping		
		for (Map.Entry<String,String> e : mapping.entrySet()) {
			
			if (StringUtils.equalsIgnoreCase("!shops", e.getKey())) {
				
				for (String s: shopNames) {
					
					resultMap.put(s, StringUtils.EMPTY);
				}
				
			} else {
				resultMap.put(e.getKey(), e.getValue());
			}			
		}		

		return resultMap;
	}
	
	
}
