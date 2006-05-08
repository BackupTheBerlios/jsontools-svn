package com.sdi.json.validator;

import com.sdi.json.validator.impl.predicates.CustomValidator;
import com.sdi.json.model.JSONValue;
import com.sdi.json.model.JSONObject;

import java.util.HashMap;

public class MyValidator
extends CustomValidator
{
    public MyValidator(String aName, JSONObject aRule, HashMap<String, Validator> aRuleset)
    {
        super(aName, aRule, aRuleset);
    }

    public void validate(JSONValue aValue) throws ValidationException
    {
        System.out.println("**** CUSTOM VALIDATOR *****");
    }
}
