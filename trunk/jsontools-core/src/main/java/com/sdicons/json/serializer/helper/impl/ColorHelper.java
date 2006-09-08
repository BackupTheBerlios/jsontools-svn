package com.sdicons.json.serializer.helper.impl;

import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.serializer.marshall.JSONMarshall;
import com.sdicons.json.serializer.marshall.MarshallException;

import java.util.HashMap;
import java.util.Date;
import java.awt.*;

public class ColorHelper
extends AbstractHelper
{
    public Object parseValue(JSONObject aObjectElement, JSONMarshall aMarshall, HashMap aPool)
    throws MarshallException
    {
        JSONMarshall.requireStringAttribute(aObjectElement, JSONMarshall.RNDR_ATTR_VALUE);
        return Color.decode(((JSONString) aObjectElement.get(JSONMarshall.RNDR_ATTR_VALUE)).getValue());
    }

    public void renderValue(Object aObj, JSONObject aParent, JSONMarshall aMarshall, HashMap aPool) throws MarshallException
    {
        Color lColor = (Color) aObj;
//        StringBuffer lBuf = new StringBuffer("0x");
//        lBuf
//            .append(Integer.toString(lColor.getRed(), 16))
//            .append(Integer.toString(lColor.getGreen(), 16))
//            .append(Integer.toString(lColor.getBlue(), 16))
//            .append("00");
        aParent.getValue().put(JSONMarshall.RNDR_ATTR_VALUE, new JSONString("" + lColor.getRGB()));
    }

    public Class getHelpedClass()
    {
        return Color.class;
    }
}
