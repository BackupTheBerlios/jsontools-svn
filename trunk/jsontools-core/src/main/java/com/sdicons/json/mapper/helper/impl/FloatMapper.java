package com.sdicons.json.mapper.helper.impl;

import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.SimpleMapperHelper;
import com.sdicons.json.model.JSONDecimal;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.model.JSONString;

import java.math.BigDecimal;

public class FloatMapper
implements SimpleMapperHelper
{
    public Class getHelpedClass()
    {
        return Float.class;
    }

    public Object toJava(JSONValue aValue, Class aRequestedClass) throws MapperException
    {
        if(aValue.isString())
        {
            try
            {
                return Float.parseFloat(((JSONString)aValue).getValue());
            }
            catch (NumberFormatException e)
            {
                throw new MapperException("FloatMapper cannot map value: " + ((JSONString)aValue).getValue());
            }
        }
        else if(aValue.isDecimal()) return ((JSONDecimal) aValue).getValue().floatValue();
        else throw new MapperException("FloatMapper cannot map: " + aValue.getClass().getName());        
    }

    public JSONValue toJSON(Object aPojo)
    throws MapperException
    {
        if(!Float.class.isAssignableFrom(aPojo.getClass())) throw new MapperException("FloatMapper cannot map: " + aPojo.getClass().getName());
        return new JSONDecimal(new BigDecimal(aPojo.toString()));
    }
}
