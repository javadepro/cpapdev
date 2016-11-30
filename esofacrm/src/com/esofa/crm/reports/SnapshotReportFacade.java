package com.esofa.crm.reports;

import static com.esofa.crm.service.OfyService.ofy;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.esofa.crm.model.report.GeneratedReport;
import com.esofa.crm.model.report.SnapshotReportReq;
import com.esofa.crm.service.ConfigService;
import com.esofa.crm.util.EsofaUtils;
import com.googlecode.objectify.Key;

public class SnapshotReportFacade {

	private ConfigService configService;
	private List<CrmReport<SnapshotReportReq>> supportedReports;
	
	/**
	 * @param reportReq
	 * @return
	 * @throws IOException 
	 */
	public void generateReport(SnapshotReportReq reportReq, Writer writer) throws IOException {
		
		String reportName = ReportUtils.getReportName(reportReq.getReportId());
		
		for (CrmReport<SnapshotReportReq> report : supportedReports) {
			
			String displayName =StringUtils.replace(report.getDisplayName(), " ","");
			
			if (StringUtils.equals(displayName, reportName)) {
				report.write(reportReq,writer);
				return;
			}
		}
	}
	
	public List<GeneratedReport> getSnapshotReports() {
		List<GeneratedReport> generatedReports = ofy().load().type(GeneratedReport.class).order("-reportDate").list();
		return generatedReports;
	}
	
	public void setSupportedReports(List<CrmReport<SnapshotReportReq>> supportedReports) {
		this.supportedReports = supportedReports;			
	}
	
	public String createGeneratedReportEntry(String reportName, String reportClassName, Date snapshotDate, GeneratedReportTypeE reportDeliveryType) throws ClassNotFoundException {
		
		String reportId  = ReportUtils.generateReportId(reportName, snapshotDate);
		GeneratedReport gr = new GeneratedReport();
		gr.setReportDate(EsofaUtils.getMidnight(snapshotDate));
		gr.setReportId(reportId);
		gr.setReportName(reportName);
		gr.setReportClassName(reportClassName);
		gr.setType(reportDeliveryType.toString());
		
		GeneratedReport grFromDb = ofy().load().type(GeneratedReport.class).filter("reportId", reportId).first().now();
		if (grFromDb != null) {			
			//need to delete first data first
			//but report entry is already created
			List<? extends Key<?>> rowKeys = ofy().load().type(Class.forName(reportClassName)).filter("reportId", reportId).keys().list();
			ofy().delete().keys(rowKeys);
			
			//update generated timestamp
			gr = grFromDb;			
		} 
		
		gr.setGeneratedDateTime(new Date());
		ofy().save().entity(gr).now();
						
		return reportId;
	}
	
	public void deleteExistingEntries() {}	
	
	public void setConfigService(ConfigService configService) {
		this.configService = configService;
	}
	public String getCompanyMode() {
		return configService.getCompanyMode();
	}
}
