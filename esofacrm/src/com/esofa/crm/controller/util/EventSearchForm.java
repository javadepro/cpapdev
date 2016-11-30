package com.esofa.crm.controller.util;

import com.esofa.crm.refdata.model.EventSubType;
import com.googlecode.objectify.Key;

public class EventSearchForm {
	private int lastXDays;
	private Key<EventSubType> eventSubType;
	public int getLastXDays() {
		return lastXDays;
	}
	public void setLastXDays(int lastXDays) {
		this.lastXDays = lastXDays;
	}
	public Key<EventSubType> getEventSubType() {
		return eventSubType;
	}
	public void setEventSubType(Key<EventSubType> eventSubType) {
		this.eventSubType = eventSubType;
	}
	
}
