package com.sdicons.json.genericwrapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.sdicons.json.genericwrapper.KeyValuePair;
import com.sdicons.json.helper.TypeRef;
import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.JSONMapperWrapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.parser.JSONParser;

public class GenericWrapperTest
{

	/**
	 * @author itaylor
	 * This tests the JSONMapperWrapper which provides an extra layer of Generics support via Super Type tokens (the typeRef object)
	 */

		@Test
		public void SerializeObject()
		{
			TestJSONObject tjo = this.getTestJSONObject(1);
			JSONValue val = JSONMapper.toJSON(tjo);

			TestJSONObject tjo2 = new TestJSONObject();
			JSONMapperWrapper<TestJSONObject> mapper = new JSONMapperWrapper<TestJSONObject>();
			tjo = mapper.toJava(val, tjo2);
			Assert.assertTrue(tjo2.equals(tjo));
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

