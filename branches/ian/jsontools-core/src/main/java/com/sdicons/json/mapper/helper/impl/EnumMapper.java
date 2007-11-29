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
 *
 * Added by Ian Taylor @ zloop.com June 2007
 */

import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;

public class EnumMapper extends AbstractMapper
{
	public Class getHelpedClass()
	{
		return Enum.class;
	}

	public Object toJava(JSONValue aValue, Class aRequestedClass, Object aPojo) throws MapperException
	{
		if (!aValue.isString())
		{
			throw new MapperException("EnumMapper cannot map class: " +
			        aValue.getClass().getName() +
			        " enums are mapped as their string representation");
		}

		return Enum.valueOf(aRequestedClass, ((JSONString) aValue).getValue());
	}
}