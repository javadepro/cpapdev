package com.esofa.crm.validator.product.inventory;

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
@Constraint(validatedBy = SufficientOriginationStockCheckValidator.class)
@Documented
public @interface SufficientOriginationStockCheck {


    String message() default "{inventorytransfer.insufficient.quantity.error}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

