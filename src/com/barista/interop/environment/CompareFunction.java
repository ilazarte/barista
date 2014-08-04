package com.barista.interop.environment;

import com.barista.interop.Function;

@Function()
public class CompareFunction<T> {
	
	/**
	 * Use as a comparator
	 * @param t1
	 * @param t2
	 */
	public native int compare(T t1, T t2);
}
