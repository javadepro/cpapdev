package com.esofa.crm.refdata.controller;

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

import com.esofa.crm.refdata.model.ContactPreference;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.spring.controller.GaeEnhancedController;

@Controller
@RequestMapping(value = "/refdata/contact-preference")
public class ContactPreferenceController extends GaeEnhancedController {
	private static final Logger log = Logger
			.getLogger(ContactPreferenceController.class.getName());

	@Autowired
	private RefdataService refdataService;

	@RequestMapping(value = { "", "/", "list" }, method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();

		mav.addObject("contactPreferences",
				refdataService.getContactPreferenceMap());
		mav.setViewName("refdata-contact-preference-list");
		return mav;
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public ModelAndView view(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
		
		ContactPreference contactPreference = null;

		try {
			contactPreference = refdataService.getContactPreferenceById(id);
			mav.addObject("contactPreference", contactPreference);
		} catch (Exception ex) {
			mav.addObject("message", "Object not found for id: " + id);
		}

		mav.setViewName("refdata-contact-preference-view");
		return mav;
	}

	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public ModelAndView form(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
	
		ContactPreference contactPreference = null;
		if (id == null) {
			contactPreference = new ContactPreference();
		} else {
			try {
				contactPreference = refdataService.getContactPreferenceById(id);

			} catch (Exception ex) {
				mav.addObject("message", "Object not found for id: " + id);
			}
		}
		mav.addObject("contactPreference", contactPreference);
		mav.setViewName("refdata-contact-preference-form");
		return mav;
	}

	@RequestMapping(value = "/formsubmit", method = RequestMethod.POST)
	public ModelAndView formSubmit(
			@ModelAttribute("contactPreference") @Valid ContactPreference contactPreference,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();
		if (result.hasErrors()) {
			
		}else{
			refdataService.saveContactPreference(contactPreference);
		}

		mav.addObject("contactPreference", contactPreference);

		mav.addObject("message", "Saved");
		mav.setViewName("refdata-contact-preference-form");

		return mav;
	}

	public RefdataService getRefdataService() {
		return refdataService;
	}

	public void setRefdataService(RefdataService refdataService) {
		this.refdataService = refdataService;
	}

}
