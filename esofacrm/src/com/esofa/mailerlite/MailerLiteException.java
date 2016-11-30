package com.esofa.mailerlite;


public class MailerLiteException extends RuntimeException {

	private static final long serialVersionUID = 4146451219273148248L;


	public MailerLiteException(String s) {
		super(s);
	}


	public MailerLiteException(String s, Exception e) {

		super(s,e);
	}
	
}
