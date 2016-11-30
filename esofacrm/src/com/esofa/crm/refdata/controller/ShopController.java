package com.esofa.crm.refdata.controller;

import java.util.Map;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.esofa.crm.common.model.Address;
import com.esofa.crm.refdata.model.PrimaryAdpInfo;
import com.esofa.crm.refdata.model.Shop;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.spring.controller.GaeEnhancedController;
import com.googlecode.objectify.Key;

@Controller
@RequestMapping(value = "/refdata/shop")
public class ShopController extends GaeEnhancedController {
	private static final Logger log = Logger.getLogger(ShopController.class
			.getName());

	@Autowired private RefdataService refdataService;
	
	@RequestMapping(value = { "", "/", "list" }, method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("shops", refdataService.getShopMap());
		mav.setViewName("refdata-shop-list");
		return mav;
	}
	
	@RequestMapping(value = { "/refresh" }, method = RequestMethod.GET)
	public ModelAndView refrseh() {
		ModelAndView mav = new ModelAndView();
		refdataService.refreshShop();
		return list();
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public ModelAndView view(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("shop", refdataService.getShopById(id));
		mav.setViewName("refdata-shop-view");
		return mav;
	}

	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public ModelAndView form(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
		Shop shop = null;
		if (id == null||id==0) {
			shop = new Shop();
		} else {
			try {
				shop = refdataService.getShopById(id);
				
			} catch (Exception ex) {
				mav.addObject("message", "Object not found for id: " + id);
				mav.setViewName("message");

			}
		}
		mav.addObject("shop", shop);
		mav.addObject("shopTypeList", Shop.ShopType.values());
		mav.addObject("countryList",Address.Country.values());
		mav.addObject("provinceList",Address.Province.values());
		mav.setViewName("refdata-shop-form");
		return mav;
	}

	@RequestMapping(value = "/formsubmit", method = RequestMethod.POST)
	public ModelAndView formSubmit(
			@ModelAttribute("shop") @Valid Shop shop,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();
		
		//Manual unique check.. should hv been in a constraint in annonation style
		ObjectError err  = uniqueCheck(shop);
		if(err != null) result.addError(err);

		if (result.hasErrors()) {
			
		}else{
			
			refdataService.saveShop(shop);
			mav.addObject("message", "Saved");
		}
		mav.addObject("shop", shop);

		mav.addObject("shopTypeList", Shop.ShopType.values());
		mav.addObject("countryList",Address.Country.values());
		mav.addObject("provinceList",Address.Province.values());

		mav.setViewName("refdata-shop-form");
		return mav;
	}
	
	@RequestMapping(value = { "/pai-list" }, method = RequestMethod.GET)
	public ModelAndView paiList(@RequestParam(required=false) boolean fwdFromSuccessfulSave) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("pais", refdataService.getPrimaryAdpInfoAll());
		mav.addObject("shops",refdataService.getActiveShopMap());
		
		//include successfully save msg if internal forward
		if (fwdFromSuccessfulSave) {
			mav.addObject("message", "Saved");
		}
		
		mav.setViewName("refdata-pai-list");
		return mav;
	}
	

	@RequestMapping(value = "/pai-form", method = RequestMethod.GET)
	public ModelAndView paiForm(@RequestParam(required = false) Long id
			, @RequestParam(required=false) boolean fwdFromSuccessfulSave) {
		ModelAndView mav = new ModelAndView();
		PrimaryAdpInfo pai = null;
		if (id == null||id==0) {
			pai = new PrimaryAdpInfo();
		} else {
			try {
				pai = refdataService.getPrimaryAdpInfo(id);
				
			} catch (Exception ex) {
				mav.addObject("message", "Object not found for id: " + id);
				mav.setViewName("message");

			}
		}
		
		//include successfully save msg if internal forward
		if (fwdFromSuccessfulSave) {
			mav.addObject("message", "Saved");
		}
		
		preparePaiMAV(mav, pai);
		mav.setViewName("refdata-pai-form");
		return mav;
	}

	
	@RequestMapping(value = "/pai-formsubmit", method = RequestMethod.POST)
	public ModelAndView paiFormSubmit(
			@ModelAttribute("pai") @Valid PrimaryAdpInfo pai,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();
		
		PrimaryAdpInfo exists = refdataService.getPrimaryAdpInfo(pai.getAdpNumber());
		
		if (exists == null) {
			Key<PrimaryAdpInfo> k = refdataService.savePrimaryAdpInfo(pai);

			mav =prepareRedirectToView("/refdata/shop/pai-list",StringUtils.EMPTY,StringUtils.EMPTY);

		} else {
			result.addError(new FieldError("pai", "adpNumber", "adpNumber provided is already a primary adp number"));

			preparePaiMAV(mav,pai);	
			mav.setViewName("refdata-pai-form");

		}

		return mav;
	}
	
	@RequestMapping(value = "/pai-delete", method = RequestMethod.GET)
	public ModelAndView paiDelete(@RequestParam(required = true) Long id) {
		ModelAndView mav = new ModelAndView();
		
		refdataService.deletePrimaryAdpInfo(id);
		return prepareRedirectToView("/refdata/shop/pai-list",StringUtils.EMPTY,StringUtils.EMPTY);
	}
	

	private ModelAndView preparePaiMAV(ModelAndView mav, PrimaryAdpInfo pai) {
		
		Map<Key<Shop>,Shop> shops =refdataService.getActiveShopMap();
		mav.addObject("shops",shops);
		mav.addObject("pai", pai);
		
		return mav;
	}
	
	protected ModelAndView prepareRedirectToView(String viewUrl, String keyName, Object keyValue) {
		
		ModelAndView mav = new ModelAndView("redirect:" +viewUrl);
		
		if (StringUtils.isNotEmpty(keyName)) {
			mav.addObject(keyName,keyValue);
		}
		mav.addObject("fwdFromSuccessfulSave",true);
		return mav;
	}
	
	
	
	private ObjectError uniqueCheck(Shop shop){
		Map<Key<Shop>,Shop> shopMap = refdataService.getShopMap();
		for (Shop s : shopMap.values()){
			if(s.getId().equals(shop.getId())){
				continue;
			}else{
				if(s.getName().equalsIgnoreCase(shop.getName())){
					return new FieldError("shop", "name", "Shop name has to be unique");
				} else if(s.getShortName().equalsIgnoreCase(shop.getShortName())){
						return new FieldError("shop", "name", "Shop name has to be unique");					
				}else if(s.getOrder()==shop.getOrder()){
					return new FieldError("shop", "order", "Shop's order has to be unique");
				}
			}
		}
		return null;
	}



}
