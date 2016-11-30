package com.esofa.crm.queuejob;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import com.esofa.crm.reports.GeneratedReportTypeE;
import com.esofa.crm.reports.ReportUtils;
import com.esofa.crm.reports.SnapshotReportFacade;
import com.esofa.crm.util.EsofaUtils;
import com.esofa.gae.queue.QueueableTask;

public abstract class BaseReportTask<T> implements QueueableTask  {

	protected final Logger log = Logger.getLogger(this.getClass().getName());

	
	protected static final String REQUEST_ID_PARAM = "rqIdParam";
	private static final String REPORT_ID_PARAM = "reportId";
	private static final String SNAPSHOTDATE_PARAM = "snapshotDateParam";
	
	protected static final String PARAM_END="END";
	protected static final String PARAM_COUNTER = "iterCount";
	protected static final String PARAM_CURSOR="cursor";
	protected static final String PARAM_FILENAME="fileName";
	protected static final String separator = "_";
	
	protected SnapshotReportFacade snapshotReportFacade;
	protected String reportName;
	protected String reportClassName;
	
	protected GeneratedReportTypeE reportDeliveryType;
	
	protected List<String> supportedCompanies;
 
	
	public BaseReportTask() {
		super();
	}

	protected abstract void initTask(Map<String, String> params);

	protected abstract Map<String, String> doTask(Map<String, String> params);

	protected abstract void executeOnComplete(Map<String, String> params);

	@Override
	public Map<String, String> executeTask(Map<String, String> params) {
	
		String rqId = params.get(REQUEST_ID_PARAM);
	
		// first iteration should not have rqId.
		// create one, and do initial setup.
		if (!supportsCompanyMode(snapshotReportFacade.getCompanyMode())) {
			
			log.log(Level.INFO, "bypassing report generating for " + this.reportName);

			return null;
		}
		
		if (StringUtils.isEmpty(rqId) ) {
	
			setRqId(params);
			initReportIdForTask(params);
			initTask(params);
	
			// clean up and return null to end iteration
		} 
	
		// actually do the work.
		// return params for next iteration. if no more work, next iteration
		// will be the last
		Map<String,String> processedParams = doTask(params);
		
		if (processedParams == null || processedParams.containsKey(PARAM_END)) {
		
			executeOnComplete(processedParams);
			processedParams = null;
		} 
		return processedParams;
	}

	protected void initReportIdForTask(Map<String, String> params) {
	
		Date snapshotDate = setSnapshotDate(params);		
		
		String reportId;
		try {
	
			reportId = snapshotReportFacade.createGeneratedReportEntry(
					reportName, reportClassName, snapshotDate,reportDeliveryType);
		} catch (ClassNotFoundException cnfe) {
			throw new RuntimeException(cnfe);
		}
	
		log.log(Level.INFO, "processing for report ID:" + reportId);
		params.put(REPORT_ID_PARAM, reportId);
	}

	protected Date setSnapshotDate(Map<String,String> params) {
		
		Date d=null;
		
		if (params.containsKey(SNAPSHOTDATE_PARAM) ) {
			
			String str_snapshotDate = params.get(SNAPSHOTDATE_PARAM);
			d = ReportUtils.stringToDate(str_snapshotDate);
		} else {
			
			d = EsofaUtils.getMidnight(); 
		}
		
		log.log(Level.INFO, "date to use: "  + d);
		return d;		
	}
	
	public boolean supportsCompanyMode(String mode) {

		if (supportedCompanies == null || supportedCompanies.size() <= 0) {
			return true;
		}

		return supportedCompanies.contains(mode);
	}

	protected void setRqId(Map<String, String> params) {
	
		String rqId = String.valueOf((new Date()).getTime());
		params.put(REQUEST_ID_PARAM, rqId);
	}
	
	public void setSnapshotReportFacade(SnapshotReportFacade snapshotReportFacade) {
		this.snapshotReportFacade = snapshotReportFacade;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public void setReportClassName(String reportClassName) {
		this.reportClassName = reportClassName;
	}

	public String getReportId(Map<String, String> params) {
		
		return params.get(REPORT_ID_PARAM);
		
	}
	public void setSupportedCompanies(List<String> supportedCompanies) {
		this.supportedCompanies = supportedCompanies;
	}
}