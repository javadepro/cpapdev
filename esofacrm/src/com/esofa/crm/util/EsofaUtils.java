package com.esofa.crm.util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;


public class EsofaUtils {	
	
	private static String[] expectedFormat = {"yyyyMMdd"};
	
	public static Date getDateAfterNDays(int day){
		Calendar c = Calendar.getInstance();
		//c.setTime(date);
		c.add(Calendar.DATE, day);
		Date d = c.getTime();
		d = getMidnight(d);
		return d;
	}
	
	public static Date getDateBeforeNDays(int day){
		Calendar c = Calendar.getInstance();
		//c.setTime(date);
		c.add(Calendar.DATE, -day);
		Date d = c.getTime();
		d = getMidnight(d);
		return d;
	}
	
	public static Date getDateAdjustedByMonth(int mth) {
		return getDateAdjustedByMonth(null, mth);
	}
	
	public static Date getDateAdjustedByMonth(Date d, int mth) {
		Calendar c = Calendar.getInstance();		
		if (d != null) { c.setTime(d);}
		
		c.add(Calendar.MONTH, mth);
		Date adjDate = c.getTime();
		adjDate = getMidnight(adjDate);
		return adjDate;
	}
		
	public static Date getFirstDateOfMonth(Date d) { 
		
		Date floorDate = DateUtils.setDays(d, 1);
		floorDate= getMidnight(floorDate);
		return floorDate;
	}

	public static Date getLastDateOfMonth(Date d) { 
		Date ceilingDate =  DateUtils.addMonths(d,1);
		ceilingDate =  DateUtils.setDays(ceilingDate, 1);
		ceilingDate= getMidnight(ceilingDate);
		return ceilingDate;
	}

	public static Date getMidnight() {
		
		return getMidnight(new Date());
	}
	
	public static Date getMidnight(Date d) {
		
		Date midnightDate = d;
		midnightDate= DateUtils.setHours(midnightDate, 0);
		midnightDate= DateUtils.setMinutes(midnightDate, 0);
		midnightDate= DateUtils.setSeconds(midnightDate, 0);
		midnightDate= DateUtils.setMilliseconds(midnightDate, 0);
		return midnightDate;
	}
	
	public static String getDateFormatted(Date d) {
		
		return String.format("%td/%tm/%tY", d,d,d);
	}

	public static boolean isWithinNDays(Date d, int days) {
		
		Date adjInvoiceDate = DateUtils.addDays(d,days);
		Calendar invoiceDateCal = Calendar.getInstance();
		invoiceDateCal.setTime(adjInvoiceDate);
		
		Date today = getMidnight();
		Calendar todayCal = Calendar.getInstance();
		todayCal.setTime(today);
		
		if (invoiceDateCal.after(todayCal) || DateUtils.isSameDay(invoiceDateCal, todayCal)) {
			
			return true;
		}
		
		
		return false;
	}
	
	public static boolean isOverNDays (Date d, int days) {
		
		Date adjInvoiceDate = DateUtils.addDays(d,days);
		Calendar invoiceDateCal = Calendar.getInstance();
		invoiceDateCal.setTime(adjInvoiceDate);
		
		Date today = getMidnight();
		Calendar todayCal = Calendar.getInstance();
		todayCal.setTime(today);
		
		if (invoiceDateCal.before(todayCal)) {
			
			return true;
		}
		
		
		return false;
	}

	//yyyyMMdd format expected
	public static Date getDateFrom(String s ) throws ParseException {
				
		Date d= DateUtils.parseDate(s, expectedFormat);		
		return getMidnight(d);
	}
}
