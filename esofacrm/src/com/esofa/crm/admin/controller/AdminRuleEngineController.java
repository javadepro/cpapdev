package com.esofa.crm.admin.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.esofa.crm.rule.Rule;
import com.esofa.crm.service.RuleEngineService;

@Controller
@RequestMapping(value = "/admin/rule-engine")
public class AdminRuleEngineController {

	@Autowired
	RuleEngineService ruleEngineService;
	
	Map actions = new HashMap();

	@RequestMapping(value = { "", "/", "list" }, method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();

		mav.addObject("rules", ruleEngineService.getRuleMap());
		mav.setViewName("admin-rule-engine-list");
		return mav;
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public ModelAndView view(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();

		Rule rule = null;

		try {
			rule = ruleEngineService.getRuleById(id);
			mav.addObject("rule", rule);
		} catch (Exception ex) {
			mav.addObject("message", "Object not found for id: " + id);
		}

		mav.setViewName("admin-rule-engine-view");
		return mav;
	}

	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public ModelAndView form(@RequestParam(required = false) Long id) {
		ModelAndView mav = new ModelAndView();

		Rule rule = null;
		if (id == null) {
			rule = new Rule();
		} else {
			try {
				rule = ruleEngineService.getRuleById(id);
				
			} catch (Exception ex) {
				mav.addObject("message", "Object not found for id: " + id);
			}
		}
		mav.addObject("actions", actions);
		mav.addObject("rule", rule);
		mav.setViewName("admin-rule-engine-form");
		return mav;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam(required = false) Long id) {
		ruleEngineService.deleteRule(id);
		ModelAndView mav = list();
		mav.addObject("message", "Rule Deleted");
		return mav;
	}

	@RequestMapping(value = "/formsubmit", method = RequestMethod.POST)
	public ModelAndView formSubmit(@ModelAttribute("rule") @Valid Rule rule,
			BindingResult result) {
		ModelAndView mav = new ModelAndView();
		if (result.hasErrors()) {

		} else {
			ruleEngineService.saveRule(rule);
		}
		mav.addObject("actions", actions);
		mav.addObject("rule", rule);
		mav.addObject("message", "Saved");
		mav.setViewName("admin-rule-engine-form");

		return mav;
	}

	public Map getActions() {
		return actions;
	}

	public void setActions(Map actions) {
		this.actions = actions;
	}
	
	

}
