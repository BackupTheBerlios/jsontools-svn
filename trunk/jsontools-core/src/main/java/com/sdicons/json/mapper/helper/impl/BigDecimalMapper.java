package com.sdicons.json.mapper.helper.impl;

import com.sdicons.json.mapper.helper.SimpleMapperHelper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONDecimal;

import java.math.BigDecimal;

public class BigDecimalMapper
implements SimpleMapperHelper
{
    public Class getHelpedClass()
    {
        return BigDecimal.class;
    }

    public Object toJava(JSONValue aValue, Class aRequestedClass) throws MapperException
    {
        if(aValue.isString())
        {
            try
            {
                return new BigDecimal(((JSONString)aValue).getValue());
            }
            catch (NumberFormatException e)
            {
                throw new MapperException("BigDecimalMapper cannot map value: " + ((JSONString)aValue).getValue());
            }
        }
        else if(aValue.isDecimal()) return ((JSONDecimal) aValue).getValue();
        else throw new MapperException("BigDecimalMapper cannot map: " + aValue.getClass().getName());
    }

    public JSONValue toJSON(Object aPojo)
    throws MapperException
    {
        if(!BigDecimal.class.isAssignableFrom(aPojo.getClass())) throw new MapperException("BigDecimalMapper cannot map: " + aPojo.getClass().getName());
        return new JSONDecimal(new BigDecimal(aPojo.toString()));
    }
}
