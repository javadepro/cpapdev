package com.esofa.crm.queuejob;

import java.io.IOException;
import java.util.Map;

import com.esofa.crm.drive.ApplicationTempFolder;
import com.esofa.crm.drive.DataDumpFolder;
import com.esofa.crm.util.MailUtils;
import com.esofa.gae.queue.QueueableTask;

public abstract class DataDumpReportTask<T> extends BaseReportTask<T> implements QueueableTask {
	
	protected MailUtils mailUtils;
	protected DataDumpFolder dataDumpFolder;
	protected ApplicationTempFolder tempFolder;
	
	protected Map<String,String> mapping;
	protected String[] header;
	
	public DataDumpReportTask() throws IOException {
		super();
		
	}
	
	protected void initReportIdForTask(Map<String, String> params) {
		//empty
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
	
	public void setDataDumpFolder(DataDumpFolder dataDumpFolder) {
		this.dataDumpFolder = dataDumpFolder;
	}
	
	public void setTempFolder(ApplicationTempFolder tempFolder) {
		this.tempFolder = tempFolder;
	}
	
	public void setMailUtils(MailUtils mailUtils) {
		this.mailUtils = mailUtils;
	}
	
}
