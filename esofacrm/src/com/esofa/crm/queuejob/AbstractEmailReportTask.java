package com.esofa.crm.queuejob;

import java.io.IOException;
import java.util.Map;

import com.esofa.crm.drive.ApplicationTempFolder;
import com.esofa.crm.reports.GeneratedReportTypeE;
import com.esofa.crm.service.ConfigService;
import com.esofa.crm.util.MailUtils;
import com.esofa.gae.queue.QueueableTask;

public abstract class AbstractEmailReportTask<T> extends BaseReportTask<T> implements QueueableTask {
	
	protected MailUtils mailUtils;
	protected ApplicationTempFolder tempFolder;
	protected ConfigService configService;

	protected Map<String,String> mapping;
	protected String[] header;
	
	protected String configToEmailAddrKey;
	
	public AbstractEmailReportTask() throws IOException {
		super();
		reportDeliveryType = GeneratedReportTypeE.Email;	
	}
	
	protected void initReportIdForTask(java.util.Map<String,String> params) {} 
	
	@Override
	protected void executeOnComplete(Map<String, String> params) {
	
		//mail it out.
	}

	public void setTempFolder(ApplicationTempFolder tempFolder) {
		this.tempFolder = tempFolder;
	}
	
	public void setMailUtils(MailUtils mailUtils) {
		this.mailUtils = mailUtils;
	}

	public void setConfigService(ConfigService configService) {
		this.configService = configService;
	}


	public String[] getHeader() {
		return header;
	}
	
	public void setHeader(String[] header) {
		this.header = header;
	}

	public void setMapping(Map<String, String> mapping) {
		this.mapping = mapping;
		setHeader(mapping.keySet().toArray(new String[0]));
	}
	
	public String getConfigToEmailAddrKey() {
		return configToEmailAddrKey;
	}
	public void setConfigToEmailAddrKey(String configToEmailAddrKey) {
		this.configToEmailAddrKey = configToEmailAddrKey;
	}
	
}
