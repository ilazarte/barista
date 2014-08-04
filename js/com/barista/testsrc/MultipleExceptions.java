package com.barista.testsrc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class MultipleExceptions {

	public static void main(String[] args) {
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
		}
		
		int i = 3;
		i++;
		System.out.println(i);
	}
}
