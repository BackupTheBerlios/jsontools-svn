/**
 *
 */
package com.sdicons.json.mapper;

import java.lang.reflect.ParameterizedType;

import com.sdicons.json.helper.TypeRef;
import com.sdicons.json.model.JSONValue;

/**
 * @author itaylor
 * A "Type safe" wrapper around JSONMapper.  It also allows for top level nested generics
 * by using a super type token (TypeRef) pattern.
 *
 *For an example of how to use the TypeRef pattern please see the JSONTools unit tests.
 */
@SuppressWarnings("unchecked")
public class JSONMapperWrapper<T>
{


	/**
	 * A toplevel generic supporting JSONMapper...
	 * aPojo can be a generic or nested generic class eg List<KeyValuePair<String, String>>
	 * To make that work, you would need to pass in the type param as new TypeRef<List<KeyValuePair<String,String>>>(){}
	 * @param aValue
	 * @param aPojo
	 * @param type The wierd typeref object...
	 * @return
	 */
	public T toJava(JSONValue aValue, T aPojo, TypeRef<?> type )
	{
		if ((type != null) && (type.getType() instanceof ParameterizedType))
		{
			return (T) JSONMapper.toJava(aValue, aPojo, (ParameterizedType) type.getType());
		}
		return (T) JSONMapper.toJava(aValue, aPojo, aPojo.getClass());

	}
	/***
	 * A JSONMapper that supports nested generics, but not toplevel...
	 *
	 * Eg. aPojo can have a List<KeyValuePair<String,String>> as one of its property
	 * like: aPojo.keyValuePairList but aPojo cannot itself be a Generic type
	 * @param aValue
	 * @param aPojo
	 * @return
	 */
    public T toJava(JSONValue aValue, T aPojo)
	{
		return (T) JSONMapper.toJava(aValue, aPojo, aPojo.getClass());
	}

	public JSONValue toJSON(T aPojo)
	{
		return JSONMapper.toJSON(aPojo);
	}
//	}
//		ParameterizedType superclass = (ParameterizedType)
//	        getClass().getGenericSuperclass();
//	    type = superclass.getActualTypeArguments()[0];
//		ParameterizedType parameterizedType = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments().getT().getTypeParameters()[0];
//		Class myClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
//
////		if (types[0] instanceof ParameterizedType)
////		{
////			Type[] childTypes = ((ParameterizedType)types[0]).getActualTypeArguments();
////			if (childTypes.length == 1)
////			{
////				if  (childTypes[0] instanceof ParameterizedType)
////				{
////					return (T) JSONMapper.toJava(aValue, aPojo, (ParameterizedType) childTypes[0]);
////				}
////				else
////				{
////					return (T) JSONMapper.toJava(aValue, aPojo, (Class) childTypes[0]);
////				}
////			}
////		}
//		return (T) JSONMapper.toJava(aValue, aPojo);



}
