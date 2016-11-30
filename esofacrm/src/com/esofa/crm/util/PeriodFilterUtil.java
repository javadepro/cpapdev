package com.esofa.crm.util;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;

public class PeriodFilterUtil {

	private static Map<Integer, String> periodFilter;
	private static Map<Integer, String> periodsFilterFuture;
	private static Map<Integer, String> periodsFilter6Mths;
	
	private static int monthsToShow = 12;
	
	static {
		periodFilter = new LinkedHashMap<Integer, String>();
		periodFilter.put(9999, "All");
		periodFilter.put(30, "Last 30 Days");
		periodFilter.put(60, "Last 60 Days");
		periodFilter.put(90, "Last 90 Days");
		periodFilter.put(180, "Last 180 Days");
		periodFilter.put(365, "Last 365 Days");
		periodFilter.put(0, "Today");
		
		periodsFilterFuture = new LinkedHashMap<Integer, String>();
		periodsFilterFuture.put(0, "Today");
		periodsFilterFuture.put(7, "Next 7 Days");
		periodsFilterFuture.put(14, "Next 14 Days");
		periodsFilterFuture.put(30, "Next 30 Days");
		periodsFilterFuture.put(60, "Next 60 Days");
		periodsFilterFuture.put(90, "Next 90 Days");
		periodsFilterFuture.put(9999, "All");
		
		periodsFilter6Mths = new LinkedHashMap<Integer, String>();
		periodsFilter6Mths.put(0, "Today");
		periodsFilter6Mths.put(7, "Last 7 Days");
		periodsFilter6Mths.put(14, "Last 14 Days");
		periodsFilter6Mths.put(30, "Last 30 Days");
		periodsFilter6Mths.put(60, "Last 60 Days");
		periodsFilter6Mths.put(90, "Last 90 Days");
		periodsFilter6Mths.put(120, "Last 120 Days");
		periodsFilter6Mths.put(150, "Last 150 Days");
		periodsFilter6Mths.put(180, "Last 180 Days");
		periodsFilter6Mths.put(9999, "All");
	}

	public static Map<Integer, String> getPeriodFilter() {
	 
		return periodFilter;
	}

	public static Map<Integer, String> getPeriodFilterFuture() {
 
		return periodsFilterFuture;
	}

	public static Map<Integer, String> getPeriodFilter6Mths() {
		 
		return periodsFilter6Mths;
	}

	public static int getFirstMonthOfYear() {
		
		Date d = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		return -1* cal.get(Calendar.MONTH);
	}
	
	public static Map<Integer, String> getRollingFilter() {
		
		
		Map<Integer, String> monthFilter = new LinkedHashMap<Integer, String>();
		Calendar cal = Calendar.getInstance();
						
		for (int i= 0; i < monthsToShow; i++) {
			Date month = DateUtils.addMonths(new Date(), -i);
			
			cal.setTime(month);
			StringBuilder displayMonth = new StringBuilder(cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
			displayMonth.append(" ").append( cal.get(Calendar.YEAR));
			
			//display as: MONTH + YEAR
			monthFilter.put(-i,displayMonth.toString());		
		}
		
		return monthFilter;
	}
	
}
