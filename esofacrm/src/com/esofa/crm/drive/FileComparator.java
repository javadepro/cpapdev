package com.esofa.crm.drive;

import java.util.Comparator;

import com.google.api.services.drive.model.File;

public class FileComparator implements Comparator<File> {

	@Override
	public int compare(File o1, File o2) {
		if( o1 == null && o2 == null ) return 0;
		if( o1 == null ) return -1;
		if( o2 == null ) return 1;
		
		String filename1 = o1.getTitle();
		String filename2 = o2.getTitle();
		
		if( filename1 == null && filename2 == null ) return 0;
		if( filename1 == null ) return -1;
		if( filename2 == null ) return 1;
		
		return filename1.compareTo(filename2);
	}
}
