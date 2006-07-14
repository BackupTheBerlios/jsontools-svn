package com.sdicons.json.serializer.helper.impl;

import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.serializer.marshall.JSONMarshall;
import com.sdicons.json.serializer.marshall.MarshallException;

import java.util.HashMap;
import java.math.BigDecimal;

public class BigDecimalHelper
extends AbstractHelper
{
    public Object parseValue(JSONObject aObjectElement, JSONMarshall aMarshall, HashMap aPool)
    throws MarshallException
    {
        JSONMarshall.requireStringAttribute(aObjectElement, JSONMarshall.RNDR_ATTR_VALUE);
        return new BigDecimal(((JSONString) aObjectElement.get(JSONMarshall.RNDR_ATTR_VALUE)).getValue());
    }

    public Class getHelpedClass()
    {
        return BigDecimal.class;
    }
}
