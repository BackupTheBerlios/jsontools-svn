/**
 *
 */
package com.sdicons.json.servlet;

import java.util.ArrayList;
import java.util.List;

import com.sdicons.json.servlet.IJSONResponder;
import com.sdicons.json.servlet.JSONServlet;

/**
 * @author itaylor
 *
 */
public class JSONServletExample extends JSONServlet
{

    private static final long serialVersionUID = 1L;
	@Override
	public String getJavascriptPrinterSuffix()
	{
		return ".js";
	}


	@Override
	public List<IJSONResponder> getJSONResponderClasses()
	{
		ArrayList<IJSONResponder> responders = new ArrayList<IJSONResponder>();
		responders.add(new JSONResponderExample());
		return responders;
	}

	@Override
	public String getResponderSuffix()
	{
		return ".json";
	}

}
