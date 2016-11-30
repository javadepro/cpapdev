package com.esofa.crm.reports.impl;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.crm.reports.CrmReport;
import com.esofa.crm.reports.MapToCsvWriter;
import com.esofa.crm.service.ConfigService;

public abstract class BaseCrmReport<T> implements CrmReport<T> {
	protected static final Logger log = Logger.getLogger(BaseCrmReport.class
			.getName());
	
	protected RefdataService refdataService;
	protected ConfigService configService;
	
	private String displayName;
	protected String[] header;
	protected Map<String,String> mapping;
	
	protected List<String> supportedCompanies;
	
	public BaseCrmReport() {}
	
	abstract protected List<Map<String,String>> getData(T req);
	
	public void write(T req, Writer writer) throws IOException {
	
		MapToCsvWriter mapToCsvWriter = new MapToCsvWriter();
		mapToCsvWriter.setHeader(getHeader()).setMapping(mapping).setWriter(writer).write(getData(req));
	}
	
	protected void populate ( Map<String,String> row, Object obj ) {
		
		MapToCsvWriter.populate(mapping, row, obj);
	}

	

	@Override
	public String getDisplayName() {
		return displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public boolean supportsCompanyMode(String mode) {
		
		if (supportedCompanies == null || supportedCompanies.size() <=0) {
			return true;
		}
		
		return supportedCompanies.contains(mode);
	}
	
	public String[] getHeader() {
		
		return header;
	}
		
	public void setHeader(String header) {
		this.header = org.springframework.util.StringUtils.tokenizeToStringArray(header, ",");
	}
	
	public void setHeader(String[] header) {
		this.header = header;
	}
	
	public void setMapping(Map<String, String> mapping) {
		this.mapping = mapping;
		setHeader(mapping.keySet().toArray(new String[0]));
	}
		
	public void setRefdataService(RefdataService refdataService) {
		this.refdataService = refdataService;
	}
	public void setConfigService(ConfigService configService) {
		this.configService = configService;
	}	
	
	public void setSupportedCompanies(List<String> supportedCompanies) {
		this.supportedCompanies = supportedCompanies;
	}
}
