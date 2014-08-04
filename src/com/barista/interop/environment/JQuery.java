package com.barista.interop.environment;

import com.barista.interop.Constructor;
import com.barista.interop.Method;
import com.barista.interop.Type;

/**
 * Using native instead of interfaces gives the latitude of include properties on clases.
 * Also, we avoid null value java compile time warnings on methods which mimic constructors but return null.
 * @author perico
 */

@Type()
public class JQuery {
	
	@Constructor(functor = true)
	public native static JQuery create();
	
	@Constructor(functor = true)
	public native static JQuery create(String context);
	
	public native static String trim();
	
	public native JQuery find(String context);
	
	/**
	 * Here we return an integer value.
	 * The function annotation lets the compiler know to use the correct name.
	 * @param key
	 * @return
	 */
	@Method(name = "css")
	public native Integer cssInt(String key);

	public native String css(String key);

	public native JQuery css(String key, Object val);
	
	public native JQuery attr(String key, String value);
	
	public native JQuery attr(String key, Integer value);
	
	public native JQuery attr(String key, Float value);
	
	@Method(name = "attr")
	public native Integer attrStr(String key);

	@Method(name = "attr")
	public native Float attrFl(String key);
	
	public native JQuery html(String html);

	public native void show();
}
