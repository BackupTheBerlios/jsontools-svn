package com.sdicons.json.mapper;

/*
    JSONTools - Java JSON Tools
    Copyright (C) 2006 S.D.I.-Consulting BVBA
    http://www.sdi-consulting.com
    mailto://nospam@sdi-consulting.com

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/

import com.sdicons.json.helper.HelperRepository;
import com.sdicons.json.mapper.helper.ComplexMapperHelper;
import com.sdicons.json.mapper.helper.SimpleMapperHelper;
import com.sdicons.json.mapper.helper.impl.*;
import com.sdicons.json.model.JSONNull;
import com.sdicons.json.model.JSONValue;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedList;

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
//        repo.addHelper(new ColorHelper());
//        repo.addHelper(new FontHelper());
//        repo.addHelper(new EnumHelper());
    }

    public static Object toJava(JSONValue aValue, Class aPojoClass)
    throws MapperException
    {
        // Null references are not allowed.
        if(aValue == null)
        {
            final String lMsg = "Mapper does not support null values.";
            throw new MapperException(lMsg);
        }
        // But null representations are.
        else if(aValue.isNull()) return null;

        // Use the class helpers for built in types.
        if(aPojoClass == Boolean.TYPE) aPojoClass = Boolean.class;
        else if(aPojoClass == Byte.TYPE) aPojoClass = Byte.class;
        else if(aPojoClass == Short.TYPE) aPojoClass = Short.class;
        else if(aPojoClass == Integer.TYPE) aPojoClass = Integer.class;
        else if(aPojoClass == Long.TYPE) aPojoClass = Long.class;
        else if(aPojoClass == Float.TYPE) aPojoClass = Float.class;
        else if(aPojoClass == Double.TYPE) aPojoClass = Double.class;
        else if(aPojoClass == Character.TYPE) aPojoClass = Character.class;

        // Find someone who can map it.
        final SimpleMapperHelper lHelperSimple = repo.findHelper(aPojoClass);

        if(lHelperSimple == null)
        {
            final String lMsg = "Could not find a mapper helper for class: " + aPojoClass.getName();
            throw new MapperException(lMsg);
        }
        else return lHelperSimple.toJava(aValue, aPojoClass);
    }

    public static Object toJava(JSONValue aValue, ParameterizedType aGenericType)
    throws MapperException
    {
        // Null references are not allowed.
        if(aValue == null)
        {
            final String lMsg = "Mapper does not support null values.";
            throw new MapperException(lMsg);
        }
        // But null representations are.
        else if(aValue.isNull()) return null;

        // First decompose the type in its raw class and the classes of the parameters.
        final Class lRawClass = (Class) aGenericType.getRawType();
        final Type[] lTypes = aGenericType.getActualTypeArguments();
        final Class[] lClasses = new Class[lTypes.length];
        for(int i = 0; i < lTypes.length; i++) lClasses[i] = (Class) lTypes[i];

        // Find someone who can map it.
        final SimpleMapperHelper lMapperHelper = repo.findHelper(lRawClass);

        if(lMapperHelper == null)
        {
            final String lMsg = "Could not find a mapper helper for parameterized type: " + aGenericType;
            throw new MapperException(lMsg);
        }
        else
        {
            if(lMapperHelper instanceof ComplexMapperHelper) return ((ComplexMapperHelper) lMapperHelper).toJava(aValue, lRawClass, lClasses);
            else return lMapperHelper.toJava(aValue, lRawClass);
        }
    }

    public static Object toJava(JSONValue aValue)
    throws MapperException
    {
        if(aValue.isArray()) return toJava(aValue, LinkedList.class);
        else if(aValue.isBoolean()) return toJava(aValue, Boolean.class);
        else if(aValue.isDecimal()) return toJava(aValue, BigDecimal.class);
        else if(aValue.isInteger()) return toJava(aValue, BigInteger.class);
        else if(aValue.isString()) return toJava(aValue, String.class);
        else return toJava(aValue, Object.class);
    }

    public static JSONValue toJSON(Object aPojo)
    throws MapperException
    {
        if(aPojo == null) return JSONNull.NULL;

        final SimpleMapperHelper lHelperSimple = repo.findHelper(aPojo.getClass());

        if(lHelperSimple == null)
        {
            final String lMsg = "Could not find a mapper helper for class: " + aPojo.getClass().getName();
            throw new MapperException(lMsg);
        }

        return lHelperSimple.toJSON(aPojo);
    }
}
