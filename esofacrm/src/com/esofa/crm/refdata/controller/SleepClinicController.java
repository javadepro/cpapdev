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

import com.esofa.crm.common.model.Address;
import com.esofa.crm.refdata.model.SleepClinic;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.spring.controller.GaeEnhancedController;

@Controller
@RequestMapping(value = "/refdata/sleep-clinic")
public class SleepClinicController extends GaeEnhancedController {
	private static final Logger log = Logger.getLogger(SleepClinicController.class
			.getName());

	@Autowired private RefdataService refdataService;

	
	@RequestMapping(value = { "", "/", "list" }, method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("sleepClinics", refdataService.getSleepClinicMap());
		mav.setViewName("refdata-sleep-clinic-list");
		return mav;
	}
	
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public ModelAndView view(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
		SleepClinic sleepClinic = null;
			try {
				sleepClinic = refdataService.getSleepClinicById(id);
				mav.addObject("sleepClinic", sleepClinic);
			} catch (Exception ex) {
				mav.addObject("message", "Object not found for id: " + id);
				mav.setViewName("message");
			}
		mav.setViewName("refdata-sleep-clinic-view");
		return mav;
	}

	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public ModelAndView form(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
		SleepClinic sleepClinic = null;
		if (id == null) {
			sleepClinic = new SleepClinic();
		} else {
			try {
				sleepClinic = refdataService.getSleepClinicById(id);
				
			} catch (Exception ex) {
				mav.addObject("message", "Object not found for id: " + id);
				mav.setViewName("message");

			}
		}
		mav.addObject("sleepClinic", sleepClinic);
		mav.addObject("countryList",Address.Country.values());
		mav.addObject("provinceList",Address.Province.values());
		mav.setViewName("refdata-sleep-clinic-form");
		return mav;
	}

	@RequestMapping(value = "/formsubmit", method = RequestMethod.POST)
	public ModelAndView formSubmit(
			@ModelAttribute("sleepClinic") @Valid SleepClinic sleepClinic,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();
		if (result.hasErrors()) {
		}else{
			refdataService.saveSleepClinic(sleepClinic);
			mav.addObject("message", "Saved");
		}

		mav.addObject("sleepClinic", sleepClinic);
		mav.addObject("countryList",Address.Country.values());
		mav.addObject("provinceList",Address.Province.values());
		
		mav.setViewName("refdata-sleep-clinic-form");
		return mav;
	}

	

}
