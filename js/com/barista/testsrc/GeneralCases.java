package com.barista.testsrc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GeneralCases extends Parent {

	public static final String DEBUG = "this statement is static";
	
	private String description;
	
	public String pizza = "fats", derp = "true";
	
	static {
		System.out.println("hello from static context");
	}
	
	{
		System.out.println("non static and ignored by transpiler!");
	}
	
	public static void main(String[] args) {
		
		int three = 3;
		int five = 1 + (2 * 2);
		System.out.printf("%s %s%n printing in main method%n", three, five);
		
		if (five > 4) {
			System.out.println("yes its true");
			System.out.println("cool right?");
		} else {
			System.out.println("yeah, uh never.");
		}
		
		for (int i = 2; i < 4; i++) {
			System.out.println(i);
		}
		
		for (;;) {
			System.out.println("exactly one execution");
			break;
		}
		
		while (three < five) {
			three++;
			System.out.println("new value of three: " + three);
		}
		
		int i = 0;
		
		do {
			i++;
		} while (i < 10);
		
		String[] hm = new String[] {"a", "b", "c"};
		System.out.println("hm0: " + hm[0]);
		
		for (String hmElem : hm) {
			System.out.println(hmElem);
		}
		
		String datestr = "10-12-2009";
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		
		try {
			sdf.parse(datestr);
			File file = new File("asdf");
			FileInputStream fis = new FileInputStream(file);
			fis.read();
			fis.close();
		} catch (ParseException pe) {
			throw new RuntimeException(pe);
		} catch (FileNotFoundException fnfe) {
			throw new RuntimeException(fnfe);
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		};
		
		GeneralCases sf = new GeneralCases();
		sf.run();
		
		System.out.println("My test statement: " + DEBUG);
	}
	
	public static void out(Object obj, String stuff) {
		System.out.println(obj + stuff);
	}
	
	public static String conc(String a, String b) {
		return a + b;
	}
	
	public void run() {
		String derp = "ext1";
		String message = "ext2";
		String full = derp + ", " + message + " ext3";
		String other = conc(derp, "ivan");
		System.out.println(full);
		String first = "derp" + "three" + (new String("hello + dude")) + "end";
		out(true, other);
		out(new Date(), " is today");
		System.out.println(first);
		this.print();
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}


class Parent {
	public void print() {
		System.out.println("Hello!");
	}
}