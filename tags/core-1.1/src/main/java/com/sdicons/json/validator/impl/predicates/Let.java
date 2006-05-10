package com.sdicons.json.validator.impl.predicates;

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

import com.sdicons.json.validator.Validator;
import com.sdicons.json.validator.ValidationException;
import com.sdicons.json.validator.impl.ValidatorUtil;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.model.JSONString;

import java.util.List;
import java.util.HashMap;

public class Let
extends Predicate
{
    private HashMap<String, Validator> ruleset;
    private String ref;


    public Let(String aName, JSONObject aRule, HashMap<String,Validator> aRuleset)
    throws ValidationException
    {
        super(aName, aRule);

        ruleset = aRuleset;
        ValidatorUtil.requiresAttribute(aRule, ValidatorUtil.PARAM_RULES, JSONArray.class);
        List<JSONValue> lRules = (List<JSONValue>) ((JSONArray) aRule.get(ValidatorUtil.PARAM_RULES)).getValue();
        for (JSONValue lRule : lRules)
        {
            JSONValue lVal = (JSONValue) lRule;
            ValidatorUtil.buildValidator(lVal, ruleset);
        }

        ValidatorUtil.requiresAttribute(aRule, ValidatorUtil.PARAM_REF, JSONString.class);
        ref = ((JSONString) aRule.get(ValidatorUtil.PARAM_REF)).getValue();
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        if(ruleset.containsKey(ref))
        {
            Validator lValidator = ruleset.get(ref);
            lValidator.validate(aValue);
        }
        else fail("Reference to an unexisting rule: \"" + ref + "\"." ,aValue);
    }
}
