package com.esofa.spring.controller;

import java.beans.PropertyEditorSupport;
import java.util.logging.Logger;

import com.googlecode.objectify.Key;

public class KeyEditor extends PropertyEditorSupport {
	
	private static final Logger log = Logger
			.getLogger(KeyEditor.class.getName());
	
	public KeyEditor(){
		super();
		
	}
	
	@Override
	public String getAsText() {
		if(getValue()==null){
			return "";
		}
		String keyString = ((Key) getValue()).getString();
		return keyString;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if(text==null||text.trim().equals("")){
			setValue(null);
		}else{
			Key<?> key = Key.create(text);
			setValue(key);
		}
	}
}