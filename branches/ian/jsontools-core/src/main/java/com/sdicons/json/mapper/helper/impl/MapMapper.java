package com.sdicons.json.mapper.helper.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.ComplexMapperHelper;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;

public class MapMapper implements ComplexMapperHelper
{
	public Class getHelpedClass()
	{
		return Map.class;
	}

	public Object toJava(JSONValue aValue, Class aRequestedClass, Object aPojo)
	        throws MapperException
	{
		return this.toJava(aValue, aRequestedClass, aPojo, new Type[0]);
	}

	public Object toJava(JSONValue aValue, Class aRawClass, Object aPojo, Type[] aTypes)
	        throws MapperException
	{
		if (!aValue.isObject())
		{
			throw new MapperException("MapMapper cannot map: " + aValue.getClass().getName());
		}
		if (!Map.class.isAssignableFrom(aRawClass))
		{
			throw new MapperException("MapMapper cannot map: " + aValue.getClass().getName());
		}
		JSONObject aObject = (JSONObject) aValue;

		Map lMapObj;

		try
		{
			if (aPojo != null)
			{
				// The pojo exists, just clear it and get on with it.
				lMapObj = (Map) aPojo;
				lMapObj.clear();
			} else
			{
				// First we try to instantiate the correct
				// collection class.
				lMapObj = (Map) aRawClass.newInstance();
			}
		} catch (Exception e)
		{
			// If the requested class cannot create an instance because
			// it is abstract, or an interface, we use a default fallback class.
			// This solution is far from perfect, but we try to make the mapper
			// as convenient as possible.
			lMapObj = new LinkedHashMap();
		}

		if (aTypes.length == 0)
		{
			// Simple, raw collection.
			for (String lKey : aObject.getValue().keySet())
			{
				JSONValue lVal = aObject.getValue().get(lKey);
				lMapObj.put(lKey, JSONMapper.toJava(lVal));
			}
		} else if (aTypes.length == 2)
		{

			for (String lKey : aObject.getValue().keySet())
			{
				Object key;
				if (aTypes[0] instanceof Class)
				{
					key = JSONMapper.toJava(new JSONString(lKey), null, (Class) aTypes[0]);
				} else
                {
	               throw new MapperException("MapMapper: Map keys with generic types are not supported");
                }

				JSONValue lVal = aObject.getValue().get(lKey);
				if (aTypes[1] instanceof Class)
				{
					lMapObj.put(key, JSONMapper.toJava(lVal, null, (Class) aTypes[1]));
				} else
				{
					lMapObj.put(key, JSONMapper.toJava(lVal, null,
					        (ParameterizedType) aTypes[1]));
				}
			}
		} else
		{
			// Not possible, a Map cannot have more than two types for
			// its contents.
			throw new MapperException("MapMapper cannot map: " + aValue.getClass().getName());
		}

		return lMapObj;
	}

	public JSONValue toJSON(Object aPojo) throws MapperException
	{
		final JSONObject lObj = new JSONObject();
		if (!Map.class.isAssignableFrom(aPojo.getClass()))
		{
			throw new MapperException("MapMapper cannot map: " + aPojo.getClass().getName());
		}

		Map lMap = (Map) aPojo;
		for (Object lKey : lMap.keySet())
		{
			lObj.getValue().put(lKey.toString(), JSONMapper.toJSON(lMap.get(lKey)));
		}
		return lObj;
	}
}
