package com.sdicons.json.mapper.helper.impl;

import com.sdicons.json.mapper.helper.SimpleMapperHelper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.model.JSONInteger;
import com.sdicons.json.model.JSONString;

import java.math.BigInteger;

public class ShortMapper
implements SimpleMapperHelper
{
    public Class getHelpedClass()
    {
        return Short.class;
    }

    public Object toJava(JSONValue aValue, Class aRequestedClass) throws MapperException
    {
        if(aValue.isString())
        {
            try
            {
                return Short.parseShort(((JSONString)aValue).getValue());
            }
            catch (NumberFormatException e)
            {
                throw new MapperException("ShortMapper cannot map value: " + ((JSONString)aValue).getValue());
            }
        }
        else if(aValue.isInteger()) return (short) ((JSONInteger) aValue).getValue().intValue();
        else throw new MapperException("ShortMapper cannot map: " + aValue.getClass().getName());        
    }

    public JSONValue toJSON(Object aPojo)
    throws MapperException
    {
        if(!Short.class.isAssignableFrom(aPojo.getClass())) throw new MapperException("ShortMapper cannot map: " + aPojo.getClass().getName());
        return new JSONInteger(new BigInteger(aPojo.toString()));
    }
}
