/**
 *
 */
package com.sdicons.json.helper;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author itaylor
 *
 * A simple implementation of supertypetokens...
 *
 * This allows you to get the parameterized type of a nested class
 *
 */

public abstract class TypeRef<T> {

	private final Type type;
	private volatile Constructor<?> constructor;

	protected TypeRef() {
	Type superclass = getClass().getGenericSuperclass();
	if (superclass instanceof Class) {
	throw new RuntimeException("Missing type parameter.");
	}
	this.type = ( (ParameterizedType)superclass ).getActualTypeArguments()[0];
	}

	public Type getType() {
	return this.type;
	}

	@Override
    public boolean equals(Object o) {
	return ( o instanceof TypeRef ) ? ( (TypeRef)o ).type.equals(this.type)
	: false;
	}

	@Override
    public int hashCode() {
	return this.type.hashCode();
	}
}