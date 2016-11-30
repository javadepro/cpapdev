package com.esofa.crm.model.report;

import java.util.Date;

public interface ReportEntry {

	public String getReportName();

	public void setReportName(String reportName);

	public Date getSnapshotDate();

	public void setSnapshotDate(Date snapshotDate);

	public String getReportId();

	public void setReportId(String reportId);

}