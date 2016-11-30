package com.esofa.spring;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.google.appengine.api.utils.SystemProperty;

public class NonValidatingXmlWebApplicationContext extends XmlWebApplicationContext {
	
    protected void initBeanDefinitionReader(XmlBeanDefinitionReader beanDefinitionReader) {
        super.initBeanDefinitionReader(beanDefinitionReader);
        if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
          beanDefinitionReader.setValidating(false);
          beanDefinitionReader.setNamespaceAware(true);
        }
        
        
        //set the timezone for the jvm
        //TimeZone.setDefault(TimeZone.getTimeZone("Canada/Eastern"));        
    }

}