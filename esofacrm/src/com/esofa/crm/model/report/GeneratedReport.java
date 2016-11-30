package com.esofa.crm.model.report;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
@Index
public class GeneratedReport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9062374510833531314L;
	public enum Type {
		SNAPSHOT
	}
	
	@Id
	private Long id;
	
	private String reportId;
	
	private Date generatedDateTime;
	private String type;
	private String reportName;
	private String reportClassName;
	private Date reportDate;
	
	public GeneratedReport() {
		generatedDateTime = new Date();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public Date getGeneratedDateTime() {
		return generatedDateTime;
	}
	public void setGeneratedDateTime(Date generatedDateTime) {
		this.generatedDateTime = generatedDateTime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getReportDate() {
		return reportDate;
	}
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	
	public String getReportName() {
		return reportName;
	}

	public void setReportClassName(String reportClassName) {
		this.reportClassName = reportClassName;
	}
	
	public String getReportClassName() {
		return reportClassName;
	}
}
