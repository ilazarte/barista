package com.barista.interop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Entry points into a javascript application.
 * @author perico
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Entry {
	
	/**
	 * The method compiles to a js function and is assigned to the variable.
	 * i.e. if assign is "myobj" the result of HelloWorld#main is:
	 * myobj = Helloworld.main()
	 * 
	 * @return
	 */
	String assign() default "";

	/**
	 * The method is returned as a reference if set to true.
	 * continuing with example above
	 * myobj = Helloworld.main
	 * 
	 * @return
	 */
	boolean function() default false;
	
	/**
	 * http://benalman.com/news/2010/11/immediately-invoked-function-expression/
	 * The method invokes the function in the top level context wrapped for var safety.
	 * 
	 * (function() {
	 * 		Helloworld.main()
	 * });
	 * 
	 * @return
	 */
	boolean IIFE() default false;
}
