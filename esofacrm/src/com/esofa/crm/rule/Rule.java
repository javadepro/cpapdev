package com.esofa.crm.rule;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Rule implements Serializable, Comparable<Rule> {
	private static final long serialVersionUID = -169817547148618115L;

	@Id
	Long id;
	
	String inputClass; 
	String condition;
	String action;
	String name;
	String resources; 
	String attribute;
	String note;
	Integer priority = 0;
	Integer dateOffset=0;		//date offset into the future to adjust the posted date to.
	
	public String getResources() {
		return resources;
	}
	public void setResources(String resources) {
		this.resources = resources;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getInputClass() {
		return inputClass;
	}
	public void setInputClass(String inputClass) {
		this.inputClass = inputClass;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public Integer getDateOffset() {
		return dateOffset;
	}
	
	public void setDateOffset(Integer dateOffset) {
		this.dateOffset = dateOffset;
	}
	
	public int compareTo(Rule rule) {
		return rule.getPriority().compareTo(rule.getPriority());
	}
}