package com.sdicons.json.mapper.helper.impl;

/*
 * JSONTools - Java JSON Tools Copyright (C) 2006 S.D.I.-Consulting BVBA
 * http://www.sdi-consulting.com mailto://nospam@sdi-consulting.com This library
 * is free software; you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software
 * Foundation; either version 2.1 of the License, or (at your option) any later
 * version. This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details. You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.ComplexMapperHelper;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.annotations.JSONExclude;
import com.sdicons.json.annotations.JSONInclude;

public class ObjectMapper implements ComplexMapperHelper
{
	public Class getHelpedClass()
	{
		return Object.class;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sdicons.json.mapper.helper.SimpleMapperHelper#toJava(com.sdicons.json.model.JSONValue,
	 *      java.lang.Class, java.lang.Object)
	 */
	public Object toJava(JSONValue value, Class requestedClass, Object pojo) throws MapperException
	{
		return this.toJava(value, requestedClass, pojo, new Type[0]);
	}

	public Object toJava(JSONValue aValue, Class aRequestedClass, Object aPojo, Type[] types)
	        throws MapperException
	{
		if (!aValue.isObject())
		{
			throw new MapperException("ObjectMapper cannot map: " + aValue.getClass().getName());
		}

		aRequestedClass = getHibernateClassForAnnotations(aRequestedClass, aPojo);

		JSONObject aObject = (JSONObject) aValue;

		// /Make a map of declared types to runtime types
		Map<String, Type> correctTypes = null;
		if (types.length > 0)
		{
			correctTypes = new HashMap<String, Type>();

			TypeVariable[] typeParameters = aRequestedClass.getTypeParameters();
			if (types.length == typeParameters.length) 
			{
				for (int i = 0; i < typeParameters.length; i++)
				{
					TypeVariable tv = typeParameters[i];
					Type t = types[i];
					correctTypes.put(tv.getName(), t);
				}
			}
		}

		try
		{
			if (aPojo == null)
			{
				aPojo = aRequestedClass.newInstance();
			}

			for (String lPropname : aObject.getValue().keySet())
			{
				// Fetch subelement information.
				final JSONValue lSubEl = aObject.get(lPropname);

				// Put the property in the bean.
				boolean lFoundWriter = false;
				PropertyDescriptor[] lPropDesc = Introspector.getBeanInfo(aRequestedClass,
				        Introspector.USE_ALL_BEANINFO).getPropertyDescriptors();

				for (PropertyDescriptor aLPropDesc : lPropDesc)
				{
					if (aLPropDesc.getName().equals(lPropname))
					{
						lFoundWriter = true;
						final Method lWriter = aLPropDesc.getWriteMethod();
						final Method lReader = aLPropDesc.getReadMethod();
						boolean allowWrite = this.SecureToJavaByAnnotations(lReader, lWriter,
						        lPropname, aRequestedClass.getName());
						if (allowWrite)
						{

							Object lProp = null;
							if (lReader != null)
							{
								lProp = lReader.invoke(aPojo);
							}
							Type[] lTypes = lWriter.getGenericParameterTypes();
							if ((lTypes.length == 1) && (lTypes[0] instanceof ParameterizedType))
							{
								ParameterizedType pt = (ParameterizedType) lTypes[0];
								// We can make use of the extra type information
								// of the parameter of the
								// setter. This extra type information can be
								// exploited by the mapper
								// to produce a more fine grained mapping.
								lProp = JSONMapper.toJava(lSubEl, lProp, pt);
							} else
							{
								// The variable wasn't declared as a
								// parameterized type, perhaps because Java
								// doesn't
								// Pass them along when the generics are nested
								// IE: List<KeyValuePair<String, String>>
								// Will come through as
								// List<KeyValuePair<Object, Object>>;
								// We have the proper types stored in the types
								// variable... So, what we'll do is look up the
								// getter, check its return type
								// and then use that to figure out what type the
								// setter is.
								if (correctTypes != null)
								{
									Type[] propTypes = lWriter.getGenericParameterTypes();

									if (propTypes.length == 1) 
									{
										if (propTypes[0] instanceof TypeVariable)
										{
											Type correctType = correctTypes
											        .get(((TypeVariable) propTypes[0]).getName());
											if (correctType instanceof ParameterizedType)
											{
												lProp = JSONMapper.toJava(lSubEl, lProp,
												        (ParameterizedType) correctType);
											} else
											{
												lProp = JSONMapper.toJava(lSubEl, lProp,
												        (Class) correctType);
											}
										}
									}
								} else
								{
									Class clazz = aLPropDesc.getPropertyType();
									lProp = JSONMapper.toJava(lSubEl, lProp, clazz);
								}

							}

							lWriter.invoke(aPojo, lProp);
						}
						break;
					}
				}

				if (!lFoundWriter)
				{
					final String lMsg = "Could not find a setter for prop: " + lPropname +
					        " in class: " + aRequestedClass.getName();
					throw new MapperException(lMsg);
				}
			}
			return aPojo;
		} catch (IllegalAccessException e)
		{
			final String lMsg = "IllegalAccessException while trying to instantiate bean: " +
			        aRequestedClass;
			throw new MapperException(lMsg, e);
		} catch (InstantiationException e)
		{
			final String lMsg = "InstantiationException while trying to instantiate bean: " +
			        aRequestedClass;
			throw new MapperException(lMsg, e);
		} catch (IntrospectionException e)
		{
			final String lMsg = "IntrospectionException while trying to fill bean: " +
			        aRequestedClass;
			throw new MapperException(lMsg, e);
		} catch (InvocationTargetException e)
		{
			final String lMsg = "InvocationTargetException while trying to fill bean: " +
			        aRequestedClass;
			throw new MapperException(lMsg, e.getTargetException());
		}
	}

	private boolean SecureToJavaByAnnotations(Method getterMethod, Method setterMethod,
	        String propName, String className)
	{
		Annotation[] getterAnnos = new Annotation[0];
		Annotation[] setterAnnos = new Annotation[0];

		if (getterMethod != null)
		{
			getterAnnos = getterMethod.getAnnotations();
		}

		if (setterMethod != null)
		{
			setterAnnos = setterMethod.getAnnotations();
		}

		// Excluded means no setting, even if it's on the getter...
		if (this.hasAnnotation(setterAnnos, JSONExclude.class) ||
		        this.hasAnnotation(getterAnnos, JSONExclude.class))
		{
			return false;
		}
		if ((getterMethod != null) && (setterMethod != null))
		{
			return true;
		}
		if ((setterMethod != null) && this.hasAnnotation(setterAnnos, JSONInclude.class))
		{
			return true;
		}
		if (this.hasAnnotation(getterAnnos, JSONInclude.class) && (setterMethod == null))
		{
			return false;
		}

		final String lMsg = "Could not find a setter for prop: " + propName + " in class: " +
		        className;
		throw new MapperException(lMsg);
	}

	public JSONValue toJSON(Object aPojo) throws MapperException
	{
		// We will render the bean properties as the elements of a JSON object.
		final JSONObject lElements = new JSONObject();

		try
		{

			Class lClass = aPojo.getClass();
			lClass = getHibernateClassForAnnotations(lClass, aPojo);

			PropertyDescriptor[] lPropDesc = Introspector.getBeanInfo(lClass,
			        Introspector.USE_ALL_BEANINFO).getPropertyDescriptors();
			for (PropertyDescriptor aLPropDesc : lPropDesc)
			{
				Method lReader = aLPropDesc.getReadMethod();
				Method lWriter = aLPropDesc.getWriteMethod();
				String lPropName = aLPropDesc.getName();

				// Serialize properties that are marked read/write, or
				// read/JSONInclude,
				// but never serialize properties marked JSONExclude on the
				// getter.
				Annotation[] getterAnnotations = new Annotation[0];

				if (lReader != null)
				{
					getterAnnotations = lReader.getAnnotations();
				}

				if (this.hasAnnotation(getterAnnotations, JSONExclude.class))
				{
					continue;
				}
				if (((lReader != null) && this.hasAnnotation(getterAnnotations, JSONInclude.class)) ||
				        ((lReader != null) && (lWriter != null)))
				{
					lElements.getValue().put(lPropName, JSONMapper.toJSON(lReader.invoke(aPojo)));
				}
			}

			return lElements;
		} catch (IntrospectionException e)
		{
			final String lMsg = "Error while introspecting JavaBean.";
			throw new MapperException(lMsg, e);
		} catch (IllegalAccessException e)
		{
			final String lMsg = "Illegal access while trying to fetch a bean property (1).";
			throw new MapperException(lMsg, e);
		} catch (InvocationTargetException e)
		{
			final String lMsg = "Illegal access while trying to fetch a bean property (2).";
			throw new MapperException(lMsg, e);
		} catch (SecurityException ex)
        {
			throw new MapperException("Could not get find method", ex);
        }
	}

	private boolean hasAnnotation(Annotation[] allAnnos, Class targetAnno)
	{
		for (Annotation anno : allAnnos)
		{
			if (anno.annotationType().equals(targetAnno))
			{
				return true;
			}
		}
		return false;
	}

	private Class getHibernateClassForAnnotations(Class clazz, Object aPojo)
	{
		for (Class i : clazz.getInterfaces())
		{
			if (i.getName() == "org.hibernate.proxy.HibernateProxy")
			{
				//Hibernate classes don't have the annotations that are on the
				//actual object.  This gets those annotations back.
				clazz =  (Class) aPojo.getClass().getGenericSuperclass();
			}
		}
		return clazz;
	}

}