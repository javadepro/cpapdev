package com.esofa.crm.drive;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.gmr.web.multipart.GMultipartFile;

import com.esofa.crm.service.ConfigService;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Children;
import com.google.api.services.drive.model.ChildList;
import com.google.api.services.drive.model.ChildReference;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;

public abstract class AbstractDriveFolder {
	
	private static final Logger logger = Logger.getLogger(AbstractDriveFolder.class.getName());
	protected Drive drive = null;	
	
	
	
	public abstract File getFolder();
	
	protected AbstractDriveFolder(ConfigService config) {
		try {
			this.drive = DriveUtil.getDriveService(config);
		} catch(Exception e) {
			logger.severe("Cannot acquire Drive service.");
			logger.severe(ExceptionUtils.getStackTrace(e));
		}
	}
	
	public Drive getDrive() {
		return drive;
	}
	
	/**
	 * Add the GMultipartFile to the Drive folder represented by this folder object. If a file with
	 * the same title already exist, it will update the existing file instead.
	 * @param file GMultipartFile. Expecting to have at least the original filename, content type, 
	 * and input stream for the content.
	 * @return Drive file id of the newly added file
	 * @throws IOException
	 */
	public String addOrUpdateFile(GMultipartFile file) throws IOException {


		return addOrUpdateFile(file.getInputStream(), file.getContentType(), file.getOriginalFilename());
	}
	
	/**
	 * @param file GMultipartFile. Expecting to have at least the original filename, content type, 
	 * and input stream for the content.
	 * @return Drive file id of the newly added file
	 * @throws IOException
	 */
	public String addOrUpdateFile(InputStream is, String contentType, String originalFileName) throws IOException {
		File inFile = new File();
		inFile.setParents(Arrays.asList(new ParentReference().setId(getFolder().getId())));
		inFile.setOriginalFilename(originalFileName);
		inFile.setTitle(originalFileName);		
		
		File existingFile = getFileByName(originalFileName);
		File uploadedFile = null;
		if( existingFile == null ) {		
			uploadedFile = drive
						   .files()
						   .insert(inFile,
								   new InputStreamContent(contentType,is))
						   .execute();
		} else {
			uploadedFile = drive
						   .files()
						   .update(existingFile.getId(), 
								   inFile,
								   new InputStreamContent(contentType,is))
						   .execute();
		}
		return uploadedFile.getId();
	}
	
	
	
	public void deleteFile(String fileId) throws IOException {		
		drive.files().delete(fileId).execute();
	}
	
	public File getFile(String fileId) throws IOException {
		return drive.files().get(fileId).execute();		
	}

	public InputStream getFileAsInputStream(File fileToGet) throws IOException {
						
		HttpResponse resp = getDrive()
				.getRequestFactory()
				.buildGetRequest(
						new GenericUrl(fileToGet.getDownloadUrl()))
				.execute();
		return resp.getContent();
	}
		
	public InputStream getFileAsInputStream(String fileId) throws IOException {
		
		File fileToGet = getFile(fileId);		
		return getFileAsInputStream(fileToGet);
	}
	
	public File getFileByName(String fileName) throws IOException {
		List<File> filesInFolder = getFolderContents();		
		File target = null;
		
		for( File f : filesInFolder ) {
			String name = f.getTitle();
			if( name != null && name.equalsIgnoreCase(fileName)) {
				target = f;
			}
		}
		return target;
	}
	
	public List<File> getFolderContents() {
		File folder = this.getFolder();
		List<File> contents = new ArrayList<File>();
		
		Children.List request = null;
		try {	
			request = drive.children().list(folder.getId());
			do {			
				ChildList children = request.execute();
		
				for( ChildReference child : children.getItems() ) {
					File file = getFile(child.getId());
					contents.add(file);			
				}
				request.setPageToken(children.getNextPageToken());
	    	} while (request.getPageToken() != null && request.getPageToken().length() > 0);
		} catch( IOException e ) {
			if( request != null ) request.setPageToken(null);
		}

		Collections.sort(contents, new FileComparator());
		return contents;
	}
	
	public List<File> getFiles(String basefileName, String separator) throws IOException {
		List<File> filesInFolder = getFolderContents();		
		List<File> filtered = new ArrayList<>();
		
		for( File f : filesInFolder ) {
			String name = f.getTitle();
			if( name != null  && StringUtils.equals(basefileName,StringUtils.substringBeforeLast(name, separator))) {
				filtered.add(f);
			}
		}
		return filtered;
	}
}
