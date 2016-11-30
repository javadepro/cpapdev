package com.esofa.crm.admin.controller;

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

import com.esofa.crm.model.Config;
import com.esofa.crm.service.ConfigService;

@Controller
@RequestMapping(value = "/admin/config")
public class AdminConfigController {
	
	private static final Logger log = Logger
			.getLogger(AdminConfigController.class.getName());

	@Autowired
	private ConfigService configService;
	
	@RequestMapping(value = { "", "/", "list" }, method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();

		mav.addObject("configs",
				configService.getConfigMap());
		mav.setViewName("admin-config-list");
		return mav;
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public ModelAndView view(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
		
		Config config = null;

		try {
			config = configService.getConfigById(id);
			mav.addObject("config", config);
		} catch (Exception ex) {
			mav.addObject("message", "Object not found for id: " + id);
		}

		mav.setViewName("admin-config-view");
		return mav;
	}

	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public ModelAndView form(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();
	
		Config config = null;
		if (id == null) {
			config = new Config();
		} else {
			try {
				config = configService.getConfigById(id);

			} catch (Exception ex) {
				mav.addObject("message", "Object not found for id: " + id);
			}
		}
		mav.addObject("config", config);
		mav.setViewName("admin-config-form");
		return mav;
	}

	@RequestMapping(value = "/formsubmit", method = RequestMethod.POST)
	public ModelAndView formSubmit(
			@ModelAttribute("config") @Valid Config config,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();
		if (result.hasErrors()) {
			
		}else{
			configService.saveConfig(config);
		}

		mav.addObject("config", config);

		mav.addObject("message", "Saved");
		mav.setViewName("admin-config-form");

		return mav;
	}

}
