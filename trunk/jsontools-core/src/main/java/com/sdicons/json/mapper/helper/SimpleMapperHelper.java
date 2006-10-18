package com.sdicons.json.mapper.helper;

import com.sdicons.json.helper.Helper;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.mapper.MapperException;

public interface SimpleMapperHelper
        extends Helper
{
    Object toJava(JSONValue aValue, Class aRequestedClass)
    throws MapperException;

    JSONValue toJSON(Object aPojo)
    throws MapperException;
}
