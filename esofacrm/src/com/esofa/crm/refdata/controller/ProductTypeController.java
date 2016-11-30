package com.esofa.crm.refdata.controller;

import java.util.List;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.esofa.crm.refdata.model.ProductSubType;
import com.esofa.crm.refdata.model.ProductType;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.spring.controller.GaeEnhancedController;
import com.googlecode.objectify.Key;

@Controller
@RequestMapping(value = "/refdata/product-type")
public class ProductTypeController extends GaeEnhancedController {
	private static final Logger log = Logger
			.getLogger(ProductTypeController.class.getName());
	
	@Autowired
	private RefdataService refdataService;
	
	@RequestMapping(value = {"","/", "list"}, method = RequestMethod.GET)
	public ModelAndView list(){
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("productTypes", refdataService.getProductTypeMap());
		
		mav.setViewName("refdata-product-type-list");
		return mav;
	}
	
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public ModelAndView view(@RequestParam(required = false) Long id){
		ModelAndView mav = new ModelAndView();
		
		ProductType productType = null;
		if(id==null){
			productType = new ProductType();
		}else{
			try{
				productType = refdataService.getProductTypeById(id);
				mav.addObject("subtypes", refdataService.getProductSubTypeByProductId(id));
			}catch(Exception ex){
				mav.addObject("message","Object not found for id: "+ id);
			}
		}
		mav.addObject("productType", productType);
		mav.setViewName("refdata-product-type-view");
		return mav;
	}
	
	
	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public ModelAndView form(@RequestParam(required = false) Long id){
		ModelAndView mav = new ModelAndView();
		ProductType productType = null;
		if(id==null){
			productType = new ProductType();
		}else{
			try{
				productType = refdataService.getProductTypeById(id);
				mav.addObject("subtypes", refdataService.getProductSubTypeByProductId(id));
			}catch(Exception ex){
				mav.addObject("message","Object not found for id: "+ id);
			}
		}
		mav.addObject("productType", productType);
		mav.setViewName("refdata-product-type-form");
		return mav;
	}
	
	@RequestMapping(value = "/formsubmit", method = RequestMethod.POST)
	public ModelAndView formSubmit(@ModelAttribute("productType") @Valid ProductType productType,
			BindingResult result){
		ModelAndView mav = new ModelAndView();
		if(result.hasErrors()){
			
		}else{
			refdataService.saveProductType(productType);
			mav.addObject("message","Saved");
		}
		Key parentKey = Key.create(ProductType.class, productType.getId());
		List<ProductSubType> subtypes = refdataService.getProductSubTypeByProductId(productType.getId());
		mav.addObject("subtypes", subtypes);
		mav.addObject("productType", productType);
		mav.setViewName("refdata-product-type-form");

		
		return mav;
	}
	
	@RequestMapping(value = "/subtype-form", method = RequestMethod.GET)
	public ModelAndView subTypeForm(@RequestParam(required = false) Long id, @RequestParam(required = true) Long parentId){
		ModelAndView mav = new ModelAndView();
		ProductType productType = null;
		ProductSubType productSubType = null;
		if(id==null){
			// new subtype
			try{
				productType = refdataService.getProductTypeById(parentId);
				//mav.addObject("subtypes", refdataService.getProductSubTypeByProductId(id));
			}catch(Exception ex){
				ex.printStackTrace();
				mav.addObject("message","Object not found for parentId: "+ parentId);
				mav.setViewName("message");
				return mav;
			}
			
			productSubType = new ProductSubType();
			Key<ProductType> parentTypeKey = Key.create(ProductType.class, parentId);
			productSubType.setParentType(parentTypeKey);
			
		}else{
			try{
				productType = refdataService.getProductTypeById(parentId);
				Key<ProductType> parentKey = Key.create(ProductType.class, parentId);
				Key<ProductSubType> key = Key.create(parentKey, ProductSubType.class, id);
				productSubType = refdataService.getProductSubTypeByKey(key);
				
			}catch(Exception ex){
				mav.addObject("message","Object not found for id: "+ id);
				mav.setViewName("message");
				return mav;
			}
		}
		mav.addObject("productType", productType);
		mav.addObject("productSubType", productSubType);
		mav.setViewName("refdata-product-subtype-form");
		return mav;
	}
	
	@RequestMapping(value = "/subtype-formsubmit", method = RequestMethod.POST)
	public ModelAndView subTypeFormSubmit(@ModelAttribute("productSubType") @Valid ProductSubType productSubType,
			BindingResult result){
		ModelAndView mav = new ModelAndView();
		if(result.hasErrors()){
			
		}else{
			refdataService.saveProductSubType(productSubType);
		}
		mav.addObject("productType", refdataService.getProductTypeByKey(productSubType.getParentType()));
		mav.addObject("productSubType", productSubType);
		mav.setViewName("refdata-product-subtype-form");
		mav.addObject("message","Saved");
		return mav;
	}

}
