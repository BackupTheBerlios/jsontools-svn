/**
 *
 */
package com.sdicons.json.servlet;

import java.lang.reflect.Method;
import java.util.HashMap;

import com.sdicons.json.annotations.JSONMethod;

/**
 * @author itaylor
 *
 */
public class JSONResponderDescription
{
	public JSONResponderDescription(IJSONResponder responder)
	{
		this.responder = responder;
		getMethods();
	}
	private IJSONResponder responder;
	private HashMap<String, Method> theMethods;

	@SuppressWarnings("unchecked")
    protected HashMap<String, Method> getMethods()
	{
		if (theMethods == null)
		{
			theMethods = new HashMap<String, Method>();
			Class<IJSONResponder> clazz = (Class<IJSONResponder>) responder.getClass();
			for(Method m : clazz.getMethods())
			{
				JSONMethod methodDesc = m.getAnnotation(JSONMethod.class);
				if (methodDesc != null)
				{
					if (! theMethods.containsKey(methodDesc.JsFunctionName()))
                    {
						theMethods.put(methodDesc.JsFunctionName(), m);
                    }
					else
					{
						throw new RuntimeException("The JSON Servlet class has a duplicate jsfunction for the function " + methodDesc.JsFunctionName() + ".  Please ensure that jsfunction are unique");
					}
				}
			}
		}
		return theMethods;
	}
	public IJSONResponder getResponderObj()
	{
		return responder;
	}
}
