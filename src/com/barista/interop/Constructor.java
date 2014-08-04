package com.barista.interop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks this function to be treated as a constructor at runtime.
 * Regardless of how it is defined in Java,
 * it will be rendered as 'new Typename(<params>)' with return type of Typename.
 * @author perico
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Constructor {
	
	/**
	 * @return
	 */
	String name() default "";

	/**
	 * Is this constructor a function?
	 * If a constructor is marked,
	 * the generated invocation will like so:
	 * 		var j = new JQuery(val);
	 * instead, function is true produces:
	 * 		var j = jQuery(val); 
	 * 
	 * @return
	 */
	boolean functor() default false;
}
