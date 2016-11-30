package com.esofa.crm.admin.controller.util;

import org.gmr.web.multipart.GMultipartFile;

public class UploaderForm {
	
	private String className;
	private String classNameParent;
	
	private GMultipartFile fileData;
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	public CommonsMultipartFile getFileData() {
		return fileData;
	}
	public void setFileData(CommonsMultipartFile fileData) {
		this.fileData = fileData;
	}
	**/
	public String getClassNameParent() {
		return classNameParent;
	}
	public GMultipartFile getFileData() {
		return fileData;
	}
	public void setFileData(GMultipartFile fileData) {
		this.fileData = fileData;
	}
	public void setClassNameParent(String classNameParent) {
		this.classNameParent = classNameParent;
	}
	
	
}
