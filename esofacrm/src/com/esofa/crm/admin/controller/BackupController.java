package com.esofa.crm.admin.controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.esofa.crm.service.ConfigService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.File.Labels;
import com.google.api.services.drive.model.ParentReference;
import com.google.drive.ClientFile;
import com.google.drive.CredentialMediator;
import com.google.drive.CredentialMediator.InvalidClientSecretsException;


@Controller
@RequestMapping(value = "/admin/backup")
public class BackupController {

	
	public final String GOOGLE_DRIVE_FOLDER_MIME_TYPE ="application/vnd.google-apps.folder";
	
	private static final Logger log = Logger
			.getLogger(AdminConfigController.class.getName());

	@Autowired
	private ConfigService configService;
	
	@RequestMapping(value = { "", "/"}, method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) {
		ModelAndView mav = new ModelAndView();
		String message = "";
		try{
			File body = new File();
			body.setTitle("backup.csv");
			body.setDescription("backup file");
			body.setMimeType("text/csv");

			String csv="'col1','col2'";
			
			ClientFile cf = new ClientFile();
			cf.setContent("'col1','col2'");
			cf.setDescription("backup file");
			cf.setEditable(true);
			Labels labels = new Labels();
			labels.setStarred(false);
			cf.setLabels(labels);
			cf.setMimeType("text/csv");
			List<ParentReference> parents = new ArrayList<ParentReference>();
			ParentReference p = new ParentReference();
			p.setId("root");
			parents.add(p);
			cf.setParents(parents);
			cf.setTitle("backup-test.csv");
			
			Drive drive =  getDriveService(req, resp);
			Drive.Files.Insert insert = drive.files().insert(body,ByteArrayContent.fromString(cf.mimeType, cf.content) );
			insert.getMediaHttpUploader().setChunkSize(1024 * 1024);
			File file = insert.execute();
			
			message += "added to drive";
					
		}catch(Exception ex){
			message += ex.toString();
		}
		mav.addObject("message", message);
		mav.setViewName("message");
		return mav;
	}
	
	@RequestMapping(value = { "full", "/full", "/full/"}, method = RequestMethod.GET)
	private ModelAndView fullBackUp(HttpServletRequest req, HttpServletResponse resp){
		ModelAndView mav = new ModelAndView();
		
		String message = "";
		try{
		
			//Create Folder
			Drive drive =  getDriveService(req, resp);
			File folder = new File();
			folder.setTitle(createFolderName());
			folder.setDescription("Backup");
			folder.setMimeType(GOOGLE_DRIVE_FOLDER_MIME_TYPE);
			Drive.Files.Insert insert = drive.files().insert(folder);
			message +="Created folder\n";
			
			//Queue all backup files
			message +="Queued folder\n";
			
			
		}catch(Exception ex){
			message+= ex.toString();
			ex.printStackTrace();
		}
		
		mav.addObject("message", message);
		mav.setViewName("message");
		return mav;
	}
	
	private String createFolderName(){
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(today)+"-backup";
	}
	
	 /**
	   * Build and return a Drive service object based on given request parameters.
	   *
	   * @param req Request to use to fetch code parameter or accessToken session
	   *            attribute.
	   * @param resp HTTP response to use for redirecting for authorization if
	   *             needed.
	   * @return Drive service object that is ready to make requests, or null if
	   *         there was a problem.
	   */
	  private Drive getDriveService(HttpServletRequest req,
	      HttpServletResponse resp) {
	    Credential credentials = getCredential(req, resp);

	    return new Drive.Builder(TRANSPORT, JSON_FACTORY, credentials).build();
	  }
	  
	 
	  protected static final HttpTransport TRANSPORT = new NetHttpTransport();
	  protected static final JsonFactory JSON_FACTORY = new JacksonFactory();

	  /**
	   * Default MIME type of files created or handled by DrEdit.
	   *
	   * This is also set in the Google APIs Console under the Drive SDK tab.
	   */
	  public static final String DEFAULT_MIMETYPE = "text/plain";

	  /**
	   * MIME type to use when sending responses back to DrEdit JavaScript client.
	   */
	  public static final String JSON_MIMETYPE = "application/json";

	  /**
	   * Path component under war/ to locate client_secrets.json file.
	   */
	  public static final String CLIENT_SECRETS_FILE_PATH
	      = "/WEB-INF/client_secrets.json";

	  /**
	   * Scopes for which to request access from the user.
	   */
	  public static final List<String> SCOPES = Arrays.asList(
	      // Required to access and manipulate files.
	      "https://www.googleapis.com/auth/drive.file",
	      // Required to identify the user in our data store.
	      "https://www.googleapis.com/auth/userinfo.email",
	      "https://www.googleapis.com/auth/userinfo.profile");

	  protected void sendError(HttpServletResponse resp, int code, String message) {
	    try {
	      resp.sendError(code, message);
	    } catch (IOException e) {
	      throw new RuntimeException(message);
	    }
	  }

	  protected InputStream getClientSecretsStream(HttpServletRequest req) {
		  
	    return req.getSession().getServletContext().getResourceAsStream(CLIENT_SECRETS_FILE_PATH);
	  }

	  protected CredentialMediator getCredentialMediator(
	      HttpServletRequest req, HttpServletResponse resp) {
	    // Authorize or fetch credentials.  Required here to ensure this happens
	    // on first page load.  Then, credentials will be stored in the user's
	    // session.
	    CredentialMediator mediator;
	    try {
	      mediator = new CredentialMediator(req, getClientSecretsStream(req), SCOPES);
	      mediator.getActiveCredential();
	      return mediator;
	    } catch (CredentialMediator.NoRefreshTokenException e) {
	      try {
	        resp.sendRedirect(e.getAuthorizationUrl());
	      } catch (IOException ioe) {
	        throw new RuntimeException("Failed to redirect user for authorization");
	      }
	      throw new RuntimeException("No refresh token found. Re-authorizing.");
	    } catch (InvalidClientSecretsException e) {
	      String message = String.format(
	          "This application is not properly configured: %s", e.getMessage());
	      sendError(resp, 500, message);
	      throw new RuntimeException(message);
	    } catch (IOException e) {
	      String message = String.format(
	          "An error happened while reading credentials: %s", e.getMessage());
	      sendError(resp, 500, message);
	      throw new RuntimeException(message);
	    }
	  }

	  protected Credential getCredential(
	      HttpServletRequest req, HttpServletResponse resp) {
	    try {
	      CredentialMediator mediator = getCredentialMediator(req, resp);
	      return mediator.getActiveCredential();
	    } catch(CredentialMediator.NoRefreshTokenException e) {
	      try {
	        resp.sendRedirect(e.getAuthorizationUrl());
	      } catch (IOException ioe) {
	        ioe.printStackTrace();
	        throw new RuntimeException("Failed to redirect for authorization.");
	      }
	    } catch (IOException e) {
	      String message = String.format(
	          "An error happened while reading credentials: %s", e.getMessage());
	      sendError(resp, 500, message);
	      throw new RuntimeException(message);
	    }
	    return null;
	  }

	  protected String getClientId(
	      HttpServletRequest req, HttpServletResponse resp) {
	    return getCredentialMediator(req, resp).getClientSecrets().getWeb()
	        .getClientId();
	  }

	  protected void deleteCredential(HttpServletRequest req, HttpServletResponse resp) {
	    CredentialMediator mediator = getCredentialMediator(req, resp);
	    try {
	      mediator.deleteActiveCredential();
	    } catch (IOException e) {
	      String message = String.format(
	          "An error happened while reading credentials: %s", e.getMessage());
	      sendError(resp, 500, message);
	      throw new RuntimeException(message);
	    }
	  }
}
