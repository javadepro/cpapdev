package com.esofa.crm.rule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import com.esofa.crm.messenger.model.WorkPackage;

@Component
public class RuleEngine {

	private static final Logger log = Logger
			.getLogger(RuleEngine.class.getName());
	
	private ExpressionParser parser;
	private List<Rule> rules;
	private Map<String, Action> actions;
	
	/**
	 * 
	 * 
	 */
	public RuleEngine(){
		parser = new SpelExpressionParser();
		rules = new ArrayList<Rule>();
	}
	
	public Map<String, Action> getActions() {
		return actions;
	}

	public void setActions(Map<String, Action> actions) {
		this.actions = actions;
	}

	public void processAndExecute(WorkPackage<Serializable,Serializable> workPackage){
		List<Rule> applicableRules = getApplicable(workPackage);
		for(Rule rule: applicableRules){
			try{
				Action action = actions.get(rule.getAction());
				log.info("Action:"+action.getClass()+" will be executed next!");
				workPackage.setResources(rule.getResources());
				workPackage.setAttribute(rule.getAttribute());
				workPackage.setDateOffset(rule.getDateOffset());
				action.execute(workPackage);
			}catch(Exception ex){
				log.fine("Exception"+ex.toString());
				ex.printStackTrace();
			}
			
		}
	}

	/**
	 * 
	 * @param workPackage
	 * @return
	 */
	public List<Rule> getApplicable(WorkPackage workPackage){
		List<Rule> applicableRules = new ArrayList<Rule>();
		log.info("Getting Applicable Rules");
		for(Rule rule : this.rules){
			
			log.info("Matching rule for this workpackage:");
			// Check if the input Class matches
			log.info("rule.getInputClass()"+rule.getInputClass());
			log.info("Workpackage.after:"+workPackage.getAfter().getClass().getName());
			if(rule.getInputClass().trim().equals(workPackage.getAfter().getClass().getName().trim())){
				log.info("rule.getCondition()"+rule.getCondition());
				Boolean applicable = (Boolean)parser.parseExpression(rule.getCondition()).getValue(workPackage);
				// check if expression is ok
				if(applicable){
					log.info(rule.getName()+": is applicable");
					
					applicableRules.add(rule);
				}
			}
		}
		Collections.sort(applicableRules);
		log.info("Got "+applicableRules.size()+" applicableRules");
		return applicableRules;
	}
	
	public List<Rule> getRules() {
		return rules;
	}

	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}
	
	public void addRule(Rule rule){
		rules.add(rule);
	}

	
}
