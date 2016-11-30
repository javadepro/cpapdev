package com.esofa.crm.drive;

import java.util.logging.Logger;

import com.esofa.crm.service.ConfigService;
import com.google.api.services.drive.model.File;

public class DataDumpFolder extends AbstractDriveFolder {

	private static final Logger logger = Logger.getLogger(DataDumpFolder.class
			.getName());

	
	private static final String FOLDER_NAME = "CPAP_REPORT_FOLDER";
	
	private File reportFolder = null;
	
	protected DataDumpFolder(ConfigService config) {
		super(config);
	}

	@Override
	public File getFolder() {
		
		if (reportFolder == null) {
			synchronized (this) {
				
				if (reportFolder == null) {
					reportFolder = DriveUtil.getExistingFolder(drive, DataDumpFolder.FOLDER_NAME);
					
					
					
					if (reportFolder == null) {
						reportFolder = DriveUtil.getFolder(drive, DataDumpFolder.FOLDER_NAME);
					}

					logger.severe("datadump folder id:  " +reportFolder.getId());
				}
			}
		}
		return this.reportFolder;
	}
	
	

	

}
