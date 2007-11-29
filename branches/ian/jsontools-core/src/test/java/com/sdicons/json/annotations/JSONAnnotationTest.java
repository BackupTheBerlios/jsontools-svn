/**
 *
 */
package com.sdicons.json.annotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import antlr.RecognitionException;
import antlr.TokenStreamException;

import com.sdicons.json.genericwrapper.KeyValuePair;
import com.sdicons.json.helper.TypeRef;
import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.JSONMapperWrapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.parser.JSONParser;


/**
 * @author itaylor
 */

public class JSONAnnotationTest
{

	@Test
	public void JsonIncludeWorksWithOnlyGetter()
	{
		TestAnnotationObj anno = this.getTestAnnotationObj();
		JSONValue val = JSONMapper.toJSON(anno);

		Assert.assertTrue(val.render(false).indexOf("value1") != -1);

		TestAnnotationObj anno2 = new TestAnnotationObj();
		JSONMapperWrapper<TestAnnotationObj> mapper = new JSONMapperWrapper<TestAnnotationObj>();
		anno2 = mapper.toJava(val, anno2);

		Assert.assertNull(anno2.getValue1());
		Assert.assertEquals("value4", anno2.getValue4()); // baseline
	}

	@Test
	public void JsonIncludeWorksWithOnlyGetterAndEnforcesNonSetable() throws TokenStreamException,
	        RecognitionException
	{
		TestAnnotationObj anno = this.getTestAnnotationObj();
		JSONValue val = JSONMapper.toJSON(anno);

		Assert.assertTrue(val.render(false).indexOf("value1") != -1);

		String s = "{ \"value1\":\"value1\", \"value2\":\"value2\", \"value3\":\"value3\", \"value4\":\"value4\" }";

		JSONParser jp = new JSONParser(s);
		JSONValue val2 = jp.nextValue();

		TestAnnotationObj anno2 = new TestAnnotationObj();
		JSONMapperWrapper<TestAnnotationObj> mapper = new JSONMapperWrapper<TestAnnotationObj>();
		anno2 = mapper.toJava(val2, anno2);

		Assert.assertNull(anno2.getValue1());
		Assert.assertEquals("value4", anno2.getValue4()); // baseline
	}

	@Test
	public void JsonIncludeOnSetterWithNoGetterAllowsSetting() throws TokenStreamException,
	        RecognitionException
	{
		TestAnnotationObj anno = this.getTestAnnotationObj();
		JSONValue val = JSONMapper.toJSON(anno);

		Assert.assertTrue(val.render(false).indexOf("value1") != -1);

		String s = "{ \"value1\":\"value1\", \"value2\":\"value2\", \"value3\":\"value3\", \"value4\":\"value4\", \"value5\":\"value5\" }";

		JSONParser jp = new JSONParser(s);
		JSONValue val2 = jp.nextValue();

		TestAnnotationObj anno2 = new TestAnnotationObj();
		JSONMapperWrapper<TestAnnotationObj> mapper = new JSONMapperWrapper<TestAnnotationObj>();
		anno2 = mapper.toJava(val2, anno2);

		Assert.assertEquals("value5", anno2.getTestValue5());
		Assert.assertEquals("value4", anno2.getValue4()); // baseline
	}

	@Test
	public void JsonExcludeWorksWhenAppliedToGetter()
	{
		TestAnnotationObj anno = this.getTestAnnotationObj();
		JSONValue val = JSONMapper.toJSON(anno);

		Assert.assertTrue(val.render(false).indexOf("value2") == -1);

		TestAnnotationObj anno2 = new TestAnnotationObj();
		JSONMapperWrapper<TestAnnotationObj> mapper = new JSONMapperWrapper<TestAnnotationObj>();
		anno2 = mapper.toJava(val, anno2);

		Assert.assertNull(anno2.getValue2());
	}

	@Test
	public void JsonExcludeEnforcesNonSetable() throws TokenStreamException, RecognitionException
	{
		TestAnnotationObj anno = this.getTestAnnotationObj();
		JSONValue val = JSONMapper.toJSON(anno);

		Assert.assertTrue(val.render(false).indexOf("value2") == -1);

		String s = "{ \"value1\":\"value1\", \"value2\":\"value2\", \"value3\":\"value3\", \"value4\":\"value4\" }";

		JSONParser jp = new JSONParser(s);
		JSONValue val2 = jp.nextValue();

		TestAnnotationObj anno2 = new TestAnnotationObj();
		JSONMapperWrapper<TestAnnotationObj> mapper = new JSONMapperWrapper<TestAnnotationObj>();
		anno2 = mapper.toJava(val2, anno2);

		Assert.assertNull(anno2.getValue2());
		Assert.assertEquals("value4", anno2.getValue4()); // baseline
	}

	@Test(expectedExceptions = MapperException.class)
	public void ExceptionThrownWhenTrySettingOnNonExistantField() throws TokenStreamException,
	        RecognitionException
	{
		String s = "{ \"value1\":\"value1\", \"jerks\":\"value2\", \"value3\":\"value3\", \"value4\":\"value4\" }";

		JSONParser jp = new JSONParser(s);
		JSONValue val2 = jp.nextValue();

		TestAnnotationObj anno2 = new TestAnnotationObj();
		JSONMapperWrapper<TestAnnotationObj> mapper = new JSONMapperWrapper<TestAnnotationObj>();
		anno2 = mapper.toJava(val2, anno2);
	}

	@Test
	public void JsonExcludeOnSetterEnforcesNonSetable() throws TokenStreamException,
	        RecognitionException
	{
		TestAnnotationObj anno = this.getTestAnnotationObj();
		JSONValue val = JSONMapper.toJSON(anno);

		Assert.assertTrue(val.render(false).indexOf("value3") != -1);

		String s = "{ \"value1\":\"value1\", \"value2\":\"value2\", \"value3\":\"value3\", \"value4\":\"value4\" }";

		JSONParser jp = new JSONParser(s);
		JSONValue val2 = jp.nextValue();

		TestAnnotationObj anno2 = new TestAnnotationObj();
		JSONMapperWrapper<TestAnnotationObj> mapper = new JSONMapperWrapper<TestAnnotationObj>();
		anno2 = mapper.toJava(val2, anno2);

		Assert.assertNull(anno2.getValue3());
		Assert.assertEquals("value4", anno2.getValue4()); // baseline
	}
	@Test
	public void TestDoubleGenericList()
	{
		List<KeyValuePair<String, Integer>> testObj = this.getTestDoubleNestedGeneric(5);
		JSONValue val = JSONMapper.toJSON(testObj);

		JSONParser jp = new JSONParser(val.render(true));
		JSONValue val2 = jp.nextValue();

		JSONMapperWrapper<List<KeyValuePair<String, Integer>>> mapper = new JSONMapperWrapper<List<KeyValuePair<String,Integer>>>();
		List<KeyValuePair<String, Integer>> testObj2 =  mapper.toJava(val2, testObj, new TypeRef<List<KeyValuePair<String, Integer>>>(){});

		Assert.assertEquals(testObj2.size(), 2);
		Assert.assertEquals(testObj2.get(0).getKey(), "test5" );
		Assert.assertEquals(testObj2.get(0).getValue(), Integer.valueOf(5));
	}

	private List<KeyValuePair<String, Integer>> getTestDoubleNestedGeneric(int seed)
	{
		List<KeyValuePair<String, Integer>> list = new ArrayList<KeyValuePair<String, Integer>>();
		list.add(new KeyValuePair<String, Integer>("test" + seed, seed));
		list.add(new KeyValuePair<String, Integer>("test" + seed + 1, seed + 1));
		return list;
	}

	private TestAnnotationObj getTestAnnotationObj()
	{
		TestAnnotationObj anno = new TestAnnotationObj("value1", "value2", "value3", "value4");
		return anno;
	}

}
