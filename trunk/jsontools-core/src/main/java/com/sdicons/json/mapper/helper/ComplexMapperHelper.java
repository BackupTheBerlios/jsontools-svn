package com.sdicons.json.mapper.helper;

import com.sdicons.json.helper.Helper;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.mapper.MapperException;

public interface ComplexMapperHelper
extends SimpleMapperHelper    
{
    Object toJava(JSONValue aValue, Class aRequestedClass, Class[] aHelperClasses)
    throws MapperException;

    JSONValue toJSON(Object aPojo)
    throws MapperException;
}
