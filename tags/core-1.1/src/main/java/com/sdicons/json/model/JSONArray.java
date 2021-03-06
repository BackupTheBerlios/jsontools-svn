package com.sdicons.json.model;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a JSON array (list), an ordered list of values..
 */
public class JSONArray
extends JSONComplex
{
    private List<JSONValue> array = new ArrayList<JSONValue>();

    /**
     * @return The length of the array (list).
     */
    public int size()
    {
        return array.size();
    }

    /**
     * @return The JSON elements in the array (list).
     */
    public List<JSONValue> getValue()
    {
        return array;
    }

    public String toString()
    {
        final StringBuffer lBuf = new StringBuffer();
        lBuf.append("JSONArray(").append(getLine()).append(":").append(getCol()).append(")[");
        for (int i = 0; i < array.size(); i++)
        {
            JSONValue jsonValue = (com.sdicons.json.model.JSONValue) array.get(i);
            lBuf.append(jsonValue.toString());
            if(i < array.size() - 1) lBuf.append(", ");
        }
        lBuf.append("]");
        return lBuf.toString();
    }

    protected String render(boolean aPretty, String aIndent)
    {
        final StringBuffer lBuf = new StringBuffer();
        if(aPretty)
        {
            lBuf.append(aIndent).append("[\n");
            String lIndent = aIndent + "   ";
            for (int i = 0; i < array.size(); i++)
            {
                JSONValue jsonValue = (com.sdicons.json.model.JSONValue) array.get(i);
                lBuf.append(jsonValue.render(true, lIndent));
                if(i < array.size() - 1) lBuf.append(",\n");
                else lBuf.append("\n");
            }
            lBuf.append(aIndent).append("]");            
        }
        else
        {
            lBuf.append("[");
            for (int i = 0; i < array.size(); i++)
            {
                JSONValue jsonValue = (com.sdicons.json.model.JSONValue) array.get(i);
                lBuf.append(jsonValue.render(false, ""));
                if(i < array.size() - 1) lBuf.append(",");
            }
            lBuf.append("]");
        }
        return lBuf.toString();
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final JSONArray jsonArray = (JSONArray) o;

        if (!array.equals(jsonArray.array)) return false;

        return true;
    }

    public int hashCode()
    {
        return array.hashCode();
    }
}
