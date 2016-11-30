package com.esofa.crm.refdata.service;

import static com.esofa.crm.service.OfyService.begin;
import static com.esofa.crm.service.OfyService.ofy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Service;

import com.esofa.crm.common.model.Address;
import com.esofa.crm.model.Config;
import com.esofa.crm.refdata.model.AlertCategoryE;
import com.esofa.crm.refdata.model.AlertSubType;
import com.esofa.crm.refdata.model.AlertType;
import com.esofa.crm.refdata.model.AppointmentPreference;
import com.esofa.crm.refdata.model.ContactPreference;
import com.esofa.crm.refdata.model.CpapDiagnosis;
import com.esofa.crm.refdata.model.DentalClinic;
import com.esofa.crm.refdata.model.Dentist;
import com.esofa.crm.refdata.model.DiscountReason;
import com.esofa.crm.refdata.model.EventSubType;
import com.esofa.crm.refdata.model.EventType;
import com.esofa.crm.refdata.model.FamilyDoctor;
import com.esofa.crm.refdata.model.FundingOption;
import com.esofa.crm.refdata.model.InsuranceProvider;
import com.esofa.crm.refdata.model.Manufacturer;
import com.esofa.crm.refdata.model.PrimaryAdpInfo;
import com.esofa.crm.refdata.model.ProductSubType;
import com.esofa.crm.refdata.model.ProductType;
import com.esofa.crm.refdata.model.Setting;
import com.esofa.crm.refdata.model.Shop;
import com.esofa.crm.refdata.model.SleepClinic;
import com.esofa.crm.refdata.model.SleepDoctor;
import com.esofa.crm.security.user.model.CrmUser;
import com.esofa.crm.security.user.model.GrantedAuthorityImpl;
import com.esofa.crm.service.ConfigService;
import com.esofa.crm.service.InvoiceSeqNumService;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.cmd.Query;
import com.googlecode.objectify.util.Closeable;

@Service
public class RefdataService {
	private static final Logger log = Logger.getLogger(RefdataService.class
			.getName());

	private EhCacheCacheManager cacheManager;
	
	private ConfigService configService;

	private InvoiceSeqNumService invoiceSeqNumService;
	
	public enum SettingTypes {
		PaymentMethod, 
		MachineWarranty,
		PaymentDescription,
		AdpPaymentDescription,
		AdminPaymentDescription,
		AdminAdpPaymentDescription,
		BarcodePermissions,
		MaskWarranty,
		AdpFundingMax
	}
	
	/** Shop **/
	@Cacheable(value = "refdataShopAll" )
	public Map<Key<Shop>, Shop> getShopMap() {		
		Map<Key<Shop>, Shop> result = null; 
				
		try {
			List<Key<Shop>> keys = ofy().load().type(Shop.class).order("order").keys().list();
			result = ofy().load().keys(keys);
			
		} catch (IllegalStateException ise ) {
			Closeable closeable = begin();
			 
			List<Key<Shop>> keys = ofy().load().type(Shop.class).order("order").keys().list();
				result =  ofy().load().keys(keys);
			 closeable.close();
		}
		return result;
	}
	
	@Cacheable(value = "refdataActiveShopAll" )
	public Map<Key<Shop>, Shop> getActiveShopMap() {		
		Map<Key<Shop>, Shop> result = null; 
		
		try {
			List<Key<Shop>> keys = ofy().load().type(Shop.class).filter("displayDropDown", true).order("order").keys().list();
			result = ofy().load().keys(keys);
			
		} catch (IllegalStateException ise ) {
			Closeable closeable = begin();
			 
			List<Key<Shop>> keys = ofy().load().type(Shop.class).filter("displayDropDown", true).order("order").keys().list();
				result =  ofy().load().keys(keys);
			 closeable.close();
		}
		return result;
	}
	
	
	
	public Key<Shop> getShopKey(String shortName) {
		
		Map<Key<Shop>,Shop> shopMap = getShopMap();
		
		for (Shop s : shopMap.values()) {
			
			if (StringUtils.equalsIgnoreCase(StringUtils.trim(shortName), 
					StringUtils.trim(s.getShortName()))) {
				return Key.create(Shop.class,s.getId());
			}
		}
		
		return null;
	}

	public Shop getShopById(Long id) {
		log.info("Load shop from datastore:" + id);
		return ofy().load().type(Shop.class).id(id).now();
	}
	
	public Shop getShop(Key<Shop> shopKey) {		
		Shop s = null;
		try {
			s = ofy().load().key(shopKey).now();
		} catch (NotFoundException e) {
			log.log(Level.SEVERE, "unable to find shopKey" + shopKey, e);
		}

		return s;
	}

	@CacheEvict(value = "refdataShopAll", allEntries = true)
	public void refreshShop() {
		cacheManager.getCache("refdataShopAll").clear();
	}

	@CacheEvict(value = {"refdataShopAll","refdataActiveShopAll"}, allEntries = true)
	public synchronized void saveShop(Shop shop) {
		log.info("remove shop from cache:" + shop.getId());
		
		Key<Shop> shopKey =  ofy().save().entity(shop).now();
		
		if (!invoiceSeqNumService.exists(shopKey)) {
			invoiceSeqNumService.createNew(shopKey);
		}
		cacheManager.getCache("refdataShopAll").clear();
	}

	/** Manufacturer **/
	@Cacheable(value = "refdataManufacturererAll")
 	public Map<Key<Manufacturer>, Manufacturer> getManufacturerMap() {
		List<Key<Manufacturer>> keys = ofy().load().type(Manufacturer.class).keys().list();
		return ofy().load().keys(keys);
	}
	

	// @Cacheable(value="refdataManufacturer", key="#id")
	public Manufacturer getManufacturerById(Long id) {
		return ofy().load().type(Manufacturer.class).id(id).now();
	}

	@CacheEvict(value = "refdataManufacturererAll", allEntries = true)
	public void saveManufacturer(Manufacturer manufacturer) {
		ofy().save().entity(manufacturer).now();
	}

	/** ContactPreference **/
	@Cacheable(value = "refdataContactPreferenceAll")
	public Map<Key<ContactPreference>, ContactPreference> getContactPreferenceMap() {
		List<Key<ContactPreference>> keys = ofy().load().type(ContactPreference.class).keys().list();
		return ofy().load().keys(keys);
	}

	public ContactPreference getContactPreferenceById(Long id) {
		log.info("Load contactPreference from datastore:" + id);
		ContactPreference pref=null;
		try {
			pref = ofy().load().type(ContactPreference.class).id(id).now();
		} catch (NotFoundException e) {
			log.log(Level.SEVERE, "", e);
			e.printStackTrace();
		}
		
		return pref;
	}

	@CacheEvict(value = "refdataContactPreferenceAll", allEntries = true)
	public void saveContactPreference(ContactPreference contactPreference) {
		log.info("remove contactPreference from cache:"
				+ contactPreference.getId());
		ofy().save().entity(contactPreference).now();
	}

	/** Discount Reason **/
	@Cacheable(value = "refdataDiscountReasonAll")
	public Map<Key<DiscountReason>, DiscountReason> getDiscountReasonMap() {
		List<Key<DiscountReason>> keys = ofy().load().type(DiscountReason.class).order("displayOrder").keys().list();
		return ofy().load().keys(keys);
	}

	public DiscountReason getDiscountReasonById(Long id) {
		log.info("Load discountReason from datastore:" + id);
		return ofy().load().type(DiscountReason.class).id(id).now();
	}

	@CacheEvict(value = "refdataDiscountReasonAll", allEntries = true)
	public void saveDiscountReason(DiscountReason discountReason) {
		log.info("remove discountReason from cache:"
				+ discountReason.getId());
		ofy().save().entity(discountReason).now();
	}
	
	/** AppointmentPreference **/
	@Cacheable(value = "refdataAppointmentPreferenceAll")
	public Map<Key<AppointmentPreference>, AppointmentPreference> getAppointmentPreferenceMap() {
		List<Key<AppointmentPreference>> keys = ofy().load().type(AppointmentPreference.class).keys().list();
		return ofy().load().keys(keys);
	}

	public AppointmentPreference getAppointmentPreferenceById(Long id) {
		log.info("Load appointmentPreference from datastore:" + id);
		return ofy().load().type(AppointmentPreference.class).id(id).now();
	}

	@CacheEvict(value = "refdataAppointmentPreferenceAll", allEntries = true)
	public void saveAppointmentPreference(AppointmentPreference appointmentPreference) {
		log.info("remove appointmentPreference from cache:"
				+ appointmentPreference.getId());
		ofy().save().entity(appointmentPreference).now();
	}
	
	/** CpapDiagnosis **/
	@Cacheable(value = "refdataCpapDiagnosisAll")
	public Map<Key<CpapDiagnosis>, CpapDiagnosis> getCpapDiagnosisMap() {
		List<Key<CpapDiagnosis>> keys = ofy().load().type(CpapDiagnosis.class).keys().list();
		return ofy().load().keys(keys);
	}

	public CpapDiagnosis getCpapDiagnosisById(Long id) {
		log.info("Load cpapDiagnosis from datastore:" + id);
		return ofy().load().type(CpapDiagnosis.class).id(id).now();
	}

	@CacheEvict(value = "refdataCpapDiagnosisAll", allEntries = true)
	public void saveCpapDiagnosis(CpapDiagnosis cpapDiagnosis) {
		log.info("remove cpapDiagnosis from cache:" + cpapDiagnosis.getId());
		ofy().save().entity(cpapDiagnosis).now();
	}

	/** Family Doctor **/
	@Cacheable(value = "refdataFamilyDoctorAll")
	public Map<Key<FamilyDoctor>, FamilyDoctor> getFamilyDoctorMap() {
		
		List<Key<FamilyDoctor>> keys = ofy().load().type(FamilyDoctor.class).order("lastName").order("firstName").keys().list();
		
		return ofy().load().keys(keys);
	}

	// @Cacheable(value="refdataManufacturer", key="#id")
	public FamilyDoctor getFamilyDoctorById(Long id) {	
		return ofy().load().type(FamilyDoctor.class).id(id).now();
	}

	@CacheEvict(value = "refdataFamilyDoctorAll", allEntries = true)
	public void saveFamilyDoctor(FamilyDoctor familyDoctor) {
		ofy().save().entity(familyDoctor).now();
	}

	/** Clinic **/
	@Cacheable(value = "refdataSleepClinicAll")
	public Map<Key<SleepClinic>, SleepClinic> getSleepClinicMap() {
		List<Key<SleepClinic>> keys = ofy().load().type(SleepClinic.class).order("name").keys().list();
		return ofy().load().keys(keys);
	}

	// @Cacheable(value="refdataManufacturer", key="#id")
	public SleepClinic getSleepClinicById(Long id) {
		return ofy().load().type(SleepClinic.class).id(id).now();
	}

	@CacheEvict(value = "refdataSleepClinicAll", allEntries = true)
	public void saveSleepClinic(SleepClinic sleepClinic) {
		ofy().save().entity(sleepClinic).now();
	}
	

	/** Clinic **/
	@Cacheable(value = "refdataDentalClinicAll")
	public Map<Key<DentalClinic>, DentalClinic> getDentalClinicMap() {
		
		List<Key<DentalClinic>> keys = ofy().load().type(DentalClinic.class).order("name").keys().list();
		return ofy().load().keys(keys);
	}
 
	public DentalClinic getDentalClinicById(Long id) {
		
		return ofy().load().type(DentalClinic.class).id(id).now();
	}

	@CacheEvict(value = "refdataDentalClinicAll", allEntries = true)
	public void saveDentalClinic(DentalClinic dentalClinic) {
		ofy().save().entity(dentalClinic).now();
	}
	
	public Map<Key<CrmUser>, CrmUser> getClinicianMap() {
		return getClinicianMap(false);
	}
	
	public Map<Key<CrmUser>, CrmUser> getClinicianMap(boolean showActiveOnly) {
		//get ROLE_CLINICIAN
		int id = configService.getConfigIntByName("CLINICIAN.ROLE.ID");
		Key<GrantedAuthorityImpl> clinicianKey = Key.create(GrantedAuthorityImpl.class, id);
		Query<CrmUser> q = ofy().load().type(CrmUser.class).filter("authorities", clinicianKey);
		
		if (showActiveOnly) {
			q = q.filter("isActive", true);
		}
		List<Key<CrmUser>> keys = q.keys().list();
		return ofy().load().keys(keys);
	}

	public CrmUser getClinician(Key<CrmUser> clinicianUserKey) {
		Map<Key<CrmUser>,CrmUser> m = getClinicianMap();
		CrmUser c = m.get(clinicianUserKey);
		return c;
	}
	
	/** Insurance Provider **/
	@Cacheable(value = "refdataInsuranceProviderAll")
	public Map<Key<InsuranceProvider>, InsuranceProvider> getInsuranceProviderMap() {
		List<Key<InsuranceProvider>> keys = ofy().load().type(InsuranceProvider.class).keys().list();
		return ofy().load().keys(keys);
	}

	// @Cacheable(value="refdataManufacturer", key="#id")
	public InsuranceProvider getInsuranceProviderById(Long id) {
		return ofy().load().type(InsuranceProvider.class).id(id).now();
	}

	@CacheEvict(value = "refdataInsuranceProviderAll", allEntries = true)
	public void saveInsuranceProvider(InsuranceProvider insuranceProvider) {
		ofy().save().entity(insuranceProvider).now();
	}

	/** Sleep Doctor **/
	@Cacheable(value = "refdataSleepDoctorAll")
	public Map<Key<SleepDoctor>, SleepDoctor> getSleepDoctorMap() {
		List<Key<SleepDoctor>> keys = ofy().load().type(SleepDoctor.class).keys().list();
		return ofy().load().keys(keys);
	}

	// @Cacheable(value="refdataManufacturer", key="#id")
	public SleepDoctor getSleepDoctorById(Long id) {
		return ofy().load().type(SleepDoctor.class).id(id).now();
	}

	@CacheEvict(value = "refdataSleepDoctorAll", allEntries = true)
	public void saveSleepDoctor(SleepDoctor sleepDoctor) {
		ofy().save().entity(sleepDoctor).now();
	}

	/** Funding Option **/
	@Cacheable(value = "refdataFundingOptionAll")
	public Map<Key<FundingOption>, FundingOption> getFundingOptionMap() {
		List<Key<FundingOption>> keys = ofy().load().type(FundingOption.class).order("fundingDetailsType").keys().list();
		return ofy().load().keys(keys);
	}
	
	public List<String> getBenefitCodeList() {
		
		List<String> benefitCodeList = new ArrayList<String>();
		Map<Key<FundingOption>,FundingOption> fundingOptions = getFundingOptionMap();
		
		for (FundingOption fo : fundingOptions.values()) {
			
			if (fo.getBenefitCodeApplicable()) {
				benefitCodeList.add(fo.getOption());
			}
		}
		return benefitCodeList;
	}
	
	public BigDecimal getFundingAdp(String option) {
	
		BigDecimal adpPercentage = BigDecimal.ZERO;
		Map<Key<FundingOption>,FundingOption> fundingOptions = getFundingOptionMap();
		
		if (StringUtils.isNotEmpty(option)) {
					
			for (FundingOption fo : fundingOptions.values()) {
				
				if (StringUtils.equalsIgnoreCase(fo.getOption(), option)) {
					
					adpPercentage = fo.getAdpPercentageAsBD();
					break;
				}
			}
		}
		
		return adpPercentage.setScale(2,BigDecimal.ROUND_HALF_UP);
	}
	

	// @Cacheable(value="refdataManufacturer", key="#id")
	public FundingOption getFundingOptionById(Long id) {
		return ofy().load().type(FundingOption.class).id(id).now();
	}

	@CacheEvict(value = "refdataFundingOptionAll", allEntries = true)
	public void saveFundingOption(FundingOption fundingOption) {
		ofy().save().entity(fundingOption).now();
	}

	
	
	@Cacheable(value = "refdataFundingOptionAll")
	public Map<Key<FundingOption>, FundingOption> getFundingOptionMap(String type) {
		int begin, end = 0;
		if(type.equalsIgnoreCase("government")){
			begin = 9; 
			end = 20;
		}else{
			begin = 0;
			end = 8;
		}
		List<Key<FundingOption>> keys = ofy().load().type(FundingOption.class).filter("fundingDetailsType >= ", begin ).filter("fundingDetailsType <= ", end).keys().list();
		return ofy().load().keys(keys);
	}
	
	/** AlertType **/
	@Cacheable(value = "refdataAlertTypeAll")
	public Map<Key<AlertType>, AlertType> getAlertTypeMap() {
		List<Key<AlertType>> keys = ofy().load().type(AlertType.class).keys().list();
		return ofy().load().keys(keys);
	}

	// @Cacheable(value="refdataManufacturer", key="#id")
	public AlertType getAlertTypeById(Long id) {
		return ofy().load().type(AlertType.class).id(id).now();
	}

	// @Cacheable(value="refdataManufacturer", key="#id")
	public AlertType getAlertTypeByKey(Key<AlertType> key) {
		return ofy().load().key(key).now();
	}

	@CacheEvict(value = "refdataAlertTypeAll", allEntries = true)
	public void saveAlertType(AlertType alertType) {
		cacheManager.getCache("refdataAlertTypeMap").clear();
		ofy().save().entity(alertType).now();
	}

	// @Cacheable(value="refdataManufacturer", key="#id")
	public List<AlertSubType> getAlertSubTypeByParentId(Long parentId) {
		Key<AlertType> parentKey = Key.create(AlertType.class, parentId);
		return ofy().load().type(AlertSubType.class).ancestor(parentKey).list();
	}

	// @Cacheable(value="refdataManufacturer", key="#id")
	public AlertSubType getAlertSubTypeByKey(Key<AlertSubType> key) {
		return ofy().load().key(key).now();
	}

	/** AlertSubType **/
	@Cacheable(value = "refdataAlertSubTypeAll")
	public Map<Key<AlertSubType>, AlertSubType> getAlertSubTypeMap() {
		List<Key<AlertSubType>> keys = ofy().load().type(AlertSubType.class).keys().list();
		return ofy().load().keys(keys);
	}

	// @Cacheable(value="refdataManufacturer", key="#id")
	public AlertSubType getAlertSubTypeById(Long id) {
		return ofy().load().type(AlertSubType.class).id(id).now();
	}

	@CacheEvict(value = "refdataAlertSubTypeAll", allEntries = true)
	public void saveAlertSubType(AlertSubType alertSubType) {
		// Clear the Alert Map manually =)
		cacheManager.getCache("refdataAlertTypeMap").clear();
		ofy().save().entity(alertSubType).now();
	}

	/** Full Alert Map  by category
	 * @param alertCategory TODO**/
	@Cacheable(value = "refdataAlertTypeMap")
	public Map<AlertType, Map<Key<AlertSubType>, AlertSubType>> getFullAlertTypeMap(AlertCategoryE alertCategory) {
		
		
		Map<AlertType, Map<Key<AlertSubType>, AlertSubType>> alertTypeMap = new LinkedHashMap<AlertType, Map<Key<AlertSubType>, AlertSubType>>();
		String category = alertCategory.toString();
		
		List<Key<AlertType>> alertTypeKeys = ofy().load().type(AlertType.class)
				.filter("alertCategory", category)
				.order("type").keys().list();
		
		for (Key<AlertType> alertTypeKey : alertTypeKeys) {
			/** Get the alert **/
			AlertType alertType = ofy().load().key(alertTypeKey).now();
			/** Get the subtype of the alert **/
			List<Key<AlertSubType>> alertSubTypeKeys = ofy()
													   .load()
													   .type(AlertSubType.class)
													   .ancestor(alertTypeKey)
													   .keys()
													   .list();
			Map<Key<AlertSubType>, AlertSubType> alertSubTypeMap = ofy().load().keys(alertSubTypeKeys);
			/** put it into the map **/
			alertTypeMap.put(alertType, alertSubTypeMap);
		}

		return alertTypeMap;
	}

	/** EventType **/
	@Cacheable(value = "refdataEventTypeAll")
	public Map<Key<EventType>, EventType> getEventTypeMap() {
		List<Key<EventType>> keys = ofy().load().type(EventType.class).keys().list();
		return ofy().load().keys(keys);
	}

	// @Cacheable(value="refdataManufacturer", key="#id")
	public EventType getEventTypeById(Long id) {	
		return ofy().load().type(EventType.class).id(id).now();
	}

	// @Cacheable(value="refdataManufacturer", key="#id")
	public EventType getEventTypeByKey(Key<EventType> key) {
		return ofy().load().key(key).now();
	}

	@CacheEvict(value = "refdataEventTypeAll", allEntries = true)
	public void saveEventType(EventType alertType) {
		ofy().save().entity(alertType).now();
	}

	// @Cacheable(value="refdataManufacturer", key="#id")
	public List<EventSubType> getEventSubTypeByParentId(Long parentId) {
		Key<EventType> parentKey = Key.create(EventType.class, parentId);
		return ofy().load().type(EventSubType.class).ancestor(parentKey).list();
	}

	// @Cacheable(value="refdataManufacturer", key="#id")
	public EventSubType getEventSubTypeByKey(Key<EventSubType> key) {
		return ofy().load().key(key).now();
	}

	/** EventSubType **/
	@Cacheable(value = "refdataEventSubTypeAll")
	public Map<Key<EventSubType>, EventSubType> getEventSubTypeMap() {
		List<Key<EventSubType>> keys = ofy().load().type(EventSubType.class).keys().list();
		return ofy().load().keys(keys);
	}

	// @Cacheable(value="refdataManufacturer", key="#id")
	public EventSubType getEventSubTypeById(Long id) {
		return ofy().load().type(EventSubType.class).id(id).now();
	}

	@CacheEvict(value = "refdataEventSubTypeAll", allEntries = true)
	public void saveEventSubType(EventSubType alertSubType) {
		ofy().save().entity(alertSubType).now();
	}

	/** Full Event Map **/
	@Cacheable(value = "refdataEventTypeMap")
	public Map<EventType, Map<Key<EventSubType>, EventSubType>> getFullEventTypeMap() {
		Map<EventType, Map<Key<EventSubType>, EventSubType>> eventTypeMap = new LinkedHashMap<EventType, Map<Key<EventSubType>, EventSubType>>();
		List<Key<EventType>> eventTypeKeys = ofy().load().type(EventType.class)
				.order("type").keys().list();

		for (Key<EventType> eventTypeKey : eventTypeKeys) {
			/** Get the alert **/
			EventType eventType = ofy().load().key(eventTypeKey).now();
			/** Get the subtype of the alert **/
			List<Key<EventSubType>> eventSubTypeKeys = ofy()
													   .load()
													   .type(EventSubType.class)
													   .ancestor(eventTypeKey)
													   .keys()
													   .list();
			Map<Key<EventSubType>, EventSubType> eventSubTypeMap = ofy().load().keys(eventSubTypeKeys);
			/** put it into the map **/
			eventTypeMap.put(eventType, eventSubTypeMap);
		}

		return eventTypeMap;
	}
	
	/** ProductType **/
	@Cacheable(value = "refdataProductTypeAll")
	public Map<Key<ProductType>, ProductType> getProductTypeMap() {
		List<Key<ProductType>> keys = ofy().load().type(ProductType.class).keys().list();
		return ofy().load().keys(keys);
	}

	// @Cacheable(value="refdataManufacturer", key="#id")
	public ProductType getProductTypeById(Long id) {
		return ofy().load().type(ProductType.class).id(id).now();
	}

	public ProductType getProductTypeByKey(Key<ProductType> key) {
		return ofy().load().key(key).now();
	}

	@CacheEvict(value = "refdataProductTypeAll", allEntries = true)
	public void saveProductType(ProductType alertType) {
		ofy().save().entity(alertType).now();
	}

	/** ProductSubType **/
	@Cacheable(value = "refdataProductSubTypeAll")
	public Map<Key<ProductSubType>, ProductSubType> getProductSubTypeMap() {
		List<Key<ProductSubType>> keys = ofy().load().type(ProductSubType.class).keys().list();
		return ofy().load().keys(keys);
	}

	// @Cacheable(value="refdataManufacturer", key="#id")
	public List<ProductSubType> getProductSubTypeByProductId(Long parentId) {
		Key<ProductType> parentKey = Key.create(ProductType.class, parentId);
		return ofy().load().type(ProductSubType.class).ancestor(parentKey).list();
	}

	// @Cacheable(value="refdataManufacturer", key="#id")
	public ProductSubType getProductSubTypeByKey(Key<ProductSubType> key) {
		return ofy().load().key(key).now();
	}

	@CacheEvict(value = "refdataProductSubTypeAll", allEntries = true)
	public void saveProductSubType(ProductSubType productSubType) {
		ofy().save().entity(productSubType).now();
	}

	/** Full Product Map **/
	@Cacheable(value = "refdataProductTypeMap")
	public Map<ProductType, Map<Key<ProductSubType>, ProductSubType>> getFullProductTypeMap() {
		Map<ProductType, Map<Key<ProductSubType>, ProductSubType>> productTypeMap = new LinkedHashMap<ProductType, Map<Key<ProductSubType>, ProductSubType>>();
		List<Key<ProductType>> productTypeKeys = ofy().load().type(ProductType.class)
				.order("type").keys().list();

		for (Key<ProductType> productTypeKey : productTypeKeys) {
			/** Get the alert **/
			ProductType productType = ofy().load().key(productTypeKey).now();
			/** Get the subtype of the alert **/
			List<Key<ProductSubType>> productSubTypeKeys = ofy()
														   .load()
														   .type(ProductSubType.class)
														   .ancestor(productTypeKey)
														   .keys()
														   .list();
			Map<Key<ProductSubType>, ProductSubType> productSubTypeMap = ofy().load().keys(productSubTypeKeys);
			/** put it into the map **/
			productTypeMap.put(productType, productSubTypeMap);
		}

		return productTypeMap;
	}

	public Key<Shop> getWarehouseShop() {
		
		Config c = configService.getConfigByName("WAREHOUSE.SHOP.ID");		
		c.getValue();		
		return Key.create(Shop.class, Long.valueOf(c.getValue()));
	}
	
	@Cacheable(value = "refdataSettings")
	public List<Setting> getAllSettingsByType(SettingTypes type) {
		
		List<Setting> settings = ofy().load().type(Setting.class).filter("type", type.toString()).list();		
		return settings;		
	}
	
	public Setting getSettingByType(SettingTypes type, String name) {

		List<Setting> settings = getAllSettingsByType(type);
		Setting settingToFind = null;
		
		for (Setting s: settings) {
			
			if (StringUtils.equalsIgnoreCase(s.getName(), name)) {
				settingToFind = s;
				break;
			}
		}
		
		return settingToFind;		
	}
	
	@CacheEvict(value = "refdataPrimaryAdp", allEntries = true)
	public Key<PrimaryAdpInfo>  savePrimaryAdpInfo(PrimaryAdpInfo info ) {

		return ofy().save().entity(info).now();
	}
	
	@Cacheable(value="refdataPrimaryAdp")
	public Map<String,Key<Shop>> getPrimaryAdpInfoMap() {
		
		
		List<PrimaryAdpInfo> keys = ofy().load().type(PrimaryAdpInfo.class).list();
		
		Map<String, Key<Shop>> map = new HashMap<>();
		
		for (PrimaryAdpInfo info : keys ) {
		
			map.put(info.getAdpNumber(),  info.getShopKey());
		}
		
		return map;		
	}
	
	public Map<Key<PrimaryAdpInfo>,PrimaryAdpInfo>  getPrimaryAdpInfoAll() {

		 List<Key<PrimaryAdpInfo>>keys =ofy().load().type(PrimaryAdpInfo.class).keys().list();
		 return ofy().load().keys(keys);
	}
	
	public PrimaryAdpInfo getPrimaryAdpInfo(Long id) {
		
		return ofy().load().type(PrimaryAdpInfo.class).id(id).now();
	}
	
	public void deletePrimaryAdpInfo(Long id) {

		ofy().delete().type(PrimaryAdpInfo.class).id(id).now();
	}
	
	public PrimaryAdpInfo getPrimaryAdpInfo(String adpNumber) {
		
		PrimaryAdpInfo info=null;
		try {
			info = ofy().load().type(PrimaryAdpInfo.class).filter("adpNumber", adpNumber).first().now();
		} catch (NotFoundException e) { 

			log.log(Level.WARNING, "unable to find primary adp info for " + adpNumber, e);
			
		}
		return info;
	}
	
	
	public Address getAddressForPrimaryAdpShop(String adpNumber) {
		
		PrimaryAdpInfo pai = getPrimaryAdpInfo(adpNumber);
		
		if (pai == null) { return null;} 
		
		Key<Shop >primaryShopKey = pai.getShopKey();
		Shop s = getShop(primaryShopKey);
		return s.getAddress();
	}
	


	/** Dentist **/
	@Cacheable(value = "refdataDentistAll")
	public Map<Key<Dentist>, Dentist> getDentistMap() {

		List<Key<Dentist>> keys = ofy().load().type(Dentist.class).order("lastName").order("firstName")
				.keys().list();
		return ofy().load().keys(keys);
	}

	public Dentist getDentistById(Long id) {

		return ofy().load().type(Dentist.class).id(id).now();
	}

	@CacheEvict(value = "refdataDentistAll", allEntries = true)
	public void saveDentist(Dentist dentist) {

		ofy().save().entity(dentist).now();
	}

	
	
	public void setCacheManager(EhCacheCacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public void setConfigService(ConfigService configService) {
		this.configService = configService;
	}
	
	public void setInvoiceSeqNumService(
			InvoiceSeqNumService invoiceSeqNumService) {
		this.invoiceSeqNumService = invoiceSeqNumService;
	}
}
