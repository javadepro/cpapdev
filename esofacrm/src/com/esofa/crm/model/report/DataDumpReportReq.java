package com.esofa.crm.model.report;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class DataDumpReportReq implements Serializable {
	private static final long serialVersionUID = 5589038112684854744L;
	
	@NotNull
	private String reportName;
		
	public String getReportName() {
		return reportName;
	}
	
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
}
