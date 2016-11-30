package com.esofa.crm.drive;

import com.esofa.crm.service.ConfigService;
import com.google.api.services.drive.model.File;

public class CustomerFolder extends AbstractDriveFolder {
	private Long customerId = null;
	// TODO: potential to cache the customer folders at application level to minimize overhead of
	// fetching/creating folder
	private File customerFolder = null;
	
	public CustomerFolder(Long customerId, ConfigService config) {
		super(config);
		this.customerId = customerId;
		this.customerFolder = DriveUtil.getFolder(drive, this.customerId.toString());		
	}

	@Override
	public File getFolder() {
		return this.customerFolder;
	}	
}
