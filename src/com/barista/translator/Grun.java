package com.barista.translator;

import org.antlr.v4.runtime.misc.TestRig;

public class Grun {

	public static void main(String[] args) throws Exception {
		
		TestRig.main(args);
		
		args = new String[] {
				"com.barista.translator.gen.Java",
				"compilationUnit",
				"-gui",
				"js/com/barista/example/HelloWorld.java"
		};
		
		TestRig.main(args);
	}
}
