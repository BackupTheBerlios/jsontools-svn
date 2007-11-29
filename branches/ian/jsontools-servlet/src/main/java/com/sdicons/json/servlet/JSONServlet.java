/**
 *
 */
package com.sdicons.json.servlet;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sdicons.json.servlet.IJSONResponder;
import com.sdicons.json.servlet.JSONResponderDescription;
import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.parser.JSONParser;
import com.sdicons.json.annotations.JSONMethod;

/**
 * @author itaylor
 *
 */
public abstract class JSONServlet extends HttpServlet
{
	private static final Logger LOG = Logger.getLogger(JSONServlet.class);

	@Override
	public void init()
	{
		if (responderMap.isEmpty())
		{
			for (IJSONResponder responder : this.getJSONResponderClasses())
			{
				JSONResponderDescription responderDesc = new JSONResponderDescription(responder);
				responderMap.put(responder.getResponderName(), responderDesc);
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException
	{
		doResponse(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException
	{
		doResponse(req, resp);
	}

	protected void doResponse(HttpServletRequest req, HttpServletResponse resp) throws ServletException
	{
		if (req.getRequestURI().endsWith(getResponderSuffix()))
		{
			doJSON(req, resp);
		} else if (req.getRequestURI().endsWith(getJavascriptPrinterSuffix()))
		{
			doJavaScript(req, resp);
		} else
		{
			throw new ServletException("The request recieved was neither a JSON or a Javascript request.  It cannot be handled by this servlet.");
		}
	}

	private String getServletPathSansFile(String servletPath)
	{
		int lastSlashIndex = servletPath.lastIndexOf('/');
		if (lastSlashIndex == -1)
		{
			return "/";
		} else
		{
			return servletPath.substring(0, lastSlashIndex);
		}
	}

	protected void doJavaScript(HttpServletRequest req, HttpServletResponse resp) throws ServletException
	{
		resp.setContentType("text/javascript");
		try
		{
			resp.getWriter()
			    .write(getJSFunctionsScriptBody(getServletPathSansFile(req.getRequestURI())));
		} catch (Exception ex)
		{
			throw new ServletException("Error writing javascript", ex);
		}
	}

	protected void doJSON(HttpServletRequest req, HttpServletResponse resp) throws ServletException
	{
		try
		{
			ExecuteJSON(req, resp);
		} catch (Exception ex)
		{
			LOG.error("JSONServelet Error", ex);
			try
			{
				WriteJSONErrorResponse(ex, resp);
			} catch (Exception ex2)
			{
				throw new ServletException("Error writing JSON exception", ex2);
			}

		}
	}

	public abstract List<IJSONResponder> getJSONResponderClasses();

	public abstract String getResponderSuffix();

	public abstract String getJavascriptPrinterSuffix();

	protected static HashMap<String, JSONResponderDescription> responderMap = new HashMap<String, JSONResponderDescription>();

	protected void ExecuteJSON(HttpServletRequest req, HttpServletResponse resp) throws IOException, IllegalAccessException, InvocationTargetException
	{
		String functionName = req.getParameter("jsfunction");
		String requestURI = req.getRequestURI();
		String responderName = requestURI.substring(requestURI.lastIndexOf("/") + 1,
		                                            requestURI.indexOf(getResponderSuffix()));
		if ((responderName == null) || (responderMap.get(responderName) == null))
		{
			throw new RuntimeException("JSON Responder named: " + responderName + " was not found.");
		}
		if (functionName == null)
		{
			throw new RuntimeException("There was no jsfunction parameter passed to the JSONServlet.  No JSON call can be made");
		}

		JSONResponderDescription description = responderMap.get(responderName);
		Method method = description.getMethods().get(functionName);
		if (method == null)
		{
			throw new RuntimeException("There was no method registered with the function name " +
			        functionName);
		}
		Object result = runMethod(method, description.getResponderObj(), req);
		WriteJSONResponse(result, resp);

	}

	protected JSONObject buildJSONExceptionObj(Throwable ex)
	{
		JSONObject jsonException = new JSONObject();
		jsonException.getValue().put("exception", new JSONString(ex.getClass().getName()));
		if (ex.getMessage() != null)
		{
			jsonException.getValue().put("message", new JSONString(ex.getMessage()));
		}
		if ((ex.getCause() != null) && !ex.getCause().equals(ex))
		{
			jsonException.getValue().put("cause", buildJSONExceptionObj(ex.getCause()));
		}
		return jsonException;
	}

	protected void WriteJSONErrorResponse(Exception ex, HttpServletResponse resp) throws IOException
	{
		resp.setContentType("text/json");
		Writer writer = resp.getWriter();
		writer.write("/*-secure-\n");
		writer.write(buildJSONExceptionObj(ex).render(false));
		writer.write("*/");
		LOG.error("Exception thrown during JSON call", ex);
		ex.getStackTrace();
	}

	protected void WriteJSONResponse(Object value, HttpServletResponse resp) throws IOException
	{
		JSONValue jsonVal = JSONMapper.toJSON(value);
		resp.setContentType("text/json");
		Writer writer = resp.getWriter();
		writer.write("/*-secure-\n");
		writer.write(jsonVal.render(false));
		writer.write("*/");
	}

	@SuppressWarnings("unchecked")
	protected Object[] getMethodParameterValues(Type[] genericParameters, JSONMethod jsonMethod,
	                                            HttpServletRequest req)
	{
		String[] methodParams = jsonMethod.params();
		ArrayList<Object> paramValues = new ArrayList<Object>();

		for (int i = 0; i < methodParams.length; i++)
		{
			String paramValue = req.getParameter(methodParams[i]);

			if (paramValue != null)
			{
				Type paramType = genericParameters[i];
				JSONParser parser = new JSONParser(paramValue);
				JSONValue value = parser.nextValue();

				if (paramType instanceof ParameterizedType)
				{
					paramValues.add(JSONMapper.toJava(value, null, (ParameterizedType) paramType));
				} else
				{
					Class c = (Class) paramType;
					if (c.isAssignableFrom(JSONValue.class))
					{
						//Pass the jsonValue version of the parameter, they requested it.
						paramValues.add(value);
					} else
					{
						paramValues.add(JSONMapper.toJava(value, c));
					}
				}
			} else
			{
				paramValues.add(null);
			}
		}
		return paramValues.toArray();
	}

	protected Object runMethod(Method method, IJSONResponder responderObj, HttpServletRequest req) throws IllegalAccessException, InvocationTargetException
	{
		JSONMethod jsonMethod = method.getAnnotation(JSONMethod.class);
		Type[] paramTypes = method.getGenericParameterTypes();
		Object[] methodParameterValues = this.getMethodParameterValues(paramTypes, jsonMethod, req);
		if (method.getReturnType() == void.class)
		{
			method.invoke(responderObj, methodParameterValues);
			return null;
		}
		return method.invoke(responderObj, methodParameterValues);
	}

	private static String jsFunctionScript;

	public String getJSFunctionsScriptBody(String servletBaseUrl)
	{
		StringBuilder sb = new StringBuilder();
		if (jsFunctionScript == null)
		{
			sb.append(this.getJSCallbackFunction());

			for (String responderName : responderMap.keySet())
			{
				sb.append("var ");
				sb.append(responderName);
				sb.append(" = new Object(); \n");

				JSONResponderDescription responderDesc = responderMap.get(responderName);

				HashMap<String, Method> jsonMethods = responderDesc.getMethods();
				for (String jsFunctionName : jsonMethods.keySet())
				{
					JSONMethod jsonMethod = jsonMethods.get(jsFunctionName)
					                                   .getAnnotation(JSONMethod.class);

					sb.append(responderName);
					sb.append(".");
					sb.append(jsFunctionName);
					sb.append(" = function (onCompleteFunc");

					for (int i = 0; i < jsonMethod.params().length; i++)
					{
						sb.append(",");
						sb.append(jsonMethod.params()[i]);
					}
					sb.append(")\n{\n");

					sb.append("var dataToSend = new Array();\n");
					for (int i = 0; i < jsonMethod.params().length; i++)
					{
						String paramName = jsonMethod.params()[i];
						sb.append("dataToSend.push(\n{");
						sb.append("key:\"");
						sb.append(paramName);
						sb.append("\",");
						sb.append("value:");
						sb.append(paramName);
						sb.append("}\n);");
					}
					sb.append("\njsonServletCallback(\"");
					sb.append(servletBaseUrl);
					sb.append("/");
					sb.append(responderName);
					sb.append(this.getResponderSuffix());
					sb.append("?jsfunction=");
					sb.append(jsFunctionName);
					sb.append("\",onCompleteFunc,dataToSend)");
					sb.append(";\n}\n");
				}
			}
			jsFunctionScript = sb.toString();
		}
		return jsFunctionScript;
	}

	/**
	 * This generates the JS Callback function that is used to make ajax calls
	 * It requires that prototype.js has been loaded.
	 */
	public String getJSCallbackFunction()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("function jsonServletCallback (dataUrl, loadFunction, dataToSend)\n");
		sb.append("{\n");
		sb.append("if(!Prototype) {alert('prototype.js has not been loaded and is required to make this call'); return false;}");
		sb.append("if(!dataToSend){dataToSend = new Array();}\n");
		sb.append("if (loadFunction == null){throw(\"No loadFunction was passed into the function jsonServletCallback\");}\n");
		sb.append("var errorHandlerFunction = function(request, exception){throw(exception);}\n");
		sb.append("dataToSend.push({key:\"noCache\", value: Math.random() * 1000000000});\n");
		sb.append("var params = \"\";\n");
		sb.append("for (var i=0; i < dataToSend.length; i++)\n");
		sb.append("{params += \"&\";\n");
		sb.append("var keyValue = dataToSend[i];\n");
		sb.append("params += keyValue.key + \"=\"+ encodeURIComponent(Object.toJSON(keyValue.value));}\n");
		sb.append("var headers = ['json', 'true'];\n");
		sb.append("var successFunction = function (transport){loadFunction(transport.responseText.evalJSON(true), transport)}\n");
		sb.append("var myAjax = new Ajax.Request(dataUrl,{method: 'post',requestHeaders: headers,parameters: params,onComplete: successFunction, onException: errorHandlerFunction,onFailure: errorHandlerFunction});\n");
		sb.append("}\n");
		return sb.toString();
	}

	@Override
	protected long getLastModified(HttpServletRequest req)
	{
		return -1L;
	}

	private static final long serialVersionUID = 1L;

}
