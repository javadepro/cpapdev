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
import com.esofa.crm.refdata.model.SleepDoctor;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.spring.controller.GaeEnhancedController;

@Controller
@RequestMapping(value = "/refdata/sleep-doctor")
public class SleepDoctorController extends GaeEnhancedController {
	private static final Logger log = Logger.getLogger(SleepDoctorController.class
			.getName());

	@Autowired private RefdataService refdataService;

	
	@RequestMapping(value = { "", "/", "list" }, method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("sleepDoctors", refdataService.getSleepDoctorMap());
		mav.addObject("clinics", refdataService.getSleepClinicMap());
		mav.setViewName("refdata-sleep-doctor-list");
		return mav;
	}
	
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public ModelAndView view(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
		
			try {
				mav.addObject("sleepDoctor", refdataService.getSleepDoctorById(id));
			} catch (Exception ex) {
				mav.addObject("message", "Object not found for id: " + id);
				mav.setViewName("message");
			}
		mav.addObject("sleepClinics", refdataService.getSleepClinicMap());
		mav.setViewName("refdata-sleep-doctor-view");
		
		return mav;
	}

	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public ModelAndView form(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
		SleepDoctor sleepDoctor = null;
		if (id == null) {
			sleepDoctor = new SleepDoctor();
		} else {
			try {
				sleepDoctor = refdataService.getSleepDoctorById(id);
				
			} catch (Exception ex) {
				mav.addObject("message", "Object not found for id: " + id);
				mav.setViewName("message");

			}
		}
		mav.addObject("sleepDoctor", sleepDoctor);
		mav.addObject("sleepClinics", refdataService.getSleepClinicMap());
		mav.addObject("countryList",Address.Country.values());
		mav.addObject("provinceList",Address.Province.values());
		mav.setViewName("refdata-sleep-doctor-form");
		return mav;
	}

	@RequestMapping(value = "/formsubmit", method = RequestMethod.POST)
	public ModelAndView formSubmit(
			@ModelAttribute("sleepDoctor") @Valid SleepDoctor sleepDoctor,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();
		if (result.hasErrors()) {
			
		}else{
			refdataService.saveSleepDoctor(sleepDoctor);
			mav.addObject("message", "Saved");
			
		}
		mav.addObject("sleepDoctor", sleepDoctor);
		mav.addObject("countryList",Address.Country.values());
		mav.addObject("provinceList",Address.Province.values());
		mav.addObject("sleepClinics", refdataService.getSleepClinicMap());
		mav.setViewName("refdata-sleep-doctor-form");

		return mav;
	}

	

}
