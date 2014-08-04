package com.barista.testsrc;

public class ArrayInitializer {

	public static void main(String[] args) {
		Object[] arr = new Object[] { "a", "b", new Object[] { 1, 2, 3 } };
		for (Object str : arr) {
			System.out.println(str);
		}
	}
}
