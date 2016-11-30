package com.esofa.crm.model;

import java.io.Serializable;
import java.util.Date;

import com.esofa.crm.refdata.model.EventSubType;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;


@Entity
public class CustomerEvent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;

	@Id
	@Index
	Long id;
	
	@Index
	private Key<EventSubType> eventSubType;
	
	@Index
	private Date date = new Date();
	
	private String creater;
	
	private String details;
	
	@Parent
	private Key<Customer> customer;

	public Key<EventSubType> getEventSubType() {
		return eventSubType;
	}

	public void setEventSubType(Key<EventSubType> eventSubType) {
		this.eventSubType = eventSubType;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Key<Customer> getCustomer() {
		return customer;
	}

	public void setCustomer(Key<Customer> customer) {
		this.customer = customer;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "CustomerEvent [id=" + id + ", eventSubType=" + eventSubType
				+ ", date=" + date + ", creater=" + creater + ", details="
				+ details + ", customer=" + customer + "]";
	}
}
