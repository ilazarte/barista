package com.barista.interop.environment;

import com.barista.interop.Function;

@Function(wrapper = true)
public class Globals {

	public native static int parseInt(float f);
	
	public native static float parseFloat(float f);
}
