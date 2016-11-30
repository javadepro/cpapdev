package com.esofa.crm.model.util.sleepmed;

import java.io.IOException;
import java.util.Date;

import com.esofa.crm.model.CustomerExtendedInfo;
import com.esofa.crm.model.util.PdfMapper_BasicSignature;
import com.esofa.crm.util.EsofaUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;

public class PdfMapper_InDepthSleepAssess extends PdfMapper_BasicSignature {

	@Override
	public void mapFields(AcroFields fields, Long itemId) throws IOException,
			DocumentException {
		super.mapFields(fields, itemId);
		
		CustomerExtendedInfo cei = customerService.getCustomerExtendedById(itemId);
		
		Date dob = cei.getDateOfBirth();
		
		if (dob != null) {
			fields.setField("dob",EsofaUtils.getDateFormatted(dob));
		}
		
		fields.setField("gender",cei.getGender());

		
	}
	
}
