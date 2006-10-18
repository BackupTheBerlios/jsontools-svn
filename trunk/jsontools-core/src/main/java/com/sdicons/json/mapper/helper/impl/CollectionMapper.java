package com.sdicons.json.mapper.helper.impl;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.ComplexMapperHelper;
import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONValue;

import java.util.Collection;
import java.util.LinkedList;

public class CollectionMapper
implements ComplexMapperHelper
{
    public Class getHelpedClass()
    {
        return Collection.class;
    }

    public Object toJava(JSONValue aValue, Class aRequestedClass)
    throws MapperException
    {
        return this.toJava(aValue, aRequestedClass, new Class[0]);
    }

    public Object toJava(JSONValue aValue, Class aRawClass, Class[] aHelperClasses)
    throws MapperException
    {
        if (!aValue.isArray()) throw new MapperException("CollectionMapper cannot map: " + aValue.getClass().getName());
        if (!Collection.class.isAssignableFrom(aRawClass))
            throw new MapperException("CollectionMapper cannot map: " + aValue.getClass().getName());
        JSONArray aObject = (JSONArray) aValue;

        Collection lCollObj;

        try
        {
            // First we try to instantiate the correct
            // collection class.
            lCollObj = (Collection) aRawClass.newInstance();
        }
        catch (Exception e)
        {
            // If the requested class cannot create an instance because
            // it is abstract, or an interface, we use a default fallback class.
            // This solution is far from perfect, but we try to make the mapper
            // as convenient as possible.
            lCollObj = new LinkedList();
        }

        if(aHelperClasses.length == 0)
        {
            // Simple, raw collection.
            for (JSONValue lVal : aObject.getValue())
            {
                lCollObj.add(JSONMapper.toJava(lVal));
            }
        }
        else if(aHelperClasses.length == 1)
        {
            // Generic collection, we can make use of the type of the elements.            
            for (JSONValue lVal : aObject.getValue())
            {
                lCollObj.add(JSONMapper.toJava(lVal, aHelperClasses[0]));
            }
        }
        else
        {
            // Not possible, a collection cannot have more than two types for
            // its contents.
            throw new MapperException("CollectionMapper cannot map: " + aValue.getClass().getName());
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
