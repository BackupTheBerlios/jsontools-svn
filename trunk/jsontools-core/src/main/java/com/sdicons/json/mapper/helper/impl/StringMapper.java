package com.sdicons.json.mapper.helper.impl;

import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;

public class StringMapper
extends AbstractMapper
{
    public Class getHelpedClass()
    {
        return String.class;
    }

    public Object toJava(JSONValue aValue, Class aRequestedClass) throws MapperException
    {
        if (!aValue.isString()) throw new MapperException("StringMapper cannot map class: " + aValue.getClass().getName());
        return ((JSONString) aValue).getValue();
    }
}
