package com.sdicons.json.mapper.helper.impl;

/*
 * JSONTools - Java JSON Tools Copyright (C) 2006 S.D.I.-Consulting BVBA
 * http://www.sdi-consulting.com mailto://nospam@sdi-consulting.com This library
 * is free software; you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software
 * Foundation; either version 2.1 of the License, or (at your option) any later
 * version. This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details. You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */

import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.SimpleMapperHelper;
import com.sdicons.json.model.JSONLiteral;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;

public class JSONValueMapper implements SimpleMapperHelper
{
	public Class getHelpedClass()
	{
		return JSONValue.class;
	}

	public Object toJava(JSONValue aValue, Class aRequestedClass, Object aPojo)
	        throws MapperException
	{
		return aValue;
	}

	public JSONValue toJSON(Object aPojo) throws MapperException
	{
		if (!JSONValue.class.isAssignableFrom(aPojo.getClass()))
		{
			throw new MapperException("JSONValueMapper cannot map: " + aPojo.getClass().getName());
		}
		return (JSONValue) aPojo;
	}
}