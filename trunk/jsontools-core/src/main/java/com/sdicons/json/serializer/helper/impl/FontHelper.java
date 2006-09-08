package com.sdicons.json.serializer.helper.impl;

import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.serializer.marshall.JSONMarshall;
import com.sdicons.json.serializer.marshall.MarshallException;

import java.awt.*;
import java.util.HashMap;

public class FontHelper
extends AbstractHelper
{
    public Object parseValue(JSONObject aObjectElement, JSONMarshall aMarshall, HashMap aPool)
    throws MarshallException
    {
        JSONMarshall.requireStringAttribute(aObjectElement, JSONMarshall.RNDR_ATTR_VALUE);
        return Font.decode(((JSONString) aObjectElement.get(JSONMarshall.RNDR_ATTR_VALUE)).getValue());
    }

    public void renderValue(Object aObj, JSONObject aParent, JSONMarshall aMarshall, HashMap aPool) throws MarshallException
    {
        final Font lFont = (Font) aObj;
        final int lFontStyle = lFont.getStyle();
        
        String lStyle;
        switch(lFontStyle)
        {
            case Font.PLAIN: lStyle = "PLAIN"; break;
            case Font.BOLD: lStyle =  "BOLD"; break;
            case Font.ITALIC: lStyle = "ITALIC";break;
            case 3: lStyle = "BOLDITALIC";break;
            default: lStyle="PLAIN";
        }
        aParent.getValue().put(JSONMarshall.RNDR_ATTR_VALUE, new JSONString( lFont.getName() + "-"+ lStyle + "-" + lFont.getSize()));
    }

    public Class getHelpedClass()
    {
        return Font.class;
    }
}
