package com.esofa.crm.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.esofa.crm.model.Customer;
import com.esofa.crm.service.CustomerService;
import com.esofa.spring.controller.GaeEnhancedController;
import com.googlecode.objectify.Key;

@Controller
@RequestMapping(value = "/ajax")
public class AjaxServiceController extends GaeEnhancedController{

	@Autowired
	CustomerService customerService;
	
	@RequestMapping(value = "/customer/search", method = RequestMethod.GET)
	public  @ResponseBody 
	Map<Key<Customer>,Customer> getCustomer(@RequestParam("searchString") String searchString){		
		Map<Key<Customer>,Customer> result = customerService.search(searchString);
		return result;
	}
	
	@RequestMapping(value = "/customer/search2", method = RequestMethod.GET)
	public  @ResponseBody 
	List<Customer> getCustomerList(@RequestParam("searchString") String searchString){		
		
		return new ArrayList<Customer>(customerService.search(searchString).values());
	}
	
	@ExceptionHandler(Exception.class)
    public @ResponseBody String handleIOException(Exception ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
    	System.out.println("handling1: unexpected error: " + ex.getLocalizedMessage());
    	response.setHeader("Content-Type", "application/json");
    	response.sendError(503, "" + ex.getLocalizedMessage());
    	System.out.println("handling2: RETURNING: unexpected error: " + ex.getLocalizedMessage());
    	return "unexpected error: " + ex.getLocalizedMessage();
    }
}
