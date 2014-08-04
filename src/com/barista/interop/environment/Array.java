package com.barista.interop.environment;

import com.barista.interop.Type;

@Type()
public class Array<T> {


	public static final String PIZZA = "TRUE";
	
	public int length;
	
	public Array() {
	}
	
	/**
	 * Removes the last element from an array and returns that element.
	 * @return
	 */
	public native T pop();
	
	/**
	 * Adds one or more elements to the end of an array and returns the new length of the array.
	 * @param t
	 * @return
	 */
	public native int push(@SuppressWarnings("unchecked") T ...ts);
	
	
	/**
	 * Reverses the order of the elements of an array -- the first becomes the last, and the last becomes the first.
	 */
	public native void reverse();

	/**
	 * Removes the first element from an array and returns that element.
	 * @return
	 */
	public native T shift();
	
	/**
	 * Sorts the elements of an array.
	 */
	public native void sort();
	
	/**
	 * Sorts the elements of an array.
	 * @param cf
	 */
	public native void sort(CompareFunction<T> cf);
	
	/**
	 * Adds and/or removes elements from an array. 
	 */
	public native void splice();
	
	/**
	 * Adds one or more elements to the front of an array and returns the new length of the array.
	 */
	public native int unshift();
	
}
