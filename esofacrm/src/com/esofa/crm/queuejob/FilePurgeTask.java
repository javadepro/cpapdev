package com.esofa.crm.queuejob;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.esofa.crm.drive.AbstractDriveFolder;
import com.esofa.crm.util.EsofaUtils;
import com.esofa.gae.queue.QueueableTask;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Children;
import com.google.api.services.drive.model.ChildList;
import com.google.api.services.drive.model.ChildReference;
import com.google.api.services.drive.model.File;

public class FilePurgeTask implements QueueableTask {

	private static final Logger log = Logger.getLogger(FilePurgeTask.class.getName());
	
	private static final String PARAM_FILE = "fileName";
	
	protected AbstractDriveFolder driveFolder;
	private int daysToKeep =3;

	
	@Override
	public Map<String, String> executeTask(Map<String, String> params) {
		
		if (!params.containsKey(PARAM_FILE)) {
			
			log.info("nothing to do");

			return null;
		}

		String fileName = params.get(PARAM_FILE);
		File folder = driveFolder.getFolder();
		Drive drive = driveFolder.getDrive();
		String folderId = folder.getId();
		
		if (StringUtils.equalsIgnoreCase("all", fileName)) {
			
			try {
				
				Children.List request = drive.children().list(folderId);
				ChildList reportFiles = request.execute();
				Date purgeDateTime = EsofaUtils.getDateBeforeNDays(-daysToKeep);

				for(ChildReference fileRef : reportFiles.getItems()) {
					
					File file = driveFolder.getFile(fileRef.getId());
					
					if (file.getModifiedDate().getValue() < purgeDateTime.getTime()) {

						log.info("Deleting file Id: " + fileRef.getId() + " " + file.getOriginalFilename());
				    	drive.children().delete(folderId, fileRef.getId()).execute();
					}	        	
				}
			} catch (IOException e) {
				log.log(Level.SEVERE, "", e);
				new RuntimeException(e);
			}
		} else {
			//delete a particular file
			File file;
			try {
				file = driveFolder.getFileByName(fileName);
				drive.children().delete(folderId, file.getId()).execute();
				log.info("Deleted file Id: " + file.getId() + " " + file.getOriginalFilename());

			} catch (IOException e) {
				log.log(Level.SEVERE, "", e);
				new RuntimeException(e);
			}
	    	
		}
		
		return null;
	
	}

	public void setDriveFolder(AbstractDriveFolder driveFolder) {
		this.driveFolder = driveFolder;
	}
	
	public void setDaysToKeep(int daysToKeep) {
		this.daysToKeep = daysToKeep;
	}
	
}
