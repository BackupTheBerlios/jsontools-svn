package com.sdicons.json.mapper.helper.impl;

import com.sdicons.json.mapper.helper.SimpleMapperHelper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.model.JSONInteger;
import com.sdicons.json.model.JSONString;

import java.math.BigInteger;

public class LongMapper
implements SimpleMapperHelper
{
    public Class getHelpedClass()
    {
        return Long.class;
    }

    public Object toJava(JSONValue aValue, Class aRequestedClass) throws MapperException
    {
        if(aValue.isString())
        {
            try
            {
                return Long.parseLong(((JSONString)aValue).getValue());
            }
            catch (NumberFormatException e)
            {
                throw new MapperException("LongMapper cannot map value: " + ((JSONString)aValue).getValue());
            }
        }
        else if(aValue.isInteger()) return ((JSONInteger) aValue).getValue().longValue();
        else throw new MapperException("LongMapper cannot map: " + aValue.getClass().getName());
    }

    public JSONValue toJSON(Object aPojo)
    throws MapperException
    {
        if(!Long.class.isAssignableFrom(aPojo.getClass())) throw new MapperException("LongMapper cannot map: " + aPojo.getClass().getName());
        return new JSONInteger(new BigInteger(aPojo.toString()));
    }
}
