package com.sdicons.json.mapper;

/*
 * JSONTools - Java JSON Tools Copyright (C) 2006 S.D.I.-Consulting BVBA
 * http://www.sdi-consulting.com mailto://nospam@sdi-consulting.com This library
 * is free software; you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software
 * Foundation; either version 2.1 of the License, or (at your option) any later
 * version. This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details. You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedList;

import com.sdicons.json.helper.HelperRepository;
import com.sdicons.json.mapper.helper.ComplexMapperHelper;
import com.sdicons.json.mapper.helper.SimpleMapperHelper;
import com.sdicons.json.mapper.helper.impl.ArrayMapper;
import com.sdicons.json.mapper.helper.impl.BigDecimalMapper;
import com.sdicons.json.mapper.helper.impl.BigIntegerMapper;
import com.sdicons.json.mapper.helper.impl.BooleanMapper;
import com.sdicons.json.mapper.helper.impl.ByteMapper;
import com.sdicons.json.mapper.helper.impl.CharacterMapper;
import com.sdicons.json.mapper.helper.impl.CollectionMapper;
import com.sdicons.json.mapper.helper.impl.DateMapper;
import com.sdicons.json.mapper.helper.impl.DoubleMapper;
import com.sdicons.json.mapper.helper.impl.EnumMapper;
import com.sdicons.json.mapper.helper.impl.FloatMapper;
import com.sdicons.json.mapper.helper.impl.IntegerMapper;
import com.sdicons.json.mapper.helper.impl.JSONLiteralMapper;
import com.sdicons.json.mapper.helper.impl.JSONValueMapper;
import com.sdicons.json.mapper.helper.impl.LongMapper;
import com.sdicons.json.mapper.helper.impl.MapMapper;
import com.sdicons.json.mapper.helper.impl.ObjectMapper;
import com.sdicons.json.mapper.helper.impl.ShortMapper;
import com.sdicons.json.mapper.helper.impl.StringMapper;
import com.sdicons.json.model.JSONNull;
import com.sdicons.json.model.JSONValue;

/**
 * The mapper class is able to convert a JSON representation to/from a Java
 * representation. The mapper's goal is to produce a nice and clean JSON output
 * which can easily be used in e.g. Javascript context. As a consequence, not
 * all Java constructs are preserved during the conversion to/from JSON. The
 * mapper is the best choice in an application where the clean JSON format is
 * central. If the emphasis is on exact Java serialization where types are
 * preserved, take a look at the Serializer tool. The main difference between
 * the serializer and the mapper is that the serializer keeps as much type
 * information and structure information in the JSON data where the mapper uses
 * the type information in the provided Java classes to interprete the JSON
 * data.
 */
public class JSONMapper
{
	private static HelperRepository<SimpleMapperHelper> repo = new HelperRepository<SimpleMapperHelper>();

	static
	{
		repo.addHelper(new ObjectMapper());
		repo.addHelper(new StringMapper());
		repo.addHelper(new BooleanMapper());
		repo.addHelper(new ByteMapper());
		repo.addHelper(new ShortMapper());

		repo.addHelper(new IntegerMapper());

		repo.addHelper(new LongMapper());
		repo.addHelper(new FloatMapper());
		repo.addHelper(new DoubleMapper());
		repo.addHelper(new BigIntegerMapper());
		repo.addHelper(new BigDecimalMapper());
		repo.addHelper(new CharacterMapper());
		repo.addHelper(new DateMapper());
		repo.addHelper(new CollectionMapper());
		repo.addHelper(new MapMapper());
		repo.addHelper(new EnumMapper());
		repo.addHelper(new JSONLiteralMapper());
		repo.addHelper(new JSONValueMapper());
// repo.addHelper(new ColorHelper());
// repo.addHelper(new FontHelper());
// repo.addHelper(new EnumHelper());
	}

	public static Object toJava(JSONValue aValue, Class aPojoClass)
	{
		return toJava(aValue, null, aPojoClass);
	}

	public static Object toJava(JSONValue aValue, Object aPojo, Class aPojoClass)
	{

		// Null references are not allowed.
		if (aValue == null)
		{
			final String lMsg = "Mapper does not support null values.";
			throw new MapperException(lMsg);
		}
		// But null representations are.
		else if (aValue.isNull())
		{
			return null;
		}
		if (aPojoClass.isArray())
		{
			ArrayMapper arrayMapper = new ArrayMapper();
			return arrayMapper.toJava(aValue, aPojoClass, aPojo);
		}
		// Use the class helpers for built in types.
		if (aPojoClass == Boolean.TYPE)
		{
			aPojoClass = Boolean.class;
		} else if (aPojoClass == Byte.TYPE)
		{
			aPojoClass = Byte.class;
		} else if (aPojoClass == Short.TYPE)
		{
			aPojoClass = Short.class;
		} else if (aPojoClass == Integer.TYPE)
		{
			aPojoClass = Integer.class;
		} else if (aPojoClass == Long.TYPE)
		{
			aPojoClass = Long.class;
		} else if (aPojoClass == Float.TYPE)
		{
			aPojoClass = Float.class;
		} else if (aPojoClass == Double.TYPE)
		{
			aPojoClass = Double.class;
		} else if (aPojoClass == Character.TYPE)
		{
			aPojoClass = Character.class;
		}

		// Find someone who can map it.
		final SimpleMapperHelper lHelperSimple = repo.findHelper(aPojoClass);

		if (lHelperSimple == null)
		{
			final String lMsg = "Could not find a mapper helper for class: " + aPojoClass.getName();
			throw new MapperException(lMsg);
		} else
		{
			return lHelperSimple.toJava(aValue, aPojoClass, aPojo);
		}
	}

	/**
	 * Map a JSON representation to a Java object.
	 *
	 * @param aValue
	 *            The JSON value that has to be mapped.
	 * @param aPojoClass
	 *            The class to which the JSON object should be mapped.
	 * @return The resulting Java object, the POJO representation.
	 * @throws MapperException
	 *             when an error occurs during mapping.
	 */
	public static Object toJava(JSONValue aValue, Object aPojo)
	{
		return toJava(aValue, aPojo, aPojo.getClass());
	}

	/**
	 * Map a JSON representation to a Java object.
	 *
	 * @param aValue
	 *            The JSON value that has to be mapped.
	 * @param aGenericType
	 *            A type indication to help the mapper map the JSON text.
	 * @return The resulting Java POJO.
	 * @throws MapperException
	 *             When the JSON text cannot be mapped to POJO.
	 */
	public static Object toJava(JSONValue aValue, Object aPojo, ParameterizedType aGenericType)
	{
		// Null references are not allowed.
		if (aValue == null)
		{
			final String lMsg = "Mapper does not support null values.";
			throw new MapperException(lMsg);
		}
		// But null representations are.
		else if (aValue.isNull())
		{
			return null;
		}

		// First decompose the type in its raw class and the classes of the
		// parameters.
		final Class lRawClass = (Class) aGenericType.getRawType();
		final Type[] lTypes = aGenericType.getActualTypeArguments();

		// Find someone who can map it.
		final SimpleMapperHelper lMapperHelper = repo.findHelper(lRawClass);

		if (lMapperHelper == null)
		{
			final String lMsg = "Could not find a mapper helper for parameterized type: " +
			        aGenericType;
			throw new MapperException(lMsg);
		} else
		{
			if (lMapperHelper instanceof ComplexMapperHelper)
			{
				return ((ComplexMapperHelper) lMapperHelper).toJava(aValue,
				                                                    lRawClass,
				                                                    aPojo,
				                                                    lTypes);
			} else
			{
				return lMapperHelper.toJava(aValue, lRawClass, aPojo);
			}
		}
	}

	/**
	 * Map a JSON representation to a Java object. Since no class nor type hint
	 * is passed to the mapper, this method can only handle the most basic
	 * mappings.
	 *
	 * @param aValue
	 *            The JSON value that has to be mapped.
	 * @return he resulting Java POJO.
	 * @throws MapperException
	 *             When the JSON text cannot be mapped to POJO.
	 */
	public static Object toJava(JSONValue aValue) throws MapperException
	{
		if (aValue.isArray())
		{
			return toJava(aValue, LinkedList.class);
		} else if (aValue.isBoolean())
		{
			return toJava(aValue, Boolean.class);
		} else if (aValue.isDecimal())
		{
			return toJava(aValue, BigDecimal.class);
		} else if (aValue.isInteger())
		{
			return toJava(aValue, BigInteger.class);
		} else if (aValue.isString())
		{
			return toJava(aValue, String.class);
		} else
		{
			return toJava(aValue, Object.class);
		}
	}

	/**
	 * Map a POJO to the JSON representation.
	 *
	 * @param aPojo
	 *            to be mapped to JSON.
	 * @return The JSON representation.
	 * @throws MapperException
	 *             If something goes wrong during mapping.
	 */
	public static JSONValue toJSON(Object aPojo)
	{
		if (aPojo == null)
		{
			return JSONNull.NULL;
		}
		final Class lObjectClass = aPojo.getClass();
		if (lObjectClass.isArray())
		{
			final ArrayMapper arrayMapper = new ArrayMapper();
			return arrayMapper.toJSON(aPojo);
		}

		final SimpleMapperHelper lHelperSimple = repo.findHelper(aPojo.getClass());

		if (lHelperSimple == null)
		{
			final String lMsg = "Could not find a mapper helper for class: " +
			        aPojo.getClass().getName();
			throw new MapperException(lMsg);
		}
		return lHelperSimple.toJSON(aPojo);
	}
}
