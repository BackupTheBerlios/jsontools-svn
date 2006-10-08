package com.sdicons.json.mapper.helper.impl;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.MapperHelper;
import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONValue;

import java.util.Collection;
import java.util.LinkedList;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class CollectionMapper
implements MapperHelper
{
    public Class getHelpedClass()
    {
        return Collection.class;
    }

    public Object toJava(JSONValue aValue, Class aRequestedClass)
    throws MapperException
    {
        if (!aValue.isArray()) throw new MapperException("CollectionMapper cannot map: " + aValue.getClass().getName());
        if (!Collection.class.isAssignableFrom(aRequestedClass))
            throw new MapperException("CollectionMapper cannot map: " + aValue.getClass().getName());
        JSONArray aObject = (JSONArray) aValue;

        Collection lCollObj;

        try
        {
            // First we try to instantiate the correct
            // collection class.
            lCollObj = (Collection) aRequestedClass.newInstance();
        }
        catch (Exception e)
        {
            // If the requested class cannot create an instance because
            // it is abstract, or an interface, we use a default fallback class.
            // This solution is far from perfect, but we try to make the mapper
            // as convenient as possible.
            lCollObj = new LinkedList();
        }

        for (JSONValue lVal : aObject.getValue())
        {
            lCollObj.add(JSONMapper.toJava(lVal));
        }
        return lCollObj;
    }

    public JSONValue toJSON(Object aPojo)
    throws MapperException
    {
        JSONArray lArray = new JSONArray();
        if(! Collection.class.isAssignableFrom(aPojo.getClass())) throw new MapperException("CollectionMapper cannot map: " + aPojo.getClass().getName());

        Collection lColl = (Collection) aPojo;
        for(Object lEl : lColl)
        {
            lArray.getValue().add(JSONMapper.toJSON(lEl));
        }
        return lArray;
    }
}
