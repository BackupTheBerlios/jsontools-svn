package com.sdicons.json.mapper.helper.impl;

import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.MapperHelper;
import com.sdicons.json.model.JSONBoolean;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.model.JSONString;

public class BooleanMapper
implements MapperHelper
{
    public Class getHelpedClass()
    {
        return Boolean.class;
    }

    public Object toJava(JSONValue aValue, Class aRequestedClass) throws MapperException
    {
        if(aValue.isString())
        {
            return Boolean.parseBoolean(((JSONString)aValue).getValue());
        }
        else if(aValue.isBoolean())
        {
            return ((JSONBoolean) aValue).getValue();
        }
        else throw new MapperException("BooleanMapper cannot map: " + aValue.getClass().getName());        
    }

    public JSONValue toJSON(Object aPojo)
    throws MapperException
    {
        if(!Boolean.class.isAssignableFrom(aPojo.getClass())) throw new MapperException("BooleanMapper cannot map: " + aPojo.getClass().getName());
        return new JSONBoolean((Boolean) aPojo);
    }
}
