package com.sdicons.json.serializer.helper.impl;

/*
    JSONTools - Java JSON Tools
    Copyright (C) 2006 S.D.I.-Consulting BVBA
    http://www.sdi-consulting.com
    mailto://nospam@sdi-consulting.com

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/

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