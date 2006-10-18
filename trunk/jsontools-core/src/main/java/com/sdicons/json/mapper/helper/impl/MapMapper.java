package com.sdicons.json.mapper.helper.impl;

import com.sdicons.json.mapper.helper.ComplexMapperHelper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONObject;

import java.util.*;

public class MapMapper
implements ComplexMapperHelper
{
    public Class getHelpedClass()
    {
        return Map.class;
    }

    public Object toJava(JSONValue aValue, Class aRequestedClass)
    throws MapperException
    {
        return this.toJava(aValue, aRequestedClass, new Class[0]);
    }

    public Object toJava(JSONValue aValue, Class aRawClass, Class[] aHelperClasses)
    throws MapperException
    {
        if (!aValue.isObject()) throw new MapperException("MapMapper cannot map: " + aValue.getClass().getName());
        if (!Map.class.isAssignableFrom(aRawClass))
            throw new MapperException("MapMapper cannot map: " + aValue.getClass().getName());
        JSONObject aObject = (JSONObject) aValue;

        Map lMapObj;

        try
        {
            // First we try to instantiate the correct
            // collection class.
            lMapObj = (Map) aRawClass.newInstance();
        }
        catch (Exception e)
        {
            // If the requested class cannot create an instance because
            // it is abstract, or an interface, we use a default fallback class.
            // This solution is far from perfect, but we try to make the mapper
            // as convenient as possible.
            lMapObj = new LinkedHashMap();
        }

        if(aHelperClasses.length == 0)
        {
            // Simple, raw collection.
            for (String lKey : aObject.getValue().keySet())
            {
                JSONValue lVal = aObject.getValue().get(lKey);
                lMapObj.put(lKey, JSONMapper.toJava(lVal));
            }
        }
        else if(aHelperClasses.length == 2)
        {
            // Generic map, we can make use of the type of the elements.
            if(!aHelperClasses[0].equals(String.class)) throw new MapperException("MapMapper currently only supports String keys.");
            else
            {
                for (String lKey : aObject.getValue().keySet())
                {
                    JSONValue lVal = aObject.getValue().get(lKey);
                    lMapObj.put(lKey, JSONMapper.toJava(lVal, aHelperClasses[1]));
                }
            }
        }
        else
        {
            // Not possible, a collection cannot have more than two types for
            // its contents.
            throw new MapperException("MapMapper cannot map: " + aValue.getClass().getName());
        }

        return lMapObj;
    }

    public JSONValue toJSON(Object aPojo)
    throws MapperException
    {
        final JSONObject lObj = new JSONObject();
        if(! Map.class.isAssignableFrom(aPojo.getClass())) throw new MapperException("MapMapper cannot map: " + aPojo.getClass().getName());

        Map lMap = (Map) aPojo;
        for(Object lKey : lMap.keySet())
        {
            lObj.getValue().put(lKey.toString(), JSONMapper.toJSON(lMap.get(lKey)));
        }
        return lObj;
    }
}
