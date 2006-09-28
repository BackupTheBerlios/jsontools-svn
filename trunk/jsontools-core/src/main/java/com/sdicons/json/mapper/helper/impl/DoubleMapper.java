package com.sdicons.json.mapper.helper.impl;

import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.MapperHelper;
import com.sdicons.json.model.JSONDecimal;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.model.JSONString;

import java.math.BigDecimal;

public class DoubleMapper
implements MapperHelper
{
    public Class getHelpedClass()
    {
        return Double.class;
    }

    public Object toJava(JSONValue aValue, Class aRequestedClass) throws MapperException
    {
        if(aValue.isString())
        {
            try
            {
                return Double.parseDouble(((JSONString)aValue).getValue());
            }
            catch (NumberFormatException e)
            {
                throw new MapperException("DoubleMapper cannot map value: " + ((JSONString)aValue).getValue());
            }
        }
        else if(aValue.isDecimal()) return ((JSONDecimal) aValue).getValue().doubleValue();
        else throw new MapperException("DoubleMapper cannot map: " + aValue.getClass().getName());
    }

    public JSONValue toJSON(Object aPojo)
    throws MapperException
    {
        if(!Double.class.isAssignableFrom(aPojo.getClass())) throw new MapperException("DoubleMapper cannot map: " + aPojo.getClass().getName());
        return new JSONDecimal(new BigDecimal(aPojo.toString()));
    }
}
