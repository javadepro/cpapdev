package com.esofa.crm.model.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.Converter;

import com.googlecode.objectify.Key;

public class MyStringConverter implements Converter{

	Converter delegate;
	public MyStringConverter(Converter delegate){
		this.delegate = delegate;
	}
	

	public Object convert(Class clazz, Object value) {
		System.out.println(value);
		if(value==null){
			return "";
		}else if(value instanceof Key){
			return Long.toString(((Key)value).getId());
		}else if(value instanceof Date){
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			return sdf.format((Date)value);
		}else{
			return delegate.convert(clazz, value);
		}
	}
}