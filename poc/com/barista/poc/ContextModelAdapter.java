package com.barista.poc;

import java.lang.reflect.Method;

import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ModelAdaptor;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

public class ContextModelAdapter implements ModelAdaptor {

	@Override
	public Object getProperty(Interpreter interpreter, ST st, Object o,
			Object property, String propertyName) throws STNoSuchPropertyException {
		
		System.out.println(propertyName);
		
	    Method m = null;
	    try {
	        String mn = "get" + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
	        m = o.getClass().getMethod(mn);
	    } catch (Exception e) {
	    }
	    
	    if (m == null) { 
	        try {
	            m = o.getClass().getDeclaredMethod(propertyName);
	        } catch (Exception e) {
	        }
	    }
	    
		if (m != null) {
			try {
				Object invoke = m.invoke(o);
				//System.out.println("property value: " + invoke.toString());
				return invoke;
			} catch (Exception e) {
				System.err.println("Exception invoking: "  + propertyName);
				throw new STNoSuchPropertyException(e, property, propertyName);
			}
		} else {
			System.err.println("No such property or method found " + propertyName);
			throw new STNoSuchPropertyException(null, property, propertyName);
		}
	}

}
