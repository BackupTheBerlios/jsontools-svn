package com.sdicons.json.mapper.helper.impl;

import com.sdicons.json.mapper.helper.SimpleMapperHelper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.model.JSONInteger;
import com.sdicons.json.model.JSONString;

import java.math.BigInteger;

public class ByteMapper
implements SimpleMapperHelper
{
    public Class getHelpedClass()
    {
        return Byte.class;
    }

    public Object toJava(JSONValue aValue, Class aRequestedClass) throws MapperException
    {
        if(aValue.isString())
        {
            try
            {
                return Byte.parseByte(((JSONString)aValue).getValue());
            }
            catch (NumberFormatException e)
            {
                throw new MapperException("ByteMapper cannot map value: " + ((JSONString)aValue).getValue());
            }
        }
        else if(aValue.isInteger()) return (byte) ((JSONInteger) aValue).getValue().intValue();
        else throw new MapperException("ByteMapper cannot map: " + aValue.getClass().getName());        
    }

    public JSONValue toJSON(Object aPojo)
    throws MapperException
    {
        if(!Byte.class.isAssignableFrom(aPojo.getClass())) throw new MapperException("ByteMapper cannot map: " + aPojo.getClass().getName());
        return new JSONInteger(new BigInteger(aPojo.toString()));
    }
}
