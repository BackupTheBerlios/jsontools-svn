/**
 *
 */
package com.sdicons.json.servlet;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;
import com.sdicons.json.genericwrapper.KeyValuePair;
import com.sdicons.json.genericwrapper.TestJSONObject;
import com.sdicons.json.genericwrapper.TestJSONSubObject;
import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.model.JSONValue;

/**
 * @author itaylor
 *
 */
public class JSONServletTest
{
	private ServletRunner sr;
	@BeforeTest
	public void startServlet() throws IOException, SAXException
	{
		sr = new ServletRunner(new File("src/test/resources/web.xml"));
	}
	@AfterTest
	public void stopServlet()
	{
		sr.shutDown();
	}
	@Test
	public void testJSONRequest() throws MalformedURLException, IOException, SAXException
	{
		ServletUnitClient sc = sr.newClient();
		WebRequest request = new PostMethodWebRequest("http://test.zloop.com/jsonServlet/TestJSONResponder.json?jsfunction=test1");
		request.setParameter("text", "\"mytext\"");
		request.setParameter("number", "2");
		WebResponse response = sc.getResponse(request);
		Assert.assertEquals(response.getText(), addSecure("\"mytext20\""));
	}
	@Test
	public void testJSONRequestWithObject() throws MalformedURLException, IOException, SAXException
	{
		ServletUnitClient sc = sr.newClient();
		WebRequest request = new PostMethodWebRequest("http://test.zloop.com/jsonServlet/TestJSONResponder.json?jsfunction=test2");
		TestJSONObject obj1 = getTestJSONObject(1);
		JSONValue val = JSONMapper.toJSON(obj1);
		request.setParameter("obj1", val.render(false));
		WebResponse response = sc.getResponse(request);
		Assert.assertEquals(response.getText(), addSecure("1"));

	}

	@Test
	public void testJSONRequestWithMultiObjectsReturnsObject() throws MalformedURLException, IOException, SAXException
	{
		ServletUnitClient sc = sr.newClient();
		WebRequest request = new PostMethodWebRequest("http://test.zloop.com/jsonServlet/TestJSONResponder.json?jsfunction=test3");
		TestJSONObject obj1 = getTestJSONObject(1);
		TestJSONObject obj2 = getTestJSONObject(2);
		JSONValue val1 = JSONMapper.toJSON(obj1);
		JSONValue val2 = JSONMapper.toJSON(obj2);

		request.setParameter("obj1", val1.render(false));
		request.setParameter("obj2", val2.render(false));
		WebResponse response = sc.getResponse(request);
		Assert.assertEquals(response.getText(), addSecure(val2.render(false)));
	}

	@Test
	public void testJSONRequestReturnsGenericStringList() throws MalformedURLException, IOException, SAXException
	{
		ServletUnitClient sc = sr.newClient();
		WebRequest request = new PostMethodWebRequest("http://test.zloop.com/jsonServlet/TestJSONResponder.json?jsfunction=test4");
		request.setParameter("startIndex", "0");
		request.setParameter("endIndex", "3");
		WebResponse response = sc.getResponse(request);
		Assert.assertEquals(response.getText(), addSecure("[\"string0\",\"string1\",\"string2\"]"));
	}

	@Test
	public void testJSONRequestReturnsDoubleGenericList() throws MalformedURLException, IOException, SAXException
	{
		ServletUnitClient sc = sr.newClient();
		WebRequest request = new PostMethodWebRequest("http://test.zloop.com/jsonServlet/TestJSONResponder.json?jsfunction=test5");

		ArrayList<KeyValuePair<String, Integer>> list = new ArrayList<KeyValuePair<String,Integer>>();
		int startIndex = 0;
		int endIndex = 3;
		for(int i = startIndex; i < endIndex; i++)
		{
			list.add(new KeyValuePair<String, Integer>("string", i));
		}

		request.setParameter("startIndex", "" + startIndex);
		request.setParameter("endIndex", "" + endIndex);
		WebResponse response = sc.getResponse(request);
		Assert.assertEquals(response.getText(), addSecure(JSONMapper.toJSON(list).render(false)));
	}
	@Test
	public void testJSONRequestPassesJSONValueWhenRequestedByMehtod() throws MalformedURLException, IOException, SAXException
	{
		ServletUnitClient sc = sr.newClient();
		WebRequest request = new PostMethodWebRequest("http://test.zloop.com/jsonServlet/TestJSONResponder.json?jsfunction=test7");

		request.setParameter("jsonValue", "\"test\"");

		WebResponse resp = sc.getResponse(request);
		Assert.assertEquals(resp.getText(), addSecure("\"test\""));
	}
	@Test
	public void testJSONRequestReturnsDoubleGenericListAsGet() throws MalformedURLException, IOException, SAXException
	{
		ServletUnitClient sc = sr.newClient();
		WebRequest request = new GetMethodWebRequest("http://test.zloop.com/jsonServlet/TestJSONResponder.json");

		ArrayList<KeyValuePair<String, Integer>> list = new ArrayList<KeyValuePair<String,Integer>>();
		int startIndex = 0;
		int endIndex = 3;
		for(int i = startIndex; i < endIndex; i++)
		{
			list.add(new KeyValuePair<String, Integer>("string", i));
		}
		request.setParameter("jsfunction", "test5");
		request.setParameter("startIndex", "" + startIndex);
		request.setParameter("endIndex", "" + endIndex);
		WebResponse response = sc.getResponse(request);
		Assert.assertEquals(response.getText(), addSecure(JSONMapper.toJSON(list).render(false)));
	}

	@Test
	public void testJSRequest() throws MalformedURLException, IOException, SAXException
	{
		ServletUnitClient sc = sr.newClient();
		WebRequest request = new GetMethodWebRequest("http://test.zloop.com/jsonServlet/foo.js");
		WebResponse response = sc.getResponse(request);
		String resultScript = response.getText();
		Assert.assertEquals(response.getContentType(), "text/javascript");
		//I don't really have a good way to test that the js that came out was valid...
		//I can test that it contains the functions that I expect though.
		Assert.assertTrue(resultScript.contains("TestJSONResponder.test1 = function (onCompleteFunc,text,number)"));
	}

	@Test
	public void testVoidJSONMethod() throws MalformedURLException, IOException, SAXException
	{
		ServletUnitClient sc = sr.newClient();
		WebRequest request = new PostMethodWebRequest("http://test.zloop.com/jsonServlet/TestJSONResponder.json");

		request.setParameter("jsfunction", "test6");
		request.setParameter("str1", "\"hello\"");
		WebResponse response = sc.getResponse(request);
		Assert.assertEquals(response.getText(), addSecure("null"));
	}

	@Test
	public void testUndefined() throws MalformedURLException, IOException, SAXException
	{
		ServletUnitClient sc = sr.newClient();
		WebRequest request = new PostMethodWebRequest("http://test.zloop.com/jsonServlet/TestJSONResponder.json");

		request.setParameter("jsfunction", "test8");
		request.setParameter("paramValue", "\"undefined\"");
		WebResponse response = sc.getResponse(request);

		Assert.assertFalse(Boolean.parseBoolean(response.getText()));
	}

	private String addSecure(String s)
	{
		return "/*-secure-\n" + s + "*/";
	}

	private TestJSONObject getTestJSONObject(int seed)
	{
		TestJSONObject tjo = new TestJSONObject();
		tjo.setTestInt(seed);
		tjo.setTestString("String" + seed);
		tjo.setTestStringList(this.getTestStringList(seed));
		tjo.setTestSubObject(this.getTestJSONSubObject(seed));
		tjo.setTestMap(this.getTestMap(seed));
		tjo.setTestDoubleNestedGeneric(this.getTestDoubleNestedGeneric(seed));
		return tjo;

	}

	private List<KeyValuePair<String, Integer>> getTestDoubleNestedGeneric(int seed)
	{
		List<KeyValuePair<String, Integer>> list = new ArrayList<KeyValuePair<String, Integer>>();
		list.add(new KeyValuePair<String, Integer>("test" + seed, seed));
		list.add(new KeyValuePair<String, Integer>("test" + seed + 1, seed + 1));
		return list;
	}

	private Map<String, TestJSONSubObject> getTestMap(int seed)
	{

		Map<String, TestJSONSubObject> myMap = new HashMap<String, TestJSONSubObject>();

		myMap.put("Key_" + seed + "_1", this.getTestJSONSubObject(0));
		myMap.put("Key_" + seed + "_2", this.getTestJSONSubObject(1));

		return myMap;
	}

	private List<String> getTestStringList(int seed)
	{
		List<String> stringList = new ArrayList<String>();
		stringList.add("String_" + seed + "_1");
		stringList.add("String_" + seed + "_2");
		return stringList;
	}

	private TestJSONSubObject getTestJSONSubObject(int seed)
	{
		TestJSONSubObject tjso = new TestJSONSubObject();
		tjso.setTestInt(seed);
		tjso.setTestString("String" + seed);

		tjso.setTestStringList(this.getTestStringList(seed));
		return tjso;
	}


}
