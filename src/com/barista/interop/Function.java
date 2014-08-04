package com.barista.interop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marking a class means the class is to be understood as a single function interface.
 * As a wrapper, the methods are used as functions. 
 * @author perico
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Function {
	
	/**
	 * The namespace of the class.
	 * @return
	 */
	String namespace() default "";
	
	boolean wrapper() default false;
}
