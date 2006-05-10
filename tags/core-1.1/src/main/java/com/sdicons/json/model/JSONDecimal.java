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

import java.math.BigDecimal;

/**
 * Represents a JSON float. JSON only defines "numbers" but during parsing a diffrence is
 * made between integral types and floating types.
 */
public class JSONDecimal
extends JSONNumber
{
    private BigDecimal value;

    public JSONDecimal(BigDecimal value)
    {
        if(value == null) throw new IllegalArgumentException();
        this.value = value;
    }

    public BigDecimal getValue()
    {
        return value;
    }

    public String toString()
    {
        return "JSONDecimal(" + getLine() + ":" + getCol() + ")[" + value.toString() + "]";
    }

    String render(boolean pretty, String indent)
    {
        if(pretty) return indent + value;
        else return "" + value;
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final JSONDecimal that = (JSONDecimal) o;

        if (!value.equals(that.value)) return false;

        return true;
    }

    public int hashCode()
    {
        return value.hashCode();
    }
}
