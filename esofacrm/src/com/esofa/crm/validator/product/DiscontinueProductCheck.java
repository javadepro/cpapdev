package com.esofa.crm.validator.product;


import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


@Target( { METHOD, TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = DiscontinueProductCheckValidator.class)
@Documented
public @interface DiscontinueProductCheck {


    String message() default "{product.isActive.error}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
	    

	
}
