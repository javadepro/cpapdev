package com.esofa.crm.model.report;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.validation.constraints.NotNull;

public class DateRangeReportReq implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5589038112684854744L;
	
	private Date fromDate;
	private Date endDate;
	
	@NotNull
	private String reportName;
	
	private Map<String,Object> params;
	
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
}
