package com.sdicons.json.mapper.helper.impl;

import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.MapperHelper;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;

public abstract class AbstractMapper
implements MapperHelper
{
    public JSONValue toJSON(Object aPojo) throws MapperException
    {
        return new JSONString(aPojo.toString());
    }
}
