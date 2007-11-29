/**
 *
 */
package com.sdicons.json.annotations;

import com.sdicons.json.annotations.JSONExclude;
import com.sdicons.json.annotations.JSONInclude;

/**
 * @author itaylor
 */
public class TestAnnotationObj
{
	private String value1;

	private String value2;

	private String value3;

	private String value4;

	@SuppressWarnings("unused")
	private String value5;

	/**
	 *
	 */
	public TestAnnotationObj()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param value1
	 * @param value2
	 * @param value3
	 * @param value4
	 */
	public TestAnnotationObj(String value1, String value2, String value3, String value4)
	{
		super();
		this.value1 = value1;
		this.value2 = value2;
		this.value3 = value3;
		this.value4 = value4;
	}

	/**
	 * Tests include when there is no setter... the value should be serialized
	 * but not deserialized.
	 */
	@JSONInclude
	public String getValue1()
	{
		return this.value1;
	}

	/**
	 * Tests exclude set on getter... which should block out both getting and
	 * setting on this property
	 */
	@JSONExclude
	public String getValue2()
	{
		return this.value2;
	}

	/**
	 * @param value2
	 *            the value2 to set
	 */
	public void setValue2(String value2)
	{
		this.value2 = value2;
	}

	/**
	 * @return the value3
	 */
	public String getValue3()
	{
		return this.value3;
	}

	/**
	 * Tests case where exclude set on setter but not getter, making value 3
	 * serialized to json but not hydrated to java.
	 */
	@JSONExclude
	public void setValue3(String value3)
	{
		this.value3 = value3;
	}

	/**
	 * @return the value4
	 */
	public String getValue4()
	{
		return this.value4;
	}

	/**
	 * @param value4
	 *            the value4 to set
	 */
	public void setValue4(String value4)
	{
		this.value4 = value4;
	}

	/**
	 * A setter with no getter and JSONInclude set
	 *
	 * @param value5
	 */
	@JSONInclude
	public void setValue5(String value5)
	{
		this.value5 = value5;
	}

	public String getTestValue5()
	{
		return this.value5;
	}
}
