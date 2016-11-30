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

import com.esofa.crm.refdata.model.AppointmentPreference;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.spring.controller.GaeEnhancedController;

@Controller
@RequestMapping(value = "/refdata/appointment-preference")
public class AppointmentPreferenceController extends GaeEnhancedController {

private static final Logger log = Logger
			.getLogger(AppointmentPreferenceController.class.getName());

	@Autowired
	private RefdataService refdataService;

	@RequestMapping(value = { "", "/", "list" }, method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();

		mav.addObject("appointmentPreferences",
				refdataService.getAppointmentPreferenceMap());
		mav.setViewName("refdata-appointment-preference-list");
		return mav;
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public ModelAndView view(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
		
		AppointmentPreference appointmentPreference = null;

		try {
			appointmentPreference = refdataService.getAppointmentPreferenceById(id);
			mav.addObject("appointmentPreference", appointmentPreference);
		} catch (Exception ex) {
			mav.addObject("message", "Object not found for id: " + id);
		}

		mav.setViewName("refdata-appointment-preference-view");
		return mav;
	}

	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public ModelAndView form(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
	
		AppointmentPreference appointmentPreference = null;
		if (id == null) {
			appointmentPreference = new AppointmentPreference();
		} else {
			try {
				appointmentPreference = refdataService.getAppointmentPreferenceById(id);

			} catch (Exception ex) {
				mav.addObject("message", "Object not found for id: " + id);
			}
		}
		mav.addObject("appointmentPreference", appointmentPreference);
		mav.setViewName("refdata-appointment-preference-form");
		return mav;
	}

	@RequestMapping(value = "/formsubmit", method = RequestMethod.POST)
	public ModelAndView formSubmit(
			@ModelAttribute("appointmentPreference") @Valid AppointmentPreference appointmentPreference,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();
		if (result.hasErrors()) {
			
		}else{
			refdataService.saveAppointmentPreference(appointmentPreference);
		}

		mav.addObject("appointmentPreference", appointmentPreference);

		mav.addObject("message", "Saved");
		mav.setViewName("refdata-appointment-preference-form");

		return mav;
	}

	public RefdataService getRefdataService() {
		return refdataService;
	}

	public void setRefdataService(RefdataService refdataService) {
		this.refdataService = refdataService;
	}

}

