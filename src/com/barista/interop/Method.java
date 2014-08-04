package com.barista.interop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a method of a class.
 * Most of the time can this can be infered from the type.
 * However, marking a method with a name we can work around java not having duck typing.
 * 
 * For example:
 * 	.css(String) in jquery will sometimes return a string.
 * 				 another valid possibility is int due to css size.
 *  
 *  create another method, in java code named cssInt and marked as "css" in the annotation.
 *  
 * 
 * @author perico
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Method {
	String name() default "";
}
