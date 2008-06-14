package com.sdicons.json.model;

/**
 * A json class that allows you to serialize any literal value to JSON without any escaping.
 * Why would you want to do this?  So that you can write JSON with JS functions in it.
 * @author itaylor
 *
 */

public class JSONLiteral extends JSONSimple
{
	String value = null;

	public JSONLiteral() {}

    public JSONLiteral(String value)
    {
    	this.value = value;
    }

    public String getValue()
    {
        return value;
    }

    public String toString()
    {
        return "JSONLiteral(" + getLine() + ":" + getCol() + ")[" + value + "]";
    }

    protected String render(boolean pretty, String indent)
    {
    	String stringValue = "null";
    	if (value.length() > 0)
    	{
    		stringValue = value;
    	}
        if(pretty)
        {
	        return indent + stringValue;
        }
        else
        {
	        return "" + stringValue;
        }
    }

    public boolean equals(Object o)
    {
        if (this == o)
        {
	        return true;
        }
        if (o == null || getClass() != o.getClass())
        {
	        return false;
        }

        final JSONLiteral that = (JSONLiteral) o;

        if (!value.equals(that.value))
        {
	        return false;
        }

        return true;
    }

    public int hashCode()
    {
        return value.hashCode();
    }

    /**
     * Remove all JSON information. In the case of a JSONLiteral this is a string.
     * @return A String representing the JSONLiteral.
     */
    public Object strip()
    {
        return value;
    }

}
