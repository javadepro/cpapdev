package com.esofa.crm.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class MailUtils {
	
	private static final Logger logger = Logger.getLogger(MailUtils.class.getName());

	private String defaultToAddress;
	private String fromAddress;
	Properties props = null;
	
	
	public MailUtils() {
		props = new Properties();
	}
	
	
	public void sendMail( String subject, String msgBody) {
				
		sendMail(defaultToAddress, subject, msgBody, null,null,null);
	}
	
	public void sendMail(String toAddress, String subject, String msgBody) {
				
		sendMail(toAddress, subject, msgBody, null,null,null);
	}
	
	public void sendMail(String toAddress, String subject, String msgBody, InputStream attachment, String fileName, String contentType) {

		
		List<InputStream> files =new ArrayList<InputStream>();
		if (attachment != null) { 	files.add(attachment); }
		
		List<String> contentTypes = new ArrayList<String>();
		if (contentType != null) { 	contentTypes.add(contentType); }
		
		List<String> fileNames = new ArrayList<String>();
		if (fileNames != null) { fileNames.add(fileName); }
		
		sendMail2(toAddress,subject,msgBody, files, fileNames, contentTypes);
	}
	
	//make this better later
	public void sendMail2(String toAddress, String subject, String msgBody, List<InputStream> attachments, List<String> fileNames, List<String> fileTypes) {
		
		Session session = Session.getDefaultInstance(props);
		
		  try {
		    	Message msg = new MimeMessage(session);
				msg.setFrom(new InternetAddress(fromAddress));
				msg.addRecipients(Message.RecipientType.TO, stringToAddress(toAddress));
			    msg.setSubject(subject);
			    
			    if (attachments != null && attachments.size() > 0) {
			    	
			    	Multipart multipart = new MimeMultipart();

			    	//set the text first.
					BodyPart messageBodyPart = new MimeBodyPart();
			        messageBodyPart.setText(msgBody);
			        multipart.addBodyPart(messageBodyPart);			    	
			    	
			    	for (int i =0; i < attachments.size(); i++) {
			    		
			    		InputStream fileIS = attachments.get(i);
			    		String fileName= fileNames.get(i);
			    		String fileType = fileTypes.get(i);
			    		
			    		addAttachment(multipart, fileIS, fileName, fileType);
			    	}
			    	
			    	msg.setContent(multipart);
			    } else {
				    msg.setText(msgBody);
			    }
			    
			    Transport.send(msg);
			} catch (IOException  | MessagingException e) {
				logger.log(Level.SEVERE, "Fail to send email with body: " + msgBody);
				logger.severe(ExceptionUtils.getStackTrace(e));
			} finally {
				session = null;
			}	
		
	}
	


	private void addAttachment(Multipart multipart, InputStream is, String fileName, String fileType) throws IOException, MessagingException {

		MimeBodyPart attachmentPart = new MimeBodyPart();
		attachmentPart.setFileName(fileName);
		attachmentPart.setDisposition(Part.ATTACHMENT);
		DataSource src = new ByteArrayDataSource(is, fileType);
		DataHandler handler = new DataHandler(src);
		attachmentPart.setDataHandler(handler);		
		
		multipart.addBodyPart(attachmentPart);
	}

	private Address[] stringToAddress (String s) {
		
		String[] list = StringUtils.split(s, ",");
		Address[] addrs = new Address[list.length];
		
		for (int i =0; i < list.length; i++) {
			
			try {
				addrs[i] = new InternetAddress( list[i]);
			} catch (AddressException e) {
				logger.severe(ExceptionUtils.getStackTrace(e));
			}
		}
		return addrs;
	}
	
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
	
	
	public void setDefaultToAddress(String defaultToAddress) {
		this.defaultToAddress = defaultToAddress;
	}
}
