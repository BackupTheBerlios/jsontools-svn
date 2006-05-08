package com.sdi.json.validator.impl.predicates;

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

import com.sdi.json.model.JSONArray;
import com.sdi.json.model.JSONObject;
import com.sdi.json.model.JSONValue;
import com.sdi.json.validator.ValidationException;
import com.sdi.json.validator.Validator;
import com.sdi.json.validator.impl.ValidatorUtil;

import java.util.HashMap;
import java.util.Iterator;

public class Array
extends Predicate
{
    public Array(String aName, JSONObject aRule)
    throws ValidationException
    {
        super(aName, aRule);
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        // First we check if we have an array.
        if(!aValue.isArray()) fail("The value is not a JSONArray.", aValue);
    }
}