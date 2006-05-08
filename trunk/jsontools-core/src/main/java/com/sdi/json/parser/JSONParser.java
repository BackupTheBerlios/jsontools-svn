package com.sdi.json.parser;

/*
    JAJSON - Java JSON Tools
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

import com.sdi.json.parser.impl.*;
import com.sdi.json.model.JSONValue;

import java.io.InputStream;
import java.io.Reader;

import antlr.RecognitionException;
import antlr.TokenStreamException;

/**
 * Reads JSON text and convert it into a Java model for further handling.
 */
public class JSONParser
{
    private com.sdi.json.parser.impl.JSONParser parser;
    private String streamName = "[unknown]";

    /**
     * Construct a parser using a stream.
     * @param aStream A stream containing JSON text.
     */
    public JSONParser(InputStream aStream, String aStreamName)
    {
         JSONLexer lexer = new JSONLexer(aStream);
         parser = new com.sdi.json.parser.impl.JSONParser(lexer);
         if(aStreamName != null) streamName = aStreamName;
    }

    public JSONParser(InputStream aStream)
    {
        this(aStream, null);
    }

    /**
     * Construct a parser using a reader.
     * @param aReader A reader containing JSON text.
     */
    public JSONParser(Reader aReader, String aStreamName)
    {
         JSONLexer lexer = new JSONLexer(aReader);
         parser = new com.sdi.json.parser.impl.JSONParser(lexer);
         if(aStreamName != null) streamName = aStreamName;
    }

    public JSONParser(Reader aReader)
    {
        this(aReader, null);
    }

    /**
     * Read the next JSON structure from the stream and convert it into a
     * Java model.
     * @return    A Java object representing the object in the stream.
     * @throws TokenStreamException A syntax error is encountered.
     * @throws RecognitionException When a token could not be formed.
     */
    public JSONValue nextValue()
    throws TokenStreamException, RecognitionException
    {
        return parser.value(streamName);
    }
}
