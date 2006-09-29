package com.sdicons.json.mapper.helper.impl;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.MapperHelper;
import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONValue;

import java.util.Collection;
import java.util.LinkedList;

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
        if(!aValue.isArray()) throw new MapperException("CollectionMapper cannot map: " + aValue.getClass().getName());
        JSONArray aObject = (JSONArray) aValue;

        Collection lCollObj = new LinkedList();

        for (JSONValue lVal : aObject.getValue())
        {
            lCollObj.add(JSONMapper.toJava(lVal, Object.class));
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
