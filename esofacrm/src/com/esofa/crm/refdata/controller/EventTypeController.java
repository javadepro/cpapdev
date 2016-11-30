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

import com.esofa.crm.refdata.model.EventSubType;
import com.esofa.crm.refdata.model.EventType;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.spring.controller.GaeEnhancedController;
import com.googlecode.objectify.Key;

@Controller
@RequestMapping(value = "/refdata/event-type")
public class EventTypeController extends GaeEnhancedController {
	private static final Logger log = Logger
			.getLogger(EventTypeController.class.getName());
	
	@Autowired
	private RefdataService refdataService;
	
	@RequestMapping(value = {"","/", "list"}, method = RequestMethod.GET)
	public ModelAndView list(){
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("eventTypes", refdataService.getEventTypeMap());
		
		mav.setViewName("refdata-event-type-list");
		return mav;
	}
	
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public ModelAndView view(@RequestParam(required = false) Long id){
		ModelAndView mav = new ModelAndView();
		
		EventType eventType = null;
		if(id==null){
			eventType = new EventType();
		}else{
			try{
				eventType = refdataService.getEventTypeById(id);
				mav.addObject("subtypes", refdataService.getEventSubTypeByParentId(id));
			}catch(Exception ex){
				mav.addObject("message","Object not found for id: "+ id);
			}
		}
		mav.addObject("eventType", eventType);
		mav.setViewName("refdata-event-type-view");
		return mav;
	}
	
	
	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public ModelAndView form(@RequestParam(required = false) Long id){
		ModelAndView mav = new ModelAndView();
		EventType eventType = null;
		if(id==null){
			eventType = new EventType();
		}else{
			try{
				eventType = refdataService.getEventTypeById(id);
				mav.addObject("subtypes", refdataService.getEventSubTypeByParentId(id));
			}catch(Exception ex){
				mav.addObject("message","Object not found for id: "+ id);
			}
		}
		mav.addObject("eventType", eventType);
		mav.setViewName("refdata-event-type-form");
		return mav;
	}
	
	@RequestMapping(value = "/formsubmit", method = RequestMethod.POST)
	public ModelAndView formSubmit(@ModelAttribute("eventType") @Valid EventType eventType,
			BindingResult result){
		ModelAndView mav = new ModelAndView();
		if(result.hasErrors()){
			
		}else{
			refdataService.saveEventType(eventType);
			mav.addObject("message","Saved");
		}
		Key parentKey = Key.create(EventType.class, eventType.getId());
		List<EventSubType> subtypes = refdataService.getEventSubTypeByParentId(eventType.getId());
		mav.addObject("subtypes", subtypes);
		mav.addObject("eventType", eventType);
		mav.setViewName("refdata-event-type-form");

		
		return mav;
	}
	
	@RequestMapping(value = "/subtype-form", method = RequestMethod.GET)
	public ModelAndView subTypeForm(@RequestParam(required = false) Long id, @RequestParam(required = true) Long parentId){
		ModelAndView mav = new ModelAndView();
		EventType eventType = null;
		EventSubType eventSubType = null;
		if(id==null){
			// new subtype
			try{
				eventType = refdataService.getEventTypeById(parentId);
				//mav.addObject("subtypes", refdataService.getEventSubTypeByProductId(id));
			}catch(Exception ex){
				ex.printStackTrace();
				mav.addObject("message","Object not found for parentId: "+ parentId);
				mav.setViewName("message");
				return mav;
			}
			
			eventSubType = new EventSubType();
			Key<EventType> parentTypeKey = Key.create(EventType.class, parentId);
			eventSubType.setParentType(parentTypeKey);
			
		}else{
			try{
				eventType = refdataService.getEventTypeById(parentId);
				Key<EventType> parentKey = Key.create(EventType.class, parentId);
				Key<EventSubType> key = Key.create(parentKey, EventSubType.class, id);
				eventSubType = refdataService.getEventSubTypeByKey(key);
				
			}catch(Exception ex){
				mav.addObject("message","Object not found for id: "+ id);
				mav.setViewName("message");
				return mav;
			}
		}
		mav.addObject("eventType", eventType);
		mav.addObject("eventSubType", eventSubType);
		mav.setViewName("refdata-event-subtype-form");
		return mav;
	}
	
	@RequestMapping(value = "/subtype-formsubmit", method = RequestMethod.POST)
	public ModelAndView subTypeFormSubmit(@ModelAttribute("eventSubType") @Valid EventSubType eventSubType,
			BindingResult result){
		ModelAndView mav = new ModelAndView();
		if(result.hasErrors()){
			
		}else{
			refdataService.saveEventSubType(eventSubType);
		}
		mav.addObject("eventType", refdataService.getEventTypeByKey(eventSubType.getParentType()));
		mav.addObject("eventSubType", eventSubType);
		mav.setViewName("refdata-event-subtype-form");
		mav.addObject("message","Saved");
		return mav;
	}
	
}
