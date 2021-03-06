package com.sdi.json.model;

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

import com.sdi.json.parser.impl.ParserUtil;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Representation of a JSON object, a collection (unordered) of name/value pairs. 
 */
public class JSONObject
extends JSONComplex
{
    private HashMap<String, JSONValue> map = new HashMap<String, JSONValue>();

    public int size()
    {
        return map.size();
    }

    public HashMap<String, JSONValue> getValue()
    {
        return map;
    }

    public String toString()
    {
        final StringBuffer lBuf = new StringBuffer();
        lBuf.append("JSONObject(").append(getLine()).append(":").append(getCol()).append(")[");
        Iterator<String> lIter = map.keySet().iterator();
        while(lIter.hasNext())
        {
            final String lKey=lIter.next();
            lBuf.append(lKey).append(":").append(map.get(lKey).toString());
            if(lIter.hasNext()) lBuf.append(", ");
        }
        lBuf.append("]");
        return lBuf.toString();
    }

    protected String render(boolean aPretty, String aIndent)
    {
        final StringBuffer lBuf = new StringBuffer();
        final Iterator<String> lKeyIter = map.keySet().iterator();
        if(aPretty)
        {
            lBuf.append(aIndent).append("{\n");
            final String lIndent = aIndent + "   ";
            final String lIndent2 = aIndent + "      ";
            while(lKeyIter.hasNext())
            {
                final String lKey = lKeyIter.next();
                final JSONValue jsonValue = (com.sdi.json.model.JSONValue) map.get(lKey);

                lBuf.append(lIndent).append(ParserUtil.render(lKey, false, ""));
                if(jsonValue.isSimple())
                {
                    lBuf.append(" : ").append(jsonValue.render(false, ""));

                }
                else
                {
                    lBuf.append(" :\n").append(jsonValue.render(true, lIndent2));
                }
                if(lKeyIter.hasNext()) lBuf.append(",\n");

                else lBuf.append("\n");
            }
            lBuf.append(aIndent).append("}");
        }
        else
        {
            lBuf.append("{");
            while(lKeyIter.hasNext())
            {
                final String lKey = lKeyIter.next();
                final JSONValue jsonValue = (com.sdi.json.model.JSONValue) map.get(lKey);

                lBuf.append(ParserUtil.render(lKey, false, "")).append(":").append(jsonValue.render(false));
                if(lKeyIter.hasNext()) lBuf.append(",");
            }
            lBuf.append(aIndent).append("}");
        }
        return lBuf.toString();
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final JSONObject that = (JSONObject) o;

        if (!map.equals(that.map)) return false;

        return true;
    }

    public int hashCode()
    {
        return map.hashCode();
    }

    public boolean containsKey(String aKey)
    {
        return map.containsKey(aKey);
    }

    public JSONValue get(String aKey)
    {
        return map.get(aKey);
    }
}
