package com.esofa.crm.refdata.model.util;

import java.util.Comparator;

import com.esofa.crm.refdata.model.Manufacturer;

public class ManufacturerComparator implements Comparator<Manufacturer> {


	public int compare(Manufacturer o1, Manufacturer o2) {
		return o1.getName().compareTo(o2.getName());
	}

}
