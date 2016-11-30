package com.esofa.crm.service;

import static com.esofa.crm.service.OfyService.ofy;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.esofa.crm.model.Inventory;
import com.esofa.crm.model.Product;
import com.esofa.crm.model.ProductSearch;
import com.esofa.crm.refdata.model.ProductSubType;
import com.esofa.crm.refdata.model.Shop;
import com.esofa.crm.refdata.service.RefdataService;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.Key;
//import com.googlecode.objectify.annotation.Embed;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.cmd.Query;

@Service
public class ProductService {
	private static final Logger log = Logger.getLogger(ProductService.class
			.getName());

	private RefdataService refdataService;
	
	
	private ConfigService configService;
	
	private static final int BARCODE_PADD = 8;
	
	@Cacheable(value = "productAll")
	public Map<Key<Product>, Product> getProductMap() {
		List<Key<Product>> keys = ofy().load().type(Product.class)
				.filter("isActive", true).order("name").keys().list();
		return ofy().load().keys(keys);
	}

	public Map<Key<Product>, Product> getProductMap(boolean showActiveOnly) {
		Query<Product> q  = ofy().load().type(Product.class);
		
		if (showActiveOnly) {
			q = q.filter("isActive", true);
		}
		q =q.order("name");
		
		List<Key<Product>> keys =  q.keys().list();
		return ofy().load().keys(keys);
	}
	
	
	@CacheEvict(value = "productAll", allEntries = true)
	public Product saveProduct(Product product) {
		ofy().save().entity(product).now();
		log.info("Product id:" + product.getId() + " Saved");

		return product;
	}

	//check if the barcode exists for any of the products.
	public boolean doesBarCodeExist(String barCode) {		
		if (StringUtils.isEmpty(barCode)) {
			return false;			
		}
		
		int count =countBarCodeInstances(barCode);
		return count >0;
	}
	
	//should only have self with this barcode. no one else.
	public boolean isDuplicateBarCode(String barCode) {	
		if (StringUtils.isEmpty(barCode)) {
			return false;			
		}
		
		int expectedCount = 1;  //expect only self to have barcode.  no one else.
		return countBarCodeInstances(barCode) > expectedCount;
	}
	
	private int countBarCodeInstances(String barCode) {
		int count = ofy().load().type(Product.class)
				.filter("productBarCode", barCode).count();
		return count;
	}

	public Product getProductByKey(Key<Product> productKey) {
		return ofy().load().key(productKey).now();
	}

	public Product getProductById(Long id) {
		Key<Product> key = Key.create(Product.class, id);
		return getProductByKey(key);
	}

	public Product getProductByBarcode(String barCode) {
		Product p = ofy().load().type(Product.class)
					.filter("productBarCode", barCode.trim().toUpperCase()).first().now();
		return p;
	}
	
	public String getProductMapWithInventory(String startCursor,
			Map<Product, Map<Key<Shop>, Inventory>> resultsHolder,
			int pageSize) {


		Map<Key<Shop>, Shop> shopAllMap = refdataService.getShopMap();
		Map<Key<Shop>, Shop> shopActiveMap = refdataService.getActiveShopMap();



		Query<Product> q = ofy().load().type(Product.class).filter("isActive", true)
				.order("name").limit(pageSize);

		if (StringUtils.isNotEmpty(startCursor)) {
			q.startAt(Cursor.fromWebSafeString(startCursor));
		}

		QueryResultIterator<Product> qri = q.iterator();

		for (int i = 0; i < pageSize; i++) {
			if (qri.hasNext()) {
				
				Map<Key<Shop>, Inventory> currentInventories = new HashMap<Key<Shop>, Inventory>();
				Product currentProduct = qri.next();
				List<Inventory> inventories = ofy().load().type(Inventory.class)
						.ancestor(Key.create(Product.class, currentProduct
										.getId())).list();

				for (Inventory inventory : inventories) {
					// Inventory inventory = inventoryIt.next();
					
					Shop s = shopAllMap.get(inventory.getShop());
					
					if (s != null && (s.getDisplayDropDown() || inventory.getQty() !=0)) {
						currentInventories.put(inventory.getShop(), inventory);
					}
				}

				/** Add whatever we dun have into the list **/
				Set<Key<Shop>> missingShop = new LinkedHashSet<Key<Shop>>(
						shopActiveMap.keySet());
				missingShop.removeAll(currentInventories.keySet());

				Iterator<Key<Shop>> it2 = missingShop.iterator();
				while (it2.hasNext()) {
					Key<Shop> k = it2.next();
					currentInventories.put(k, new Inventory());
				}
				resultsHolder.put(currentProduct, currentInventories);
			} else {
				break;
			}
		}
		Cursor c = qri.getCursor();
		if (c == null) {
			return StringUtils.EMPTY;
		}
		return c.toWebSafeString();
	}
	

	public Map<Shop, Inventory> getInventoryMapByProductId(Long id) {
		Key<Product> productKey = Key.create(Product.class, id);
		return getInventoryMapByProductKey(productKey);
	}

	public Inventory getShopInventoryByProductKey(Key<Product> productKey,
			Key<Shop> shopKey) {
		Inventory inventory = ofy().load().type(Inventory.class).ancestor(productKey)
				.filter("shop", shopKey).first().now();

		return inventory;
	}

	public Map<Shop, Inventory> getInventoryMapByProductKey(
			Key<Product> productKey) {
		List<Inventory> inventoriesInDb = ofy().load().type(Inventory.class)
				.ancestor(productKey).list();

		// Has to be treeMap to make sure the order is right
		Map<Shop, Inventory> inventoryMap = new TreeMap<Shop, Inventory>();

		Map<Key<Shop>, Shop> shopAllMap = refdataService.getShopMap();
		Map<Key<Shop>, Shop> shopActiveMap = refdataService.getActiveShopMap();

		/** Put whatever inventory we got into the **/
		for (Inventory inventory : inventoriesInDb) {
			
			Key<Shop> shopKey = inventory.getShop();
			Shop s = shopAllMap.get(shopKey);
		
			//display if active store. OR has qty.
			if (s != null && (s.getDisplayDropDown() || inventory.getQty() !=0)) {
				
				inventoryMap.put(s, inventory);
			} else {
				log.warning("inventory  had shop for key: " + shopKey + "but no longer present in shop map");
			}
		}

		/** Add whatever we dun have into the list **/
		Set<Key<Shop>> missingShop = new LinkedHashSet<Key<Shop>>(
				shopActiveMap.keySet());
		for (Inventory i : inventoryMap.values()) {
			missingShop.remove(i.getShop());
		}

		for (Key<Shop> k : missingShop) {
			Inventory tempInventory = new Inventory();
			tempInventory.setProduct(productKey);
			tempInventory.setShop(k);
			inventoryMap.put(shopAllMap.get(k), tempInventory);
		}

		return inventoryMap;

	}

	public Inventory getInventory(Key<Product>  productKey, Key<Shop> shopKey) {		
		Map<Shop,Inventory> inventoryMap = getInventoryMapByProductKey(productKey);
		for (Shop s : inventoryMap.keySet()) {
			
			Key<Shop> inventoryShopKey = Key.create(Shop.class,s.getId());
			
			if (inventoryShopKey.equals(shopKey)) {
				return inventoryMap.get(s);
			}
		}
		
		return null;
	}
	
	public Map<Product, Map<Key<Shop>, Inventory>> search(
			ProductSearch productSearch) {
		Map<Product, Map<Key<Shop>, Inventory>> productMap = new LinkedHashMap<Product, Map<Key<Shop>, Inventory>>();
		Map<Key<Shop>, Shop> shopMap = refdataService.getShopMap();
		
		Map<Key<Product>, Product> searchResult = this
				.searchHelper(productSearch);
		Collection<Product> productList = searchResult.values();

		for (Product currentProduct : productList) {
			Map<Key<Shop>, Inventory> currentInventories = new HashMap<Key<Shop>, Inventory>();
			List<Inventory> inventories = ofy()
					.load()
					.type(Inventory.class)
					.ancestor(Key.create(Product.class, currentProduct.getId()))
					.list();

			for (Inventory inventory : inventories) {
				// Inventory inventory = inventoryIt.next();
				currentInventories.put(inventory.getShop(), inventory);
			}

			/** Add whatever we dun have into the list **/
			Set<Key<Shop>> missingShop = new LinkedHashSet<Key<Shop>>(
					shopMap.keySet());
			missingShop.removeAll(currentInventories.keySet());

			Iterator<Key<Shop>> it2 = missingShop.iterator();
			while (it2.hasNext()) {
				Key<Shop> k = it2.next();
				currentInventories.put(k, new Inventory());
			}
			productMap.put(currentProduct, currentInventories);
		}

		return productMap;
	}

	/** Get product by type **/
	public Map<Key<Product>, Product> getProductBySubtype(
			Key<ProductSubType> productSubType) {
		List<Key<Product>> keys = ofy().load().type(Product.class).filter("productSubType", productSubType).keys().list();
		return ofy().load().keys(keys);
	}

	/** Get product by type **/
	public Map<Key<Product>, Product> getProductBySubtypeList(
			List<Key<ProductSubType>> productSubTypeList) {
		List<Key<Product>> keys = ofy().load().type(Product.class).filter("isActive",true).order("name").keys().list();
		 
		Map<Key<Product>, Product> products  = ofy().load().keys(keys);
		Map<Key<Product>, Product> filteredProducts  = new LinkedHashMap<Key<Product>, Product>();

		for (Map.Entry<Key<Product>, Product> e : products.entrySet()) {
			
			if (productSubTypeList.contains(e.getValue().getProductSubType())) {
				
				filteredProducts.put(e.getKey(), e.getValue());
			}
		}
		
		return filteredProducts;
	}

	/** Search **/
	public Map<Key<Product>, Product> searchHelper(ProductSearch productSearch) {
		int wildCard = productSearch.hasWildCard();
		int fieldSet = productSearch.fieldSet();
		if (wildCard == 0) {
			return queryNoWildCard(productSearch);
		} else if (wildCard == 1 && fieldSet == 1) {
			return queryOneWildCard(productSearch);
		} else if (wildCard == fieldSet) {
			Map<Key<Product>, Product> result = queryOneWildCard(productSearch);
			return filterResult(result, productSearch);
		} else {
			Map<Key<Product>, Product> result = querySkipWildCard(productSearch);
			return filterResult(result, productSearch);
		}
	}

	/**
	 * GAE will only allow one in eq
	 */
	private Map<Key<Product>, Product> queryNoWildCard(ProductSearch exampleObj) {
		Query<Product> q = ofy().load().type(Product.class);

		// Add all non-null properties to query filter
		for (Field field : ProductSearch.class.getDeclaredFields()) {
			// Ignore transient, embedded, array, and collection properties
			if (field.isAnnotationPresent(Ignore.class)
					//|| (field.getType().isAnnotationPresent(Embed.class))
					|| (field.getType().isArray())
					|| (Collection.class.isAssignableFrom(field.getType()))
					|| ((field.getModifiers() & BAD_MODIFIERS) != 0))
				continue;

			field.setAccessible(true);

			Object value;
			try {
				value = field.get(exampleObj);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			if (value != null) {
				if (value instanceof String) {
					if (value.toString().trim().equals("")) {
						continue;
					}
				}
				
				//if the field name is ProductSearch.isActive and value is false
				//this means we want to show the discontinued products as well.
				//so we do not apply the ProductSearch filter for this. must display 
				//entries where ProductSearch.isActive is both true and false.
				if (exampleObj.getIsActive() == false 
						&& StringUtils.equalsIgnoreCase(field.getName(), "isActive")) {
					continue;
				}
				
				
				q = q.filter(field.getName(), value);
			}
		}
		List<Key<Product>> keys = q.keys().list();
		return ofy().load().keys(keys);
	}

	private Map<Key<Product>, Product> queryOneWildCard(ProductSearch exampleObj) {
		Query<Product> q = ofy().load().type(Product.class);

		boolean foundIneq = false;

		// Add all non-null properties to query filter
		for (Field field : ProductSearch.class.getDeclaredFields()) {
			// Ignore transient, embedded, array, and collection properties
			if (field.isAnnotationPresent(Ignore.class)
					//|| (field.getType().isAnnotationPresent(Embed.class))
					|| (field.getType().isArray())
					|| (Collection.class.isAssignableFrom(field.getType()))
					|| ((field.getModifiers() & BAD_MODIFIERS) != 0))
				continue;

			field.setAccessible(true);

			Object value;
			try {
				value = field.get(exampleObj);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			if (value != null) {
				if (value instanceof String) {
					if (foundIneq) {
						continue;
					}
					if (!value.toString().trim().equals("")
							&& value.toString().trim().contains("*")) {
						String searchString = value.toString().trim()
								.replace("*", "");
						q.filter(field.getName() + " >=", searchString)
								.filter(field.getName() + " <",
										searchString + "\uFFFD");
						foundIneq = true;
					}
					continue;
				}
				
				//if the field name is ProductSearch.isActive and value is false
				//this means we want to show the discontinued products as well.
				//so we do not apply the ProductSearch filter for this. must display 
				//entries where ProductSearch.isActive is both true and false.
				if (exampleObj.getIsActive() == false 
						&& StringUtils.equalsIgnoreCase(field.getName(), "isActive")) {
					continue;
				}
				
				q = q.filter(field.getName(), value);
			}
		}
		List<Key<Product>> keys = q.keys().list();
		return ofy().load().keys(keys);
	}

	/**
	 * GAE will only allow one in eq
	 */
	private Map<Key<Product>, Product> querySkipWildCard(
			ProductSearch exampleObj) {
		Query<Product> q = ofy().load().type(Product.class);

		// Add all non-null properties to query filter
		for (Field field : ProductSearch.class.getDeclaredFields()) {
			// Ignore transient, embedded, array, and collection properties
			if (field.isAnnotationPresent(Ignore.class)
					//|| (field.getType().isAnnotationPresent(Embed.class))
					|| (field.getType().isArray())
					|| (Collection.class.isAssignableFrom(field.getType()))
					|| ((field.getModifiers() & BAD_MODIFIERS) != 0))
				continue;

			field.setAccessible(true);

			Object value;
			try {
				value = field.get(exampleObj);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			if (value != null) {
				if (value instanceof String) {
					if (value.toString().trim().equals("")
							|| value.toString().trim().endsWith("*")) {
						continue;
					}
				}
				
				//if the field name is ProductSearch.isActive and value is false
				//this means we want to show the discontinued products as well.
				//so we do not apply the ProductSearch filter for this. must display 
				//entries where ProductSearch.isActive is both true and false.
				if (exampleObj.getIsActive() == false 
						&& StringUtils.equalsIgnoreCase(field.getName(), "isActive")) {
					continue;
				}
				
				q = q.filter(field.getName(), value);
			}
		}
		List<Key<Product>> keys = q.keys().list();
		return ofy().load().keys(keys);
	}

	private Map<Key<Product>, Product> filterResult(
			Map<Key<Product>, Product> result, ProductSearch productSearch) {

		String nameSearchString = productSearch.getName().replace("*", "");
		String barCodeSearchString = productSearch.getProductBarCode().replace("*", "");
		for (Iterator<Map.Entry<Key<Product>, Product>> i = result.entrySet()
				.iterator(); i.hasNext();) {
			Entry<Key<Product>, Product> entry = i.next();
			
			//filter for name
			if (!entry.getValue().getName().startsWith(nameSearchString)) {
				i.remove();
				continue;
			}
			
			//filter for barCode
			if (!entry.getValue().getProductBarCode().startsWith(barCodeSearchString)) {
				i.remove();
				continue;
			}			
		}

		return result;
	}

	/**
	 * generates a barcode
	 * @return
	 */
	public String generateBarCode() {
		
		String barCodePrefix = configService.getBarCodePrefix();
		String barCodeSeq = String.valueOf(configService.incrementBarCodeSequence());
		String generatedBarCode = barCodePrefix + StringUtils.leftPad(barCodeSeq, BARCODE_PADD, "0");
		
		//recursive try again if already in db.
		if (doesBarCodeExist(generatedBarCode)) {
			
			generatedBarCode = generateBarCode();
		}
		
		return generatedBarCode;
	}
		
	static final int BAD_MODIFIERS = Modifier.FINAL | Modifier.STATIC
			| Modifier.TRANSIENT;

	public void setRefdataService(RefdataService refdataService) {
		this.refdataService = refdataService;
	}


	public void setConfigService(ConfigService configService) {
		this.configService = configService;
	}
}
