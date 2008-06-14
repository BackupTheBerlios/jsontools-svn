/**
 *
 */
package com.sdicons.json.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sdicons.json.annotations.JSONMethod;
import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.parser.JSONParser;

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
			for (IJSONResponder responder : getJSONResponderClasses())
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
			ex.printStackTrace();
			LOG.error("Error writing javascript", ex);
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
			LOG.error("Error processing JSON request", ex);
			throw new ServletException("Error processing JSON request:" + ex.getMessage(), ex);
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
		if (responderName.equals("JSONServletBatchResponseHandler"))
		{
			ExecuteBatch(req, resp);
			return;
		}
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

		JSONMethod jsonMethod = method.getAnnotation(JSONMethod.class);
		String[] params = jsonMethod.params();
		Object result = runMethod(method, description.getResponderObj(), turnParamMapToJsonVer(req.getParameterMap(), params ));
		WriteJSONResponse(result, req, resp);
	}
	@SuppressWarnings("unchecked")
    private Map<String, JSONValue> turnParamMapToJsonVer(Map parameterMap, String[] functionParams)
	{
		Map<String, JSONValue> myMap = new HashMap<String, JSONValue>();
		for (String key : functionParams)
		{
			if (parameterMap.get(key) != null)
			{
				String value = ((String[])parameterMap.get(key))[0];
				myMap.put(key, new JSONParser(value).nextValue());
			}
		}
		return myMap;
	}
	protected void ExecuteBatch(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		String responderNamesStr = req.getParameter("responderNameArray");
		String dataToSendStr = req.getParameter("dataToSendArray");
		if (responderNamesStr == null || dataToSendStr == null)
		{
			throw new RuntimeException("The responderNameArray or dataToSendArray were null on a batch request");
		}
		JSONArray responderNames = (JSONArray) new JSONParser(responderNamesStr).nextValue();
		JSONArray dataToSend = (JSONArray) new JSONParser(dataToSendStr).nextValue();

		if (responderNames.size() != dataToSend.size())
		{
			throw new RuntimeException("The responderNameArray and dataToSendArray did not match in size");
		}
		ArrayList<Object> results = new ArrayList<Object>();
		for(int i=0; i < responderNames.size() ; i++)
		{

			String responderAndFxName = ((JSONString) responderNames.get(i)).getValue();

			String responderName =  responderAndFxName.split(getResponderSuffix())[0];
			String functionName = responderAndFxName.split("=")[1];

			if ((responderName == null) || (responderMap.get(responderName) == null))
			{
				throw new RuntimeException("JSON Responder named: " + responderName + " was not found.");
			}

			JSONObject dataValues = (JSONObject) dataToSend.get(i);
			Map<String, JSONValue> dataParams = dataValues.getValue();

			JSONResponderDescription description = responderMap.get(responderName);
			Method method = description.getMethods().get(functionName);

			if (method == null)
			{
				throw new RuntimeException("There was no method registered with the function name " +
				        functionName);
			}
			Object result = runMethod(method, description.getResponderObj(), dataParams);
			results.add(result);
		}
		WriteJSONResponse(results, req, resp);

	}

	protected JSONObject buildJSONExceptionObj(Throwable ex)
	{
		JSONObject jsonException = new JSONObject();
		LOG.info("Exception thrown in JSON Call.  Exception will be presented to Javascript.", ex);
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

	protected void WriteJSONResponse(Object value, HttpServletRequest req,  HttpServletResponse resp) throws IOException
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
	                                            Map<String, JSONValue> reqParameters)
	{
		String[] methodParams = jsonMethod.params();
		ArrayList<Object> paramValues = new ArrayList<Object>();

		for (int i = 0; i < methodParams.length; i++)
		{
			JSONValue paramValue = reqParameters.get(methodParams[i]);

			if (paramValue != null)
			{
				Type paramType = genericParameters[i];

				if (paramType instanceof ParameterizedType)
				{
					paramValues.add(JSONMapper.toJava(paramValue, null, (ParameterizedType) paramType));
				} else
				{
					Class c = (Class) paramType;
					if (c.isAssignableFrom(JSONValue.class))
					{
						//Pass the jsonValue version of the parameter, they requested it.
						paramValues.add(paramValue);
					} else
					{
						paramValues.add(JSONMapper.toJava(paramValue, c));
					}
				}
			} else
			{
				paramValues.add(null);
			}
		}
		return paramValues.toArray();
	}

	@SuppressWarnings("unchecked")
    protected Object runMethod(Method method, IJSONResponder responderObj, Map reqParams)
	{
		try
		{
			JSONMethod jsonMethod = method.getAnnotation(JSONMethod.class);
			Type[] paramTypes = method.getGenericParameterTypes();
			Object[] methodParameterValues = getMethodParameterValues(paramTypes, jsonMethod, reqParams);
			if (method.getReturnType() == void.class)
			{
				method.invoke(responderObj, methodParameterValues);
				return null;
			}
			return method.invoke(responderObj, methodParameterValues);
		}
		catch (InvocationTargetException ex)
		{
			Throwable realEx = ex.getCause();
			if (realEx != null)
			{
				return buildJSONExceptionObj(realEx);
			}
			return buildJSONExceptionObj(ex);
		}
		catch (Exception ex)
		{
			return buildJSONExceptionObj(ex);
		}
	}

	private static String jsFunctionScript;

	public String getJSFunctionsScriptBody(String servletBaseUrl)
	{
		StringBuilder sb = new StringBuilder();
		if (jsFunctionScript == null)
		{
			sb.append(getJSCallbackFunction(servletBaseUrl));

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
					sb.append(",batch)\n{\n");

					sb.append("var dataToSend = {\n");
					for (int i = 0; i < jsonMethod.params().length; i++)
					{
						String paramName = jsonMethod.params()[i];
						sb.append(paramName);
						sb.append(":");
						sb.append(paramName);
						if (i < jsonMethod.params().length -1 )
						{
							sb.append(",\n");
						}
					}
					sb.append("};\n");
					sb.append("\njsonServletCallback(\"");
					sb.append(responderName);
					sb.append(getResponderSuffix());
					sb.append("?jsfunction=");
					sb.append(jsFunctionName);
					sb.append("\",onCompleteFunc,dataToSend, batch)");
					sb.append(";\n}\n");
				}
			}
			jsFunctionScript = sb.toString();
		}
		return jsFunctionScript;
	}

	public String getJSCallbackFunction(String servletBaseUrl)
	{
		InputStream resourceAsStream = JSONServlet.class.getResourceAsStream("/jsCallback.js");
		if (resourceAsStream == null)
		{
			throw new RuntimeException("Unable to read jsCallback.js as resource");
		}
		StringBuilder sb = new StringBuilder();
		sb.append("var jsonServletDataUrl=\"");
		sb.append(servletBaseUrl);
		sb.append("/\";\n");
		BufferedReader in = new BufferedReader(new InputStreamReader(resourceAsStream));
		String line;
		try
        {
	        while ((line = in.readLine()) != null)
	        {
	        	sb.append(line + "\n");
	        }
        }
        catch (IOException ex)
        {
        	LOG.error("Couldn't read from jscallback.js", ex);
	       throw new RuntimeException("Couldn't read from jscallback.js", ex);
        }
		return sb.toString();
	}

	@Override
	protected long getLastModified(HttpServletRequest req)
	{
		return -1L;
	}

	private static final long serialVersionUID = 1L;

}
