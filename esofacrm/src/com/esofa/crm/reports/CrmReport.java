package com.esofa.crm.reports;

import java.io.IOException;
import java.io.Writer;

public interface CrmReport<T> {

	public String getDisplayName();
	
	public boolean supportsCompanyMode(String mode);
	
	public void write(T req, Writer writer) throws IOException;
}
