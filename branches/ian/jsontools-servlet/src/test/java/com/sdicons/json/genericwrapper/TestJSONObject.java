package com.sdicons.json.genericwrapper;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class TestJSONObject implements Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String testString;

	private int testInt;

	private TestJSONSubObject testSubObject;

	private List<String> testStringList;

	private Map<String, TestJSONSubObject> testMap;

	private List<KeyValuePair<String, Integer>> testDoubleNestedGeneric;

	public TestJSONObject()
	{

	}

	/**
	 * @param testString
	 * @param testInt
	 * @param testSubObject
	 * @param testStringList
	 * @param testMap
	 * @param testDoubleNestedGeneric
	 */
	public TestJSONObject(String testString, int testInt, TestJSONSubObject testSubObject,
	        List<String> testStringList, Map<String, TestJSONSubObject> testMap,
	        List<KeyValuePair<String, Integer>> testDoubleNestedGeneric)
	{
		super();
		this.testString = testString;
		this.testInt = testInt;
		this.testSubObject = testSubObject;
		this.testStringList = testStringList;
		this.testMap = testMap;
		this.testDoubleNestedGeneric = testDoubleNestedGeneric;
	}

	/**
	 * @return the testString
	 */
	public String getTestString()
	{
		return this.testString;
	}

	/**
	 * @param testString
	 *            the testString to set
	 */
	public void setTestString(String testString)
	{
		this.testString = testString;
	}

	/**
	 * @return the testInt
	 */
	public int getTestInt()
	{
		return this.testInt;
	}

	/**
	 * @param testInt
	 *            the testInt to set
	 */
	public void setTestInt(int testInt)
	{
		this.testInt = testInt;
	}

	/**
	 * @return the testSubObject
	 */
	public TestJSONSubObject getTestSubObject()
	{
		return this.testSubObject;
	}

	/**
	 * @param testSubObject
	 *            the testSubObject to set
	 */
	public void setTestSubObject(TestJSONSubObject testSubObject)
	{
		this.testSubObject = testSubObject;
	}

	/**
	 * @return the testStringList
	 */
	public List<String> getTestStringList()
	{
		return this.testStringList;
	}

	/**
	 * @param testStringList
	 *            the testStringList to set
	 */
	public void setTestStringList(List<String> testStringList)
	{
		this.testStringList = testStringList;
	}

	/**
	 * @return the testMap
	 */
	public Map<String, TestJSONSubObject> getTestMap()
	{
		return this.testMap;
	}

	/**
	 * @param testMap
	 *            the testMap to set
	 */
	public void setTestMap(Map<String, TestJSONSubObject> testMap)
	{
		this.testMap = testMap;
	}

	/**
	 * @return the testDoubleNestedGeneric
	 */
	public List<KeyValuePair<String, Integer>> getTestDoubleNestedGeneric()
	{
		return this.testDoubleNestedGeneric;
	}

	/**
	 * @param testDoubleNestedGeneric
	 *            the testDoubleNestedGeneric to set
	 */
	public void setTestDoubleNestedGeneric(
	        List<KeyValuePair<String, Integer>> testDoubleNestedGeneric)
	{
		this.testDoubleNestedGeneric = testDoubleNestedGeneric;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof TestJSONObject)
		{
			TestJSONObject obj = (TestJSONObject) other;
			boolean simpleMembersOk = false;
			if ((this.testString == obj.getTestString()) && (this.testInt == obj.getTestInt()) &&
			        this.testSubObject.equals(obj.testSubObject))
			{
				simpleMembersOk = true;

			}
			boolean listsOk = this.SimpleListEqualsTest(this.testDoubleNestedGeneric, obj
			        .getTestDoubleNestedGeneric()) &&
			        this.SimpleListEqualsTest(this.testStringList, obj.getTestStringList());
			boolean mapOk = this.SimpleMapEqualsTest(this.testMap, obj.getTestMap());

			if (simpleMembersOk && listsOk && mapOk)
			{
				return true;
			}
		}
		return false;
	}

	private boolean SimpleMapEqualsTest(Map<? extends Object, ? extends Object> map1,
	        Map<? extends Object, ? extends Object> map2)
	{
		if (((map1 == null) && (map2 != null)) || ((map1 != null) && (map2 == null)))
		{
			return false;
		}
		if ((map1 == null) && (map2 == null))
		{
			return true;
		}
		if (map1.size() != map2.size())
		{
			return false;
		}
		try
		{
			for (Object key : map1.keySet())
			{
				if (!map1.get(key).equals(map2.get(key)))
				{
					return false;
				}
			}
		} catch (Exception ex)
		{
			return false;
		}
		return true;
	}

	private boolean SimpleListEqualsTest(List<? extends Object> list1, List<? extends Object> list2)
	{
		if (((list1 == null) && (list2 != null)) || ((list1 != null) && (list2 == null)))
		{
			return false;
		}
		if ((list1 == null) && (list2 == null))
		{
			return true;
		}
		if (list1.size() != list2.size())
		{
			return false;
		}
		for (int i = 0; i < list1.size(); i++)
		{
			if (!list1.get(i).equals(list2.get(i)))
			{
				return false;
			}
		}
		return true;
	}
}
