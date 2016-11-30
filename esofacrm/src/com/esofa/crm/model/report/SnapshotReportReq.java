package com.esofa.crm.model.report;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class SnapshotReportReq implements Serializable {



	private static final long serialVersionUID = -4728395415532022487L;
	
	@NotNull
	private String reportId;

	public String getReportId() {
		return reportId;
	}
	
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
}
