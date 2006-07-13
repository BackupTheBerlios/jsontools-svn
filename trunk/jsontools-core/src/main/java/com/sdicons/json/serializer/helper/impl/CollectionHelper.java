package com.sdicons.json.serializer.helper.impl;

import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.serializer.helper.Helper;
import com.sdicons.json.serializer.marshall.JSONMarshall;
import com.sdicons.json.serializer.marshall.MarshallException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class CollectionHelper
implements Helper
{
    public CollectionHelper()
    {
    }

    public void renderValue(Object aObj, JSONObject aObjectElement, JSONMarshall aMarshall, HashMap aPool)
    throws MarshallException
    {
        final Collection lCollection = (Collection) aObj;
        final Iterator lIter = lCollection.iterator();
        final JSONArray lArray = new JSONArray();
        aObjectElement.getValue().put(JSONMarshall.RNDR_ATTR_VALUE, lArray);

        while(lIter.hasNext())
        {
            Object lColEl = lIter.next();
            lArray.getValue().add(aMarshall.marshallImpl(lColEl, aPool));
        }
    }

    public Object parseValue(JSONObject aObjectElement, JSONMarshall aMarshall, HashMap aPool)
    throws MarshallException
    {
        final JSONArray lArray = (JSONArray) aObjectElement.getValue().get(JSONMarshall.RNDR_ATTR_VALUE);

        JSONMarshall.requireStringAttribute(aObjectElement, JSONMarshall.RNDR_ATTR_CLASS);
        String lCollectionClassName = ((JSONString) aObjectElement.get(JSONMarshall.RNDR_ATTR_CLASS)).getValue();

        String lId = null;
        try
        {
            JSONMarshall.requireStringAttribute(aObjectElement, JSONMarshall.RNDR_ATTR_ID);
            lId = ((JSONString) aObjectElement.get(JSONMarshall.RNDR_ATTR_ID)).getValue();
        }
        catch (Exception eIgnore) { }

        try
        {
            Class lCollectionClass = Class.forName(lCollectionClassName);
            Collection lCollection = null;

            lCollection = (Collection) lCollectionClass.newInstance();
            if (lId != null) aPool.put(lId, lCollection);

            for(JSONValue lVal : lArray.getValue())
            {
                lCollection.add(aMarshall.unmarshallImpl((JSONObject)lVal, aPool));
            }
            return lCollection;
        }
        catch (IllegalAccessException e)
        {
            final String lMsg = "IllegalAccessException while trying to instantiate collection: " + lCollectionClassName;
            throw new MarshallException(lMsg);
        }
        catch (InstantiationException e)
        {
            final String lMsg = "InstantiationException while trying to instantiate collection: " + lCollectionClassName;
            throw new MarshallException(lMsg);
        }
        catch (ClassNotFoundException e)
        {
            final String lMsg = "ClassNotFoundException while trying to instantiate collection: " + lCollectionClassName;
            throw new MarshallException(lMsg);
        }
    }

    public Class getHelpedClass()
    {
        return Collection.class;
    }
}
