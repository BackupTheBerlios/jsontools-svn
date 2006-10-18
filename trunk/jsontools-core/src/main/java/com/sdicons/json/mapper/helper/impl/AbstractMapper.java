package com.sdicons.json.mapper.helper.impl;

import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.SimpleMapperHelper;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;

public abstract class AbstractMapper
implements SimpleMapperHelper
{
    public JSONValue toJSON(Object aPojo) throws MapperException
    {
        return new JSONString(aPojo.toString());
    }
}
