package com.sdicons.json.serializer.helper.impl;

import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.serializer.marshall.JSONMarshall;
import com.sdicons.json.serializer.marshall.MarshallException;

import java.math.BigInteger;
import java.util.HashMap;

public class BigIntegerHelper
extends AbstractHelper
{
    public Object parseValue(JSONObject aObjectElement, JSONMarshall aMarshall, HashMap aPool)
    throws MarshallException
    {
        JSONMarshall.requireStringAttribute(aObjectElement, JSONMarshall.RNDR_ATTR_VALUE);
        return new BigInteger(((JSONString) aObjectElement.get(JSONMarshall.RNDR_ATTR_VALUE)).getValue());
    }

    public Class getHelpedClass()
    {
        return BigInteger.class;
    }
}
