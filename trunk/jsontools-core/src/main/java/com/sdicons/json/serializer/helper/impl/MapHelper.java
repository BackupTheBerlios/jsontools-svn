package com.sdicons.json.serializer.helper.impl;

import com.sdicons.json.serializer.helper.Helper;
import com.sdicons.json.serializer.marshall.JSONMarshall;
import com.sdicons.json.serializer.marshall.MarshallException;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MapHelper
implements Helper
{
    public MapHelper()
    {
    }

    public void renderValue(Object aObj, JSONObject aObjectElement, JSONMarshall aMarshall, HashMap aPool)
    throws MarshallException
    {
        // We create a new JSON array where we will collect the elements of the
        // map. We attach this new array as the parent object value.
        final JSONArray lArray = new JSONArray();
        aObjectElement.getValue().put(JSONMarshall.RNDR_ATTR_VALUE, lArray);

        // We iterate through the keys of the map, render these as
        // JSON values and put these values in the array created above.
        final Map lMap = (Map) aObj;
        final Iterator lIter = lMap.keySet().iterator();
        while(lIter.hasNext())
        {
            // Get hold of each key-value pair.
            final Object lKey = lIter.next();
            final Object lValue = lMap.get(lKey);

            // We create a JSON object to render the key-value pairs.
            final JSONObject lKeyValuePair = new JSONObject();
            lArray.getValue().add(lKeyValuePair);
            lKeyValuePair.getValue().put("key", aMarshall.marshallImpl(lKey, aPool));
            lKeyValuePair.getValue().put("value", aMarshall.marshallImpl(lValue, aPool));
        }
    }

    public Object parseValue(JSONObject aObjectElement, JSONMarshall aMarshall, HashMap aPool)
    throws MarshallException
    {
        final JSONArray lArray = (JSONArray) aObjectElement.getValue().get(JSONMarshall.RNDR_ATTR_VALUE);

        JSONMarshall.requireStringAttribute(aObjectElement, JSONMarshall.RNDR_ATTR_CLASS);
        String lMapClassName = ((JSONString) aObjectElement.get(JSONMarshall.RNDR_ATTR_CLASS)).getValue();

        String lId = null;
        try
        {
            JSONMarshall.requireStringAttribute(aObjectElement, JSONMarshall.RNDR_ATTR_ID);
            lId = ((JSONString) aObjectElement.get(JSONMarshall.RNDR_ATTR_ID)).getValue();
        }
        catch (Exception eIgnore) { }

        try
        {
            Class lMapClass = Class.forName(lMapClassName);
            Map lMap = null;

            lMap = (Map) lMapClass.newInstance();
            if (lId != null) aPool.put(lId, lMap);

            for(JSONValue lKeyValue : lArray.getValue())
            {
                Object lKey = aMarshall.unmarshallImpl((JSONObject) ((JSONObject) lKeyValue).getValue().get("key"), aPool);
                Object lValue = aMarshall.unmarshallImpl((JSONObject) ((JSONObject) lKeyValue).getValue().get("value"), aPool);
                lMap.put(lKey, lValue);
            }
            return lMap;
        }
        catch (IllegalAccessException e)
        {
            final String lMsg = "IllegalAccessException while trying to instantiate map: " + lMapClassName;
            throw new MarshallException(lMsg);
        }
        catch (InstantiationException e)
        {
            final String lMsg = "InstantiationException while trying to instantiate map: " + lMapClassName;
            throw new MarshallException(lMsg);
        }
        catch (ClassNotFoundException e)
        {
            final String lMsg = "ClassNotFoundException while trying to instantiate map: " + lMapClassName;
            throw new MarshallException(lMsg);
        }
    }

    public Class getHelpedClass()
    {
        return Map.class;
    }
}
