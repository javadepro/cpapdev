package com.esofa.crm.queuejob;

import static com.esofa.crm.service.OfyService.ofy;

import java.util.Map;

import com.esofa.crm.model.report.ReportEntry;
import com.esofa.crm.reports.GeneratedReportTypeE;
import com.esofa.crm.reports.ReportUtils;
import com.esofa.gae.queue.QueueableTask;

public abstract class AbstractOnlineReportTask<T> extends BaseReportTask<T> implements QueueableTask {

	
	public AbstractOnlineReportTask() {
		super();
		reportDeliveryType = GeneratedReportTypeE.Online;
	}
	
	protected void setReInfo(ReportEntry re, Map<String,String>params ) {
		
		String reportId = getReportId(params);
		re.setReportId(reportId);
		re.setSnapshotDate(ReportUtils.getDateFrom(reportId));
		re.setReportName(reportName);
	}

	protected void saveRE(T entry) {
		ofy().save().entity(entry).now();
	}
}
