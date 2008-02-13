/**
 *
 */
package com.sdicons.json.genericwrapper;

import java.io.Serializable;

/**
 * @author itaylor
 */
public class KeyValuePair<K, V> implements Serializable
{

	private static final long serialVersionUID = 1L;

	private K Key;

	private V Value;

	/**
	 * @param key
	 * @param value
	 */
	public KeyValuePair(K key, V value)
	{
		super();
		this.Key = key;
		this.Value = value;
	}

	/**
	 *
	 */
	public KeyValuePair()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the key
	 */
	public K getKey()
	{
		return this.Key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(K key)
	{
		this.Key = key;
	}

	/**
	 * @return the value
	 */
	public V getValue()
	{
		return this.Value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(V value)
	{
		this.Value = value;
	}

	private boolean nullSafeEquals(Object obj1, Object obj2)
	{
		if (obj1 == null)
		{
			if (obj2 != null)
			{
				return false;
			}
		}
		if (obj2 == null)
		{
			if (obj1 != null)
			{
				return false;
			}
		}
		return obj1.equals(obj2);
	}

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof KeyValuePair)
		{
			KeyValuePair obj = (KeyValuePair) other;
			Object myKey = this.getKey();
			Object otherKey = obj.getKey();
			Object myValue = this.getValue();
			Object otherValue = obj.getValue();

			return this.nullSafeEquals(myKey, otherKey) && this.nullSafeEquals(myValue, otherValue);
		}
		return false;
	}

}
