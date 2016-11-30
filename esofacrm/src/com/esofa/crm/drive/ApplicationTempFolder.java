package com.esofa.crm.drive;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.esofa.crm.service.ConfigService;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;

public class ApplicationTempFolder extends AbstractDriveFolder {
	private static final Logger logger = Logger.getLogger(ApplicationTempFolder.class.getName());
	
	private static final String APPLICATION_TEMP_FOLDER = "CPAP_TEMP_FOLDER";
	
	// TODO: potential to cache the temp folder at application level to minimise overhead of
	// fetching/creating folder
	private File applicationTempFolder = null;
	
	public ApplicationTempFolder(ConfigService config) {
		super(config);

	}

	@Override
	public File getFolder() {
		
		if (applicationTempFolder == null) {
			synchronized (this) {
				if (this.applicationTempFolder == null) {
					this.applicationTempFolder = DriveUtil.getExistingFolder(drive, ApplicationTempFolder.APPLICATION_TEMP_FOLDER);
			
					if (applicationTempFolder == null) {
						applicationTempFolder = DriveUtil.getFolder(drive, ApplicationTempFolder.APPLICATION_TEMP_FOLDER);
					}

					logger.severe("applicationTempFolder folder id:  " +applicationTempFolder.getId());
				} 
				
			}
		}

		return this.applicationTempFolder;
	}
	
	public File newTempFile()   throws IOException{
		File emptyFile = new File();
		emptyFile.setParents(Arrays.asList(new ParentReference().setId(getFolder().getId())));
		
		String tmpName = "temp_"  + new Date().getTime();
		emptyFile.setOriginalFilename(tmpName);
		emptyFile.setTitle(tmpName);		
		
		File existingFile = getFileByName(tmpName);
		File uploadedFile = null;
		if( existingFile == null ) {		
			uploadedFile = drive.files().insert(emptyFile).execute(); 
		}
		return uploadedFile;
	}
	
	public void clearFolder(String basefileName, String separator) throws IOException {
		
		List<File> contents = getFiles(basefileName, separator);
		
		for (File f : contents) {
			try {
				drive.files().delete(f.getId()).execute();
			} catch (IOException ioe) {
				logger.severe(ExceptionUtils.getStackTrace(ioe));				
			}
		}
	}	
}
