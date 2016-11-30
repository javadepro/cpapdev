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

import com.esofa.crm.refdata.model.AlertSubType;
import com.esofa.crm.refdata.model.AlertType;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.spring.controller.GaeEnhancedController;
import com.googlecode.objectify.Key;

@Controller
@RequestMapping(value = "/refdata/alert-type")
public class AlertTypeController extends GaeEnhancedController {
	private static final Logger log = Logger
			.getLogger(AlertTypeController.class.getName());
	
	@Autowired
	private RefdataService refdataService;
	
	@RequestMapping(value = {"","/", "list"}, method = RequestMethod.GET)
	public ModelAndView list(){
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("alertTypes", refdataService.getAlertTypeMap());
		
		mav.setViewName("refdata-alert-type-list");
		return mav;
	}
	
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public ModelAndView view(@RequestParam(required = false) Long id){
		ModelAndView mav = new ModelAndView();
		
		AlertType alertType = null;
		if(id==null){
			alertType = new AlertType();
		}else{
			try{
				alertType = refdataService.getAlertTypeById(id);
				mav.addObject("subtypes", refdataService.getAlertSubTypeByParentId(id));
			}catch(Exception ex){
				mav.addObject("message","Object not found for id: "+ id);
			}
		}
		mav.addObject("alertType", alertType);
		mav.setViewName("refdata-alert-type-view");
		return mav;
	}
	
	
	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public ModelAndView form(@RequestParam(required = false) Long id){
		ModelAndView mav = new ModelAndView();
		AlertType alertType = null;
		if(id==null){
			alertType = new AlertType();
		}else{
			try{
				alertType = refdataService.getAlertTypeById(id);
				mav.addObject("subtypes", refdataService.getAlertSubTypeByParentId(id));
			}catch(Exception ex){
				mav.addObject("message","Object not found for id: "+ id);
			}
		}
		mav.addObject("alertType", alertType);
		mav.setViewName("refdata-alert-type-form");
		return mav;
	}
	
	@RequestMapping(value = "/formsubmit", method = RequestMethod.POST)
	public ModelAndView formSubmit(@ModelAttribute("alertType") @Valid AlertType alertType,
			BindingResult result){
		ModelAndView mav = new ModelAndView();
		if(result.hasErrors()){
			
		}else{
			refdataService.saveAlertType(alertType);
			mav.addObject("message","Saved");
		}
		Key<AlertType> parentKey = Key.create(AlertType.class, alertType.getId());
		List<AlertSubType> subtypes = refdataService.getAlertSubTypeByParentId(alertType.getId());
		mav.addObject("subtypes", subtypes);
		mav.addObject("alertType", alertType);
		mav.setViewName("refdata-alert-type-form");

		
		return mav;
	}
	
	@RequestMapping(value = "/subtype-form", method = RequestMethod.GET)
	public ModelAndView subTypeForm(@RequestParam(required = false) Long id, @RequestParam(required = true) Long parentId){
		ModelAndView mav = new ModelAndView();
		AlertType alertType = null;
		AlertSubType alertSubType = null;
		if(id==null){
			// new subtype
			try{
				alertType = refdataService.getAlertTypeById(parentId);
				//mav.addObject("subtypes", refdataService.getAlertSubTypeByProductId(id));
			}catch(Exception ex){
				ex.printStackTrace();
				mav.addObject("message","Object not found for parentId: "+ parentId);
				mav.setViewName("message");
				return mav;
			}
			
			alertSubType = new AlertSubType();
			Key<AlertType> parentTypeKey = Key.create(AlertType.class, parentId);
			alertSubType.setParentType(parentTypeKey);
			
		}else{
			try{
				alertType = refdataService.getAlertTypeById(parentId);
				Key<AlertType> parentKey = Key.create(AlertType.class, parentId);
				Key<AlertSubType> key = Key.create(parentKey, AlertSubType.class, id);
				alertSubType = refdataService.getAlertSubTypeByKey(key);
				
			}catch(Exception ex){
				mav.addObject("message","Object not found for id: "+ id);
				mav.setViewName("message");
				return mav;
			}
		}
		mav.addObject("alertType", alertType);
		mav.addObject("alertSubType", alertSubType);
		mav.setViewName("refdata-alert-subtype-form");
		return mav;
	}
	
	@RequestMapping(value = "/subtype-formsubmit", method = RequestMethod.POST)
	public ModelAndView subTypeFormSubmit(@ModelAttribute("alertSubType") @Valid AlertSubType alertSubType,
			BindingResult result){
		ModelAndView mav = new ModelAndView();
		if(result.hasErrors()){
			
		}else{
			refdataService.saveAlertSubType(alertSubType);
		}
		mav.addObject("alertType", refdataService.getAlertTypeByKey(alertSubType.getParentType()));
		mav.addObject("alertSubType", alertSubType);
		mav.setViewName("refdata-alert-subtype-form");
		mav.addObject("message","Saved");
		return mav;
	}
	
}
