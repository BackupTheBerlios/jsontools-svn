package com.sdicons.json.genericwrapper;

import java.io.Serializable;
import java.util.List;

public class TestJSONSubObject implements Serializable
{

	private static final long serialVersionUID = 1L;

	private String testString;

	private int testInt;

	private List<String> testStringList;

	public TestJSONSubObject()
	{
	};

	public TestJSONSubObject(String _testString, int _testInt, List<String> _testStringList)
	{
		this.testString = _testString;
		this.testInt = _testInt;
		this.testStringList = _testStringList;
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

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof TestJSONSubObject)
		{
			TestJSONSubObject obj = (TestJSONSubObject) other;

			if ((this.testString == obj.getTestString()) && (this.testInt == obj.getTestInt()) &&
			        this.SimpleListEqualsTest(this.testStringList, obj.getTestStringList()))
			{
				return true;

			}
		}
		return false;
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
