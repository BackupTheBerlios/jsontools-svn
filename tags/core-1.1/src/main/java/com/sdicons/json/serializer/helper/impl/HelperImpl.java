package com.sdicons.json.serializer.helper.impl;

/*
    JSONTools - Java JSON Tools
    Copyright (C) 2006 S.D.I.-Consulting BVBA
    http://www.sdicons-consulting.com
    mailto://nospam@sdicons-consulting.com

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

import com.sdicons.json.serializer.helper.Helper;
import com.sdicons.json.serializer.marshall.MarshallException;
import com.sdicons.json.serializer.marshall.JSONMarshall;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;

import java.util.*;

public abstract class HelperImpl
implements Helper
{
    public boolean equals(Object obj)
    {
        return this.getHelpedClass() == ((Helper) obj).getHelpedClass();
    }

    public void renderValue(Object aObj, JSONObject aParent, JSONMarshall aMarshall, HashMap aPool)
    throws MarshallException
    {
        aParent.getValue().put(JSONMarshall.RNDR_ATTR_VALUE, new JSONString(aObj.toString()));
    }
}
