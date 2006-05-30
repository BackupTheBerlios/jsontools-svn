package com.sdicons.json.model;

/*
    JSONTools - Java JSON Tools
    Copyright (C) 2006 S.D.I.-Consulting BVBA
    http://www.sdi-consulting.com
    mailto://nospam@sdi-consulting.com

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/

/**
 * Base class for all JSON representations.
 */
public abstract class JSONValue
{
    private String streamName;
    private int line = 0;
    private int col = 0;
    private Object data = null;

    /**
     * Get the line number in the textual representation where this JSON value was encountered.
     * Can be handy for post processing tools which want to give an indication tot he user
     * where in the representation some condition occured.
     * @return   The line number.
     */
    public int getLine()
    {
        return line;
    }

    /**
     * Set The position where this JSON element occured during parsing. This method is called
     * by the parser. Probably no need to call this yourself.
     * @param line
     * @param col
     */
    public void setLineCol(int line, int col)
    {
        this.line = line;
        this.col = col;
    }

    /**
     * Get information about the stream in which the value occured.
     * Its purpose is to identify in which stream the error occured if multiple streams are being parsed
     * and the error reports are being collected in a single report.
     * @return The name of the stream.
     */
    public String getStreamName()
    {
        return streamName;
    }

    /**
     * Fill in informatio about the stream.
     * @param streamName
     */
    public void setStreamName(String streamName)
    {
        this.streamName = streamName;
    }

    /**
     * Get the column number in the textual representation where this JSON value was encountered.
     * Can be handy for post processing tools which want to give an indication tot he user
     * where in the representation some condition occured.
     * @return   The line number.
     */
    public int getCol()
    {
        return col;
    }

    /**
     * Get user data.
     * @return  The user data.
     */
    public Object getData()
    {
        return data;
    }

    /**
     * Set user data. The user of the library can link whatever information is usefull in the
     * user context to a JSON object in order to track back to the original JSON data.
     * The JSON tools do not use this field, it is for the user of the library.
     * @param data
     */
    public void setData(Object data)
    {
        this.data = data;
    }

    public boolean isSimple()
    {
        return this instanceof JSONSimple;
    }

    public boolean isComplex()
    {
        return this instanceof JSONComplex;
    }

    public boolean isArray()
    {
        return this instanceof JSONArray;
    }

    public boolean isObject()
    {
        return this instanceof JSONObject;
    }

    public boolean isNumber()
    {
        return this instanceof JSONNumber;
    }

    public boolean isDecimal()
    {
        return this instanceof JSONDecimal;
    }

    public boolean isInteger()
    {
        return this instanceof JSONInteger;
    }

    public boolean isNull()
    {
        return this instanceof JSONNull;
    }

    public boolean isBoolean()
    {
        return this instanceof JSONBoolean;
    }
    public boolean isString()
    {
        return this instanceof JSONString;
    }

    public String render(boolean pretty)
    {
        return render(pretty, "");
    }

    /**
     * Convert the JSON value into a string representation (JSON representation).
     * @param pretty Indicating if the print should be made pretty (human readers) or compact (transmission).
     * @param indent Starting indent.
     * @return A JSON representation.
     */
    abstract String render(boolean pretty, String indent);
}
