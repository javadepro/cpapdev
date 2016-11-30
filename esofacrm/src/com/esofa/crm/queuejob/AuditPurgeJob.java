package com.esofa.crm.queuejob;

import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

import com.esofa.crm.service.AuditService;
import com.esofa.crm.service.ConfigService;
import com.esofa.crm.util.EsofaUtils;
import com.esofa.gae.queue.QueueableTask;

public class AuditPurgeJob implements QueueableTask {
	private static final Logger log = Logger.getLogger(AuditPurgeJob.class.getName());
	
	private AuditService auditService;

	private ConfigService configService;
	
	@Override
	public Map<String, String> executeTask(Map<String, String> params) {		
		log.info("delete old audit entries start");
		int dayLimit =configService.getConfigIntByName("AUDIT.KEEP.DAYS");
		
		Date olderThan = EsofaUtils.getDateBeforeNDays(dayLimit);
		int deletedRecords = auditService.delete(olderThan);
		log.info("delete old audit entries done : deleted " + deletedRecords + " records older than " + olderThan);		
		return null;
	}
	
	public void setAuditService(AuditService auditService) {
		this.auditService = auditService;
	}
	public void setConfigService(ConfigService configService) {
		this.configService = configService;
	}
}
