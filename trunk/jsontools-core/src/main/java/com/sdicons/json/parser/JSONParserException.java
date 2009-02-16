package com.sdicons.json.parser;

/*
    JSONTools - Java JSON Tools
    Copyright (C) 2006-2008 S.D.I.-Consulting BVBA
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

public class JSONParserException
extends Exception
{
    private int line = -1;
    private int column = -1;
    private String streamName = "[unknown stream]";

    public JSONParserException()
    {
        super();
    }

    public JSONParserException(String aMsg)
    {
        super(aMsg);
    }

    public JSONParserException(String aStreamName, int aLine, int aCol, String aMsg)
    {
        super(aMsg);
        line = aLine;
        column = aCol;
        streamName = aStreamName;
    }

    public int getLine()
    {
        return line;
    }

    public int getColumn()
    {
        return column;
    }

    public String getStreamName()
    {
        return streamName;
    }

    @Override
    public String getMessage()
    {
        final StringBuilder lBuilder = new StringBuilder();
        lBuilder.append("Error in stream: \"").append(streamName).append("\" in (line/col): (").append(line).append("/").append(column).append(") with message: \n").append(super.getMessage());
        return lBuilder.toString();
    }
}
