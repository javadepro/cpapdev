package com.esofa.crm.reports;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.BeanUtilsBean2;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.DateTimeConverter;
import org.apache.commons.lang3.StringUtils;

import au.com.bytecode.opencsv.CSVWriter;

import com.esofa.crm.reports.impl.BaseCrmReport;

/**
 * write from list of maps to csv
 * 
 * style wise diff....just seeing what the hype is all about
 * for this type of method chaining.
 * @author JHa
 *
 */
public class MapToCsvWriter {


	protected static final Logger log = Logger.getLogger(MapToCsvWriter.class
			.getName());

	private static BeanUtilsBean beanUtils;
	
	private String[] header;
	protected Map<String,String> mapping;
	private Writer writer;
	private boolean includeHeader;

	static {
		// classloader level thing really...
		BeanUtilsBean.setInstance(new BeanUtilsBean2());

		// restrict custom conversion for report class only to avoid behaviour change to other area
		DateTimeConverter converter = new DateConverter();
		converter.setPattern("dd/MM/yyyy");

		beanUtils = BeanUtilsBean.getInstance();
		beanUtils.getConvertUtils().register(converter, java.util.Date.class);
	}
	
	public MapToCsvWriter() {
		includeHeader=true;
	}
	
	//helper method for mapping the row.
	public static void populate (Map<String,String> mapping, Map<String,String> row, Object obj ) {		
		for(Map.Entry<String,String> entry : mapping.entrySet()) {
			
			//if blank, then entry is not avail from obj, requires custom logic.
			if (StringUtils.isNotEmpty(entry.getValue() )) {			
				try {										
					String value = beanUtils.getProperty(obj, entry.getValue());
					row.put(entry.getKey(), value);				
				} catch (IllegalAccessException | InvocationTargetException | org.apache.commons.beanutils.NestedNullException e) {
					log.warning(e.getMessage());
				} catch (NoSuchMethodException  e) {
					//do nothing
				}
			}
		}
	}
	
	public MapToCsvWriter setHeader(String[] header) {
		this.header = header;
		return this;
	}
	

	public MapToCsvWriter setMapping(Map<String, String> mapping) {
		this.mapping = mapping;
		return setHeader(mapping.keySet().toArray(new String[0]));
	}
	
	public MapToCsvWriter setWriter(Writer writer) {
		this.writer = writer;
		return this;
	}
	
	public MapToCsvWriter setIncludeHeader(boolean shouldInclude) {
		
		this.includeHeader = shouldInclude;
		return this;
	}
	
	private String[] getHeader() {
		
		return header;
	}
	
	public void write(List<Map<String,String>> data) throws IOException {
		
		CSVWriter csv = new CSVWriter(writer);
		if (includeHeader) {csv.writeNext(getHeader()); }
		writeBody(data,csv);

		csv.flush();
		writer.flush();
		csv.close();
		
	}
	
	protected void writeBody(List<Map<String,String>> data, CSVWriter writer) {

		if (data == null || data.size() ==0 ) {  return; }

		for (Map<String,String> rawRow : data) {
			
			String[] rowArr = new String[mapping.size()];
			int counter =0;
			
			for (String key : mapping.keySet()) {

				rowArr[counter] = rawRow.get(key);
				counter++;				
			}
			
			writer.writeNext(rowArr);
		}
	}
	
	
	
	
	
}