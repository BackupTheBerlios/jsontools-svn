package com.sdicons.json.mapper.helper.impl;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.SimpleMapperHelper;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONValue;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.LinkedList;
import java.util.List;

public class ObjectMapperDirect
implements SimpleMapperHelper
{
    public Class getHelpedClass()
    {
        return Object.class;
    }

    protected List<Field> getFieldInfo(Class aClass)
    {
        final List<Field> lJavaFields = new LinkedList<Field>();
        Class lClassWalker = aClass;
        while (lClassWalker != null)
        {
            final Field[] lClassFields = lClassWalker.getDeclaredFields();
            for (Field lFld : lClassFields)
            {
                int lModif = lFld.getModifiers();
                if (!Modifier.isTransient(lModif) &&
                        !Modifier.isAbstract(lModif) &&
                        !Modifier.isStatic(lModif) &&
                        !Modifier.isFinal(lModif))
                {
                    lFld.setAccessible(true);
                    lJavaFields.add(lFld);
                }
            }
            lClassWalker = lClassWalker.getSuperclass();
        }
        return lJavaFields;
    }

    public Object toJava(JSONValue aValue, Class aRequestedClass)
    throws MapperException
    {
        if(!aValue.isObject()) throw new MapperException("ObjectMapperDirect cannot map: " + aValue.getClass().getName());
        JSONObject aObject = (JSONObject) aValue;

        try
        {
            final List<Field> lJavaFields = getFieldInfo(aRequestedClass);
            Object lResult = aRequestedClass.newInstance();
            for (String lPropname : aObject.getValue().keySet())
            {
                // Fetch subelement information.
                final JSONValue lSubEl = aObject.get(lPropname);
                for (Field lFld : lJavaFields)
                {
                    // Write the property.
                    if (lFld.getName().equals(lPropname))
                    {
                        Object lFldValue;
                        final Type lGenType = lFld.getGenericType();
                        if(lGenType instanceof ParameterizedType)
                        {
                            lFldValue = JSONMapper.toJava(lSubEl, (ParameterizedType) lGenType);                            
                        }
                        else
                        {
                            lFldValue = JSONMapper.toJava(lSubEl, lFld.getType());
                        }

                        try
                        {
                            lFld.setAccessible(true);
                            lFld.set(lResult, lFldValue);
                            break;
                        }
                        catch (Exception e)
                        {
                            throw new MapperException(String.format("ObjectMapperDirect error while deserializing. Type error while trying to set the field: '%1$s' in class: '%2$s' with a value of class: '%3$s'.", lFld.getName(), aRequestedClass.getName(), lFldValue.getClass().getName()), e);
                        }
                    }
                }
            }

            // First we have to cope with the special case of "readResolve" objects.
            // It is described in the official specs of the serializable interface.
            ////////////////////////////////////////////////////////////////////////////
            if (lResult instanceof Serializable)
            {
                try
                {
                    final Method lReadResolve = lResult.getClass().getDeclaredMethod("readResolve");
                    if (lReadResolve != null)
                    {
                        lReadResolve.setAccessible(true);
                        lResult = lReadResolve.invoke(lResult);
                    }
                }
                catch (NoSuchMethodException e)
                {
                    // Do nothing, just continue normal operation.
                }
                catch (Exception e)
                {
                    throw new MapperException(String.format("ObjectMapperDirect error while creating java object. Tried to invoke 'readResolve' on instance of class: '%1$s'.", lResult.getClass().getName()), e);
                }
            }
            ////////////////////////////////////////////////////////////////////////////

            return lResult;
        }
        catch (IllegalAccessException e)
        {
            final String lMsg = "IllegalAccessException while trying to instantiate bean: " + aRequestedClass;
            throw new MapperException(lMsg);
        }
        catch (InstantiationException e)
        {
            final String lMsg = "InstantiationException while trying to instantiate bean: " + aRequestedClass;
            throw new MapperException(lMsg);
        }
    }

    public JSONValue toJSON(Object aPojo)
            throws MapperException
    {
        // We will render the bean properties as the elements of a JSON object.
        final JSONObject lElements = new JSONObject();

        // First we have to cope with the special case of "writeReplace" objects.
        // It is described in the official specs of the serializable interface.
        ////////////////////////////////////////////////////////////////////////////
        if(aPojo instanceof Serializable)
        {
            try
            {
                final Method lWriteReplace = aPojo.getClass().getDeclaredMethod("writeReplace");
                if(lWriteReplace != null)
                {
                    lWriteReplace.setAccessible(true);
                    return JSONMapper.toJSON(lWriteReplace.invoke(aPojo));
                }
            }
            catch(NoSuchMethodException e)
            {
                // Do nothing, just continue normal operation.
            }
            catch(Exception e)
            {
                throw new MapperException(String.format("ObjectMapperDirect error while trying to invoke 'writeReplace' on instance of class: '%1$s'.", aPojo.getClass().getName()), e);
            }
        }
        ////////////////////////////////////////////////////////////////////////////

        Class lJavaClass = aPojo.getClass();
        final List<Field> lJavaFields = getFieldInfo(lJavaClass);
        for(Field lFld : lJavaFields)
        {
            try
            {
                lFld.setAccessible(true);
                final JSONValue lFieldVal = JSONMapper.toJSON(lFld.get(aPojo));
                lElements.getValue().put(lFld.getName(), lFieldVal);
            }
            catch(Exception e)
            {
                throw new MapperException(String.format("ObjectMapperDirect error while serializing. Error while reading field: '%1$s' from instance of class: '%2$s'.", lFld.getName(), lJavaClass.getName()), e);
            }
        }
        return lElements;
    }
}
