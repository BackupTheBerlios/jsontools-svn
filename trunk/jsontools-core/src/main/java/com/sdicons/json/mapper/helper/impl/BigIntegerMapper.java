package com.sdicons.json.mapper.helper.impl;

import com.sdicons.json.mapper.helper.MapperHelper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONInteger;

import java.math.BigInteger;

public class BigIntegerMapper
implements MapperHelper
{
    public Class getHelpedClass()
    {
        return BigInteger.class;
    }

    public Object toJava(JSONValue aValue, Class aRequestedClass) throws MapperException
    {
        if(aValue.isString())
        {
            try
            {
                return new BigInteger(((JSONString)aValue).getValue());
            }
            catch (NumberFormatException e)
            {
                throw new MapperException("BigIntegerMapper cannot map value: " + ((JSONString)aValue).getValue());
            }
        }
        else if(aValue.isInteger()) return ((JSONInteger) aValue).getValue();
        else throw new MapperException("BigIntegerMapper cannot map: " + aValue.getClass().getName());
    }

    public JSONValue toJSON(Object aPojo)
    throws MapperException
    {
        if(!BigInteger.class.isAssignableFrom(aPojo.getClass())) throw new MapperException("BigIntegerMapper cannot map: " + aPojo.getClass().getName());
        return new JSONInteger(new BigInteger(aPojo.toString()));
    }
}
