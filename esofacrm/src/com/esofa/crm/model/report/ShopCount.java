package com.esofa.crm.model.report;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

//import com.googlecode.objectify.annotation.Embed;

//@Embed
public class ShopCount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1586233971387340596L;
	private String name;
	private int count;
	
	public ShopCount() {
	}
	
	public ShopCount(String name, int count) {
		super();
		this.name = name;
		this.count = count;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	@Override
	public boolean equals(Object obj) {

		if (!(obj instanceof ShopCount)) {
			return false;
		}
		
		ShopCount otherObj = (ShopCount) obj;
		
		return StringUtils.equalsIgnoreCase(this.getName(), otherObj.getName());		
	}
	
	
	@Override
	public int hashCode() {		
		
		if (this.getName() == null) {
			return 0;
		}
		return this.getName().hashCode();
	}
	
}
