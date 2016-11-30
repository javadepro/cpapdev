package com.esofa.crm.reports;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.esofa.crm.model.report.DataDumpReportReq;

public class DataDumpReportFacade {
	private List<CrmReport<DataDumpReportReq>> supportedReports;
	
	/**
	 * @param reportReq
	 * @return
	 * @throws IOException 
	 */
	public void generateReport(DataDumpReportReq reportReq, Writer writer) throws IOException {		
		for (CrmReport<DataDumpReportReq> report : supportedReports) {			
			if (StringUtils.equals(report.getDisplayName(), reportReq.getReportName())) {
				report.write(reportReq,writer);
				return;
			}
		}
	}
	
	public void setSupportedReports(List<CrmReport<DataDumpReportReq>> supportedReports) {
		this.supportedReports = supportedReports;		
	}	
}
