package com.barista.interop.environment;

import com.barista.interop.Type;

@Type(name = "Math")
public class MathGlobals {

	public native static float random();
	
	public native static int floor(float f);

	public native static int ceil(float f);
	
	public native static int round(float f);
	
	public native static int min(int ... ints);
	
	public native static int max(int ... ints);
}
