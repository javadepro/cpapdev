package com.esofa.crm.common.model;

import java.util.HashMap;

public interface HashMapTransformable<T> {

	public T importFromHashMap(HashMap<String, String> hashMap);
	public HashMap<String, String> exportToHashMap();
	
}
