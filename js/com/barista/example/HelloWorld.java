package com.barista.example;

import com.barista.interop.Entry;
import com.barista.interop.environment.Array;
import com.barista.interop.environment.CompareFunction;
import com.barista.interop.environment.Console;
import com.barista.interop.environment.JQuery;
import com.barista.interop.environment.MathGlobals;


public class HelloWorld {

	@Entry(IIFE = true)
	public static void main(String[] args) {
		
		HelloWorld hw = new HelloWorld();
		hw.awesomeness();
	}

	/**
	 * Run some stuff!
	 */
	private void awesomeness() {
		
		JQuery.create("#message").html("hello world boys!").show();
		
		Array<Integer> arr = new Array<Integer>();
		
		for (int i = 0; i < 100; i++) {
			arr.push((int) MathGlobals.floor(MathGlobals.random()));
		}
		
		arr.sort(new CompareFunction<Integer>() {
			@Override
			public int compare(Integer t1, Integer t2) {
				return t1 - t2;
			}
		});

		Console.log(arr);
	}
}
