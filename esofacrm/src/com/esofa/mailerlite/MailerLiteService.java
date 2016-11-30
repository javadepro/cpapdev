package com.esofa.mailerlite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class MailerLiteService {

	private String apiKey;
	private String  listId;
	
	private String subscribeUrl;
	private String unsubscribeUrl;	
	
	public boolean addUpdateSubscriber(BasicSubscriber sub) {
		
		String url = subscribeUrl + listId + "/";

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("apiKey", apiKey));
		nvps.add(new BasicNameValuePair("id", listId));
		nvps.add(new BasicNameValuePair("email", sub.getEmail()));
		nvps.add(new BasicNameValuePair("name", sub.getFirstName()));
		nvps.add(new BasicNameValuePair("country", sub.getCountry()));
		nvps.add(new BasicNameValuePair("resubscribe","1"));

		HttpResponse response;
		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));

			HttpClient httpClient = new DefaultHttpClient();
			response = httpClient.execute(httpPost);
		} catch (IOException e) {

			throw new MailerLiteException("unable to add subscriber: "
					+ sub.getEmail(), e);
		}

		if (response.getStatusLine().getStatusCode() != 200) {

			throw new MailerLiteException("unable to add subscriber: "
					+ sub.getEmail() + ". httpStatus:"
					+ response.getStatusLine().toString());
		}

		return true;
	}
	
	public boolean removeSubscriber(String email) {
		
		String url = unsubscribeUrl + listId + "/";

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("apiKey", apiKey));
		nvps.add(new BasicNameValuePair("email", email));

		HttpResponse response;
		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));

			HttpClient httpClient = new DefaultHttpClient();
			response = httpClient.execute(httpPost);
		} catch (IOException e) {

			throw new MailerLiteException("unable to remove subscriber: "
					+ email, e);
		}

		if (response.getStatusLine().getStatusCode() != 200) {

			throw new MailerLiteException("unable to remove subscriber: "
					+ email + ". httpStatus:"
					+ response.getStatusLine().toString());
		}

		return true;
	}
	
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public String getListId() {
		return listId;
	}
	public void setListId(String listId) {
		this.listId = listId;
	}
	
	public void setSubscribeUrl(String subscribeUrl) {
		this.subscribeUrl = subscribeUrl;
	}
	
	public void setUnsubscribeUrl(String unsubscribeUrl) {
		this.unsubscribeUrl = unsubscribeUrl;
	}
}
