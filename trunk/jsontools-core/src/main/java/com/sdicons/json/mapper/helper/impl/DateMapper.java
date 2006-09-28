package com.sdicons.json.mapper.helper.impl;

import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateMapper
extends AbstractMapper
{
    private SimpleDateFormat dateFormat;

    public DateMapper()
    {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS z");
        dateFormat.setLenient(false);
    }

    public Class getHelpedClass()
    {
        return Date.class;
    }

    public JSONValue toJSON(Object aPojo) throws MapperException
    {
        return new JSONString(dateFormat.format((Date) aPojo));
    }

    public Object toJava(JSONValue aValue, Class aRequestedClass) throws MapperException
    {
        if (!aValue.isString()) throw new MapperException("DateMapper cannot map class: " + aValue.getClass().getName());

        try
        {
            return dateFormat.parse(((JSONString) aValue).getValue().trim());
        }
        catch (Exception e)
        {
            throw new MapperException(e.getMessage());
        }
    }
}
