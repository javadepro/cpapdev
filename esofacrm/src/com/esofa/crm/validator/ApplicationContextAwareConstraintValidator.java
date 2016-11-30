package com.esofa.crm.validator;

import java.lang.annotation.Annotation;

import javax.validation.ConstraintValidator;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public abstract class ApplicationContextAwareConstraintValidator<A  extends Annotation, T> implements ConstraintValidator<A, T>, ApplicationContextAware {

	protected ApplicationContext applicationContext;
 
	    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
	 
	        this.applicationContext = applicationContext;
	    }
}
