package com.esofa.crm.model.util;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class CustomerPdfMapperProvider {

	private Map<String,PdfMapper> pdfMapperMap;
	
	public PdfMapper getMapper(String reportName) {
		
		return pdfMapperMap.get(reportName);
	}
	
	public void setPdfMapperMap(Map<String, PdfMapper> pdfMapperMap) {
		this.pdfMapperMap = pdfMapperMap;
	}
	
	
}
