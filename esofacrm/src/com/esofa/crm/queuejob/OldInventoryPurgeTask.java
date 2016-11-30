package com.esofa.crm.queuejob;

import static com.esofa.crm.service.OfyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.esofa.crm.model.Inventory;
import com.esofa.crm.refdata.model.Shop;
import com.esofa.crm.refdata.service.RefdataService;
import com.googlecode.objectify.Key;

public class OldInventoryPurgeTask extends BasePurgeTask {
	
	protected RefdataService refdataService;

	protected List<? extends Key<?>> getKeysToDelete() {

		Map<Key<Shop>, Shop> shopAllMap = refdataService.getShopMap();
		List<Key<Inventory>> keys=new ArrayList<>();
		
		for (Key<Shop> sKey : shopAllMap.keySet()) {
			
			Shop s = shopAllMap.get(sKey);
			
			if (s.getDisplayDropDown()) { continue;	}
			
			keys.addAll(ofy().load().type(Inventory.class).filter("shop", sKey).filter("qty", 0).keys().list());
					
			
		}
					
		return keys;
	}
	
	public void setRefdataService(RefdataService refdataService) {
		this.refdataService = refdataService;
	}

}
