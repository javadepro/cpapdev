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
import com.esofa.crm.refdata.model.DentalClinic;
import com.esofa.crm.refdata.service.RefdataService;
import com.esofa.spring.controller.GaeEnhancedController;

@Controller
@RequestMapping(value = "/refdata/dental-clinic")
public class DentalClinicController extends GaeEnhancedController {
	private static final Logger log = Logger.getLogger(DentalClinicController.class
			.getName());

	@Autowired private RefdataService refdataService;

	
	@RequestMapping(value = { "", "/", "list" }, method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("dentalClinics", refdataService.getDentalClinicMap());
		mav.setViewName("refdata-dental-clinic-list");
		return mav;
	}
	
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public ModelAndView view(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
		DentalClinic dentalClinic = null;
			try {
				dentalClinic = refdataService.getDentalClinicById(id);
				mav.addObject("dentalClinic", dentalClinic);
			} catch (Exception ex) {
				mav.addObject("message", "Object not found for id: " + id);
				mav.setViewName("message");
			}
		mav.setViewName("refdata-dental-clinic-view");
		return mav;
	}

	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public ModelAndView form(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
		DentalClinic dentalClinic = null;
		if (id == null) {
			dentalClinic = new DentalClinic();
		} else {
			try {
				dentalClinic = refdataService.getDentalClinicById(id);
				
			} catch (Exception ex) {
				mav.addObject("message", "Object not found for id: " + id);
				mav.setViewName("message");

			}
		}

		prepareFormMAV(mav,dentalClinic);	
		return mav;
	}

	@RequestMapping(value = "/formsubmit", method = RequestMethod.POST)
	public ModelAndView formSubmit(
			@ModelAttribute("dentalClinic") @Valid DentalClinic dentalClinic,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();
		if (result.hasErrors()) {
		}else{
			refdataService.saveDentalClinic(dentalClinic);
			mav.addObject("message", "Saved");
		}

		prepareFormMAV(mav,dentalClinic);		
		return mav;
	}

	private ModelAndView prepareFormMAV(ModelAndView mav, DentalClinic dentalClinic) {

		mav.addObject("dentalClinic", dentalClinic);
		mav.addObject("countryList",Address.Country.values());
		mav.addObject("provinceList",Address.Province.values());
		mav.setViewName("refdata-dental-clinic-form");
		return mav;
	}
	

}
