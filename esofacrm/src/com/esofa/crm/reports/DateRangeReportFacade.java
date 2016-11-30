package com.esofa.crm.reports;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import com.esofa.crm.model.report.DateRangeReportReq;
import com.esofa.crm.service.ConfigService;

public class DateRangeReportFacade implements InitializingBean {	
	
	private ConfigService configService;
	private List<CrmReport<DateRangeReportReq>> supportedReports;
	private List<String> supportedReportNames;
	

	protected void init() {

		String companyName = configService.getCompanyMode();
		
		for (CrmReport report : supportedReports) {
		
			if (report.supportsCompanyMode(companyName)) {
			
				supportedReportNames.add(report.getDisplayName());
			}
			
		}
	}
	
	/**
	 * @param reportReq
	 * @return
	 * @throws IOException 
	 */
	public void generateReport(DateRangeReportReq reportReq, Writer writer) throws IOException {
		
		for (CrmReport<DateRangeReportReq> report : supportedReports) {
			
			if (StringUtils.equals(report.getDisplayName(), reportReq.getReportName())) {
				report.write(reportReq,writer);
				return;
			}
		}
	}
	
	public void setSupportedReports(List<CrmReport<DateRangeReportReq>> supportedReports) {
		this.supportedReports = supportedReports;			
		setSupportedReportNames(supportedReports);		
	}
	
	private void setSupportedReportNames(List<CrmReport<DateRangeReportReq>> supportedReports) {
		
		if (supportedReports == null) { return;}
		supportedReportNames = new ArrayList(supportedReports.size());
		
		
	}
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		init();
		
	}
	
	public List<String> getSupportedReportNames() {
		return supportedReportNames;
	}
	
	public void setConfigService(ConfigService configService) {
		this.configService = configService;
	}
	
}
