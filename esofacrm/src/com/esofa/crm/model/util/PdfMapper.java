package com.esofa.crm.model.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.io.ByteStreams;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.AcroFields.Item;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public abstract class PdfMapper<T> {

	private static final Logger log = Logger.getLogger(PdfMapper.class.getName());
	
	protected String formPath;
	
	/**
	 * @param fields Instance of the pdf form fields being mapped
	 * @param id Key to the entity to be mapped to the pdf fields
	 * @throws IOException
	 * @throws DocumentException
	 */
	protected abstract void mapFields(AcroFields fields, T itemId) throws IOException, DocumentException;
	
	protected byte[] getFormPath() {		
		
		try {
			return ByteStreams.toByteArray(new FileInputStream(formPath));
		} catch ( Exception e) {

			throw new RuntimeException(e);
		}
		
	}

			
	public void createPdf(OutputStream os, T itemId) {		
		try {			
			PdfReader.unethicalreading = true;			
			PdfReader reader = new PdfReader(getFormPath());

			// TODO dev only. TBD
			// whatFields(reader);
			
			PdfStamper stamper = new PdfStamper(reader, os,'\0',true);
			AcroFields fields = stamper.getAcroFields();
			mapFields(fields, itemId);
			stamper.close();
		} catch (DocumentException | IOException e) {
			
			log.log(Level.SEVERE,"error generating adp form",e);
			throw new RuntimeException(e);
			
		}
	}
	
	public void whatFields(PdfReader pdf) {
		AcroFields fields = pdf.getAcroFields();
		Map<String, Item> fieldMap = fields.getFields();
		for( String field : fieldMap.keySet() ) {
			System.out.print(field + "," + getAcroFieldType(fields.getFieldType(field)));
			
			if( getAcroFieldType(fields.getFieldType(field)).equalsIgnoreCase("Checkbox") ) {
				String[] states = fields.getAppearanceStates(field);
				for( String state : states ) {
					System.out.print("," + state);
				}
			}
			
			System.out.println();
		}
	}
	
	private String getAcroFieldType(int typeId) {
		String type = "undefined";
		switch(typeId) {
			case 0: type = "NONE"; break;
			case 1: type = "PushButton"; break;
			case 2: type = "Checkbox"; break;
			case 3: type = "RadioButton"; break;
			case 4: type = "Text"; break;
			case 5: type = "List"; break;
			case 6: type = "Combo"; break;
			case 7: type = "Signature"; break;
		}
		return type;
	}
	
	public void setFormPath(String formPath) {
		this.formPath = formPath;
	}
	
}
