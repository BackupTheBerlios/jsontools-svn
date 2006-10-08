package com.sdicons.json.mapper;

import com.sdicons.json.helper.HelperRepository;
import com.sdicons.json.mapper.helper.MapperHelper;
import com.sdicons.json.mapper.helper.impl.*;
import com.sdicons.json.model.JSONNull;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.serializer.helper.impl.CollectionHelper;

import java.util.LinkedList;
import java.math.BigDecimal;
import java.math.BigInteger;

public class JSONMapper
{
    private static HelperRepository<MapperHelper> repo = new HelperRepository<MapperHelper>();

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
//        repo.addHelper(new MapHelper());
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
        final MapperHelper lHelper = repo.findHelper(aPojoClass);

        if(lHelper == null)
        {
            final String lMsg = "Could not find a mapper helper for class: " + aPojoClass.getName();
            throw new MapperException(lMsg);
        }
        else return lHelper.toJava(aValue, aPojoClass);
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

        final MapperHelper lHelper = repo.findHelper(aPojo.getClass());

        if(lHelper == null)
        {
            final String lMsg = "Could not find a mapper helper for class: " + aPojo.getClass().getName();
            throw new MapperException(lMsg);
        }

        return lHelper.toJSON(aPojo);
    }
}
