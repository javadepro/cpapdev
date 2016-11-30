package com.esofa.crm.annotation.customer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * this class is used to annotate any field to indicate what tab it belongs to.
 * 
 * @author JHa
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CustomerTabIdentifier {

	CustomerTabNameE usedIn();
}
