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

import java.math.BigInteger;
import java.util.Date;

import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.model.JSONInteger;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;

/**
 * The original JSONTools 1.5 class used to do a whole lot of maneuvering to get dates to
 * to RFC3339 format, which would be cool if javascript could read RFC3339 natively, but it
 * can't.  Maybe I'm just retarded and doing something wrong,
 * but I can't even get Javascript's date.parse to read
 * ISO8601.  Since the whole point of a JSON parser/mapper is to put data into javascript in
 * a format that is usable, I'm cutting right to the bone and using the only date format that
 * will always work.  Unix time.  Yeah, it's not human readable in the serialized json, but
 * when it comes across to the other side it's just going to be put into a "new Date()" anyways...
 * @author itaylor@zloop.com
 *
 */

public class DateMapper extends AbstractMapper
{

	public Class getHelpedClass()
	{
		return Date.class;
	}

	@Override
	public JSONValue toJSON(Object aPojo) throws MapperException
	{
		if (aPojo instanceof Date)
		{
			return new JSONInteger(new BigInteger(Long.toString(((Date)aPojo).getTime()))); //yuk
		}
		throw new MapperException("The object passed to DateMapper was not a date");
	}

	public Object toJava(JSONValue aValue, Class aRequestedClass, Object object)
	        throws MapperException
	{
		Date theDate = new Date();
		if (aValue.isNumber())
		{
			theDate.setTime(((JSONInteger) aValue).getValue().longValue());
		}
		else if (aValue.isString())
		{
			theDate.setTime(Long.parseLong(((JSONString)aValue).getValue()));
		}
		else
		{
			throw new MapperException("Date could not be parsed: " +aValue.toString());
		}
		return theDate;
	}

}