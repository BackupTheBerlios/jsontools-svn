/**
 *
 */
package com.sdicons.json.servlet;

import java.util.ArrayList;
import java.util.List;

import com.sdicons.json.genericwrapper.KeyValuePair;
import com.sdicons.json.genericwrapper.TestJSONObject;
import com.sdicons.json.helper.TypeRef;
import com.sdicons.json.mapper.JSONMapperWrapper;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.parser.JSONParser;
import com.sdicons.json.annotations.JSONMethod;
import com.sdicons.json.servlet.IJSONResponder;

/**
 * @author itaylor
 *
 */
public class JSONResponderExample implements IJSONResponder
{

	public String getResponderName()
	{
		return "TestJSONResponder";
	}

	@JSONMethod(JsFunctionName="test1", params= {"text" , "number"})
	public String test1(String text, int number)
	{
		return text + (number *10);
	}

	@JSONMethod(JsFunctionName="test2", params = {"obj1"})
	public int test2(TestJSONObject obj1)
	{
		return obj1.getTestInt();
	}

	@JSONMethod(JsFunctionName="test3", params = {"obj1", "obj2"})
	public TestJSONObject test3(TestJSONObject obj1, TestJSONObject obj2)
	{
		return obj2;
	}

	@JSONMethod(JsFunctionName="test4", params = {"startIndex", "endIndex"})
	public List<String> test4(int startIndex, int endIndex)
	{
		ArrayList<String> myList = new ArrayList<String>();
		for(int i = startIndex; i < endIndex; i++)
		{
			myList.add("string"+i);
		}
		return myList;
	}

	@JSONMethod(JsFunctionName="test5", params = {"startIndex", "endIndex"})
	public List<KeyValuePair<String, Integer>> test5(int startIndex, int endIndex)
	{
		ArrayList<KeyValuePair<String, Integer>> myList = new ArrayList<KeyValuePair<String, Integer>>();
		for(int i = startIndex; i < endIndex; i++)
		{
			myList.add(new KeyValuePair<String, Integer>("string", i));
		}
		return myList;
	}

	@JSONMethod(JsFunctionName="test6", params = {"str1"})
	public void test6(String str1)
	{

	}

	@JSONMethod(JsFunctionName="test7", params = {"jsonValue"})
	public String test7(JSONValue jsonValue)
	{
		JSONMapperWrapper<String> wrapper = new JSONMapperWrapper<String>();

		String s = "";
		s = wrapper.toJava(jsonValue, s, new TypeRef<String>() {});
		return s;
	}

	@JSONMethod(JsFunctionName="test8", params = {"paramValue"})
	public boolean test8(String paramValue)
	{
		JSONParser parser = new JSONParser(paramValue);
		JSONValue value = parser.nextValue();

		if(value.getData() != null)
		{
			return true;
		}

		return false;
	}
}
