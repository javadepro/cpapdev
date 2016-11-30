package com.esofa.crm.drive;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.esofa.crm.service.ConfigService;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class DriveUtil {
	private static final Logger logger = Logger.getLogger(DriveUtil.class.getName());
	
	// GAE service account
	// DEV key
	//public static final String DEVELOPER_KEY = "AIzaSyBKSWWhtlr2hSfn9A1ygZzQxi0ONXz-A6M";
	
	// PROD key
	// public static final String DEVELOPER_KEY = "";
	
	//default serviceaccount info so that drive starts up if environment is not setup yet
	private static final String SERVICE_ACCOUNT_EMAIL_CONFIG_DEF="940509934739-j1aqj4gcbsek4hejroe1lpd5marvndna@developer.gserviceaccount.com";
	private static final String SERVICE_ACCOUNT_PKCS12_FILE_PATH_CONFIG_DEF="WEB-INF/misc/d9129b7e606a93649e00b88608fcf7d2679dea00-privatekey.p12";
	
	// cloud service account
	private static final String SERVICE_ACCOUNT_EMAIL_CONFIG_KEY = "DRIVE.SERVICEACCOUNT.EMAIL";
	private static final String SERVICE_ACCOUNT_PKCS12_FILE_PATH_CONFIG_KEY = "DRIVE.SERVICEACCOUNT.PKCS12PATH";
	
	// TODO may need to reconsider this implementation. possible problem if caller tries
	// to access the ACCESS_TOKEN before the drive service is initiated
	public static String ACCESS_TOKEN = "";
	
	private static Drive serviceAccount = null;
	
	public static Drive getDriveService(ConfigService config) 
			throws GeneralSecurityException,
				   IOException, 
				   URISyntaxException 
	{
		if( serviceAccount == null ) {
			serviceAccount = getCloudConsoleDriveService(config);
		}
		return serviceAccount;
	}
	
	private static Drive getCloudConsoleDriveService(ConfigService config)
			throws GeneralSecurityException,
				   IOException, 
				   URISyntaxException 
	{
		String _SERVICE_ACCOUNT_EMAIL = config.getConfigStringByName(SERVICE_ACCOUNT_EMAIL_CONFIG_KEY);
		if (StringUtils.isEmpty(_SERVICE_ACCOUNT_EMAIL)) {
			
			logger.info("service account from configService was empty");
			_SERVICE_ACCOUNT_EMAIL= SERVICE_ACCOUNT_EMAIL_CONFIG_DEF;
		}
		
		String _SERVICE_ACCOUNT_PKCS12_FILE_PATH = config.getConfigStringByName(SERVICE_ACCOUNT_PKCS12_FILE_PATH_CONFIG_KEY);
		if (StringUtils.isEmpty(_SERVICE_ACCOUNT_PKCS12_FILE_PATH)) {
			
			logger.info("service account file path from configService was empty");
			_SERVICE_ACCOUNT_PKCS12_FILE_PATH= SERVICE_ACCOUNT_PKCS12_FILE_PATH_CONFIG_DEF;
		}
		
		HttpTransport httpTransport = new NetHttpTransport();
		JacksonFactory jsonFactory = new JacksonFactory();
		
		Collection<String> driveScopes = new ArrayList<String>();
		driveScopes.add(DriveScopes.DRIVE);
		
		GoogleCredential credential = new GoogleCredential.Builder()
										  .setTransport(httpTransport)
										  .setJsonFactory(jsonFactory)
										  .setServiceAccountId(_SERVICE_ACCOUNT_EMAIL)
										  .setServiceAccountScopes(driveScopes)
										  .setServiceAccountPrivateKeyFromP12File(
												  new java.io.File(_SERVICE_ACCOUNT_PKCS12_FILE_PATH))
										  .build();

		ACCESS_TOKEN = credential.getAccessToken();
		if( ACCESS_TOKEN == null ) {
			if( credential.refreshToken() ) {
				ACCESS_TOKEN = credential.getAccessToken();
			}
		}
		
		Drive service = new Drive.Builder(httpTransport, jsonFactory, null)
								 .setHttpRequestInitializer(credential)
								 .build();
		
		serviceAccount = service;
		
		return service;
	}
		
	/**
	 * Obtain customer folder. If one is not already created, create one.
	 * 
	 * @param service
	 * @param custId
	 * @return
	 */
	public static File getCustomerFolder(Drive service, long custId) {
		return getFolder(service, Long.toString(custId));
	}
	
	// intentionally leave it in the package scope
	static File getFolder(Drive service, String folderName) {
		File folder = getExistingFolder(service,folderName);
			
		try {
			// create new folder if we can't find the folder with the specified name
			if( folder == null ) {
				folder = new File();
				folder.setTitle(folderName);
				folder.setOriginalFilename(folderName);
				folder.setMimeType("application/vnd.google-apps.folder");
				folder = service.files().insert(folder).execute();
				
				if( logger.isLoggable(Level.INFO)) {
					logger.info("Creating folder " + folder.getTitle() + " with id: " + folder.getId());
				}
			} else {
				if( logger.isLoggable(Level.INFO)) {
					logger.info("Using folder " + folder.getTitle() + " with id: " + folder.getId());
				}				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return folder;
	}
	
	
	// intentionally leave it in the package scope
	static File getExistingFolder(Drive service, String folderName) {
		File folder = null;
		Files.List request;
		try {
			request = service.files().list().setQ("mimeType = 'application/vnd.google-apps.folder'");
			
			do {
				FileList fileList = request.execute();
			
				// TODO may want to consider checking for files directly under root only
				for( File f : fileList.getItems() ) {
					String name = f.getTitle();
					if( name != null && name.equalsIgnoreCase(folderName)) {
						folder = f;
						break;
					}
				}
			} while (request.getPageToken() != null && request.getPageToken().length() > 0);
			
			
		} catch (IOException e) {
			logger.severe(ExceptionUtils.getStackTrace(e));
		}
		
		return folder;
	}
	
} 
