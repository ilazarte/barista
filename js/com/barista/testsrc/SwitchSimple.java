package com.barista.testsrc;

public class SwitchSimple {

	public static void main(String[] args) {
		int start = 0;
		int val = 5;
		
		switch (val) {
		case 1:
			start++;
		case 2:
			start += 2;
		case 3:
		case 9:
		case 10:
			start += 3;
			break;
		case 5:
			start += 5;
		case 6:
			start += 6;
		case 7:
			start += 7;
			break;
		}
		
		/*
		 * if val == 1
		 * 	start++
		 *  start +=2
		 *  start +=3
		 * if val == 2
		 *   start += 2
		 *   start += 3
		 * if val == 3 or val == 9 or val == 10
		 *   start += 3
		 * etc...
		 */
		
		System.out.println("start: " + start);
	}
}
