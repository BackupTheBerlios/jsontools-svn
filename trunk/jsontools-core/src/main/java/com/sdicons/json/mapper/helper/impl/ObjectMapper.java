package com.sdicons.json.mapper.helper.impl;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.MapperHelper;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONValue;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.Iterator;

public class ObjectMapper
implements MapperHelper
{
    public Class getHelpedClass()
    {
        return Object.class;
    }

    public Object toJava(JSONValue aValue, Class aRequestedClass)
    throws MapperException
    {
        if(!aValue.isObject()) throw new MapperException("ObjectMapper cannot map: " + aValue.getClass().getName());
        JSONObject aObject = (JSONObject) aValue;

        try
        {
            final Object lBean = aRequestedClass.newInstance();
            Iterator<String> lElIter = aObject.getValue().keySet().iterator();

            while (lElIter.hasNext())
            {
                // Fetch subelement information.
                final String lPropname = lElIter.next();
                final JSONValue lSubEl = aObject.get(lPropname);

                // Put the property in the bean.
                boolean lFoundWriter = false;
                PropertyDescriptor[] lPropDesc = Introspector.getBeanInfo(aRequestedClass, Introspector.USE_ALL_BEANINFO).getPropertyDescriptors();
                for (int i = 0; i < lPropDesc.length; i++)
                {
                    if (lPropDesc[i].getName().equals(lPropname))
                    {
                        lFoundWriter = true;

                        // If there is a field with the same name as the property,
                        // we try to use that type information. In case of generic types,
                        // the field contains more information than the property.
                        // This is interesting in the case of generic collections for instance.
                        final Type lFieldType = findFieldType(lBean, lPropname);
                        
                        final Object lProp = (lFieldType!=null)?JSONMapper.toJava(lSubEl, lFieldType.getClass()):JSONMapper.toJava(lSubEl, lPropDesc[i].getPropertyType());

                        final Method lWriter = lPropDesc[i].getWriteMethod();
                        if (lWriter == null)
                        {
                            final String lMsg = "Could not find a setter for prop: " + lPropname + " in class: " + aRequestedClass;
                            throw new MapperException(lMsg);
                        }
                        lWriter.invoke(lBean, new Object[]{lProp});
                        break;
                    }
                }

                if (!lFoundWriter)
                {
                    final String lMsg = "Could not find a setter for prop: " + lPropname + " in class: " + aRequestedClass;
                    throw new MapperException(lMsg);
                }
            }
            return lBean;
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
        catch (IntrospectionException e)
        {
            final String lMsg = "IntrospectionException while trying to fill bean: " + aRequestedClass;
            throw new MapperException(lMsg);
        }
        catch (InvocationTargetException e)
        {
            final String lMsg = "InvocationTargetException while trying to fill bean: " + aRequestedClass;
            throw new MapperException(lMsg);
        }
    }

    private Type findFieldType(Object aBean, String aPropName)
    {
        try
        {
//            Field[] test = aBean.getClass().getFields();
//            for(Field f: test)
//            {
//                System.out.println(f.getGenericType().getClass().getName());
//            }
            final Field lField = aBean.getClass().getDeclaredField(aPropName);
            final ParameterizedType lType = (ParameterizedType) lField.getGenericType();
            return lType;
        }
        catch (NoSuchFieldException e)
        {
            return null;
        }
    }

    public JSONValue toJSON(Object aPojo)
    throws MapperException
    {
         // We will render the bean properties as the elements of a JSON object.
        final JSONObject lElements = new JSONObject();

        try
        {
            Class lClass = aPojo.getClass();
            PropertyDescriptor[] lPropDesc = Introspector.getBeanInfo(lClass, Introspector.USE_ALL_BEANINFO).getPropertyDescriptors();
            for (PropertyDescriptor aLPropDesc : lPropDesc)
            {
                Method lReader = aLPropDesc.getReadMethod();
                Method lWriter = aLPropDesc.getWriteMethod();
                String lPropName = aLPropDesc.getName();

                // Only serialize if the property is READ-WRITE.
                if (lReader != null && lWriter != null)
                {
                    lElements.getValue().put(lPropName, JSONMapper.toJSON(lReader.invoke(aPojo, new Object[]{})));
                }
            }
            
            return lElements;
        }
        catch(IntrospectionException e)
        {
            final String lMsg = "Error while introspecting JavaBean.";
            throw new MapperException(lMsg);
        }
        catch(IllegalAccessException e)
        {
            final String lMsg = "Illegal access while trying to fetch a bean property (1).";
            throw new MapperException(lMsg);
        }
        catch(InvocationTargetException e)
        {
            final String lMsg = "Illegal access while trying to fetch a bean property (2).";
            throw new MapperException(lMsg);
        }
    }
}
