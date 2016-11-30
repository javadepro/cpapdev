package com.esofa.crm.controller;

import java.util.Map;
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

import com.esofa.crm.model.MailTemplate;
import com.esofa.crm.service.MailerService;
import com.esofa.spring.controller.GaeEnhancedController;

@Controller
@RequestMapping(value = "/mailer")
public class MailerController extends GaeEnhancedController {

	private static final Logger log = Logger.getLogger(MailerController.class
			.getName());
	
	@Autowired
	MailerService mailService;
	
	@RequestMapping(value = { "/template/list"}, method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		Map<?,?> templates = mailService.getMailTemplateMap();
		mav.addObject("mailTemplates", mailService.getMailTemplateMap());
		mav.setViewName("mailer-template-list");
		return mav;
	}
	

	@RequestMapping(value = "/template/view", method = RequestMethod.GET)
	public ModelAndView view(@RequestParam(required = false) Long id){
		ModelAndView mav = new ModelAndView();
		mav.addObject("mailTemplate", mailService.getgetCustomerByKeyById(id));
		mav.setViewName("mailer-template-view");
		return mav;
	}

	@RequestMapping(value = "/template/form", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam(required = false) Long id){
		ModelAndView mav = new ModelAndView();
		if(id==null||id==0){
			mav.addObject("mailTemplate", new MailTemplate());
		}else{
			mav.addObject("mailTemplate", mailService.getgetCustomerByKeyById(id));
		}
		mav.setViewName("mailer-template-form");
		return mav;
	}
	
	@RequestMapping(value = "/template/formsubmit", method = RequestMethod.POST)
	public ModelAndView save(@ModelAttribute("mailTemplate") @Valid MailTemplate mailTemplate,
			BindingResult result){
		ModelAndView mav = new ModelAndView();
		if(!result.hasErrors()){
			mailService.saveMailTemplate(mailTemplate);
		}
		mav.addObject("mailTemplate", mailTemplate);
		mav.setViewName("mailer-template-form");
		return mav;
	}
	
}
