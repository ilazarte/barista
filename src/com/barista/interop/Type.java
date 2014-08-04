package com.barista.interop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * This is the fully qualified name of the object to call.
 * @author perico
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Type {
	
	/**
	 * The name of the type.
	 * An empty value means do not name the type.
	 * @return
	 */
	String name() default "";
	
	/**
	 * The namespace of the class.
	 * An empty value means do not prefix this type.
	 * @return
	 */
	String namespace() default "";
}
