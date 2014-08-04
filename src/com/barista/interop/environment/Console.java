package com.barista.interop.environment;

import com.barista.interop.Type;

@Type(name = "console")
public class Console {

	public native static void log(Object obj);
	
	public native static void group(String name);
	
	public native static void groupEnd();
}
