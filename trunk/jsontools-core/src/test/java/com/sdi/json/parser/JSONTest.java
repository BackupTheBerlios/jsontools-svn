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

import junit.framework.TestCase;
import com.sdi.json.parser.JSONParser;
import com.sdi.json.model.JSONValue;
import com.sdi.json.model.JSONArray;
import com.sdi.json.model.JSONString;

import java.util.Iterator;

import antlr.TokenStreamException;
import antlr.RecognitionException;

public class JSONTest
extends TestCase
{
    public void testExamples()
    {
        try
        {
            final JSONParser lParser = new JSONParser(JSONTest.class.getResourceAsStream("/data/config.json"));
            final JSONValue lConfig = lParser.nextValue();
            assertTrue(lConfig.isArray());
            final JSONArray lConfigArray = (JSONArray) lConfig;
            final Iterator<JSONValue> lIter = lConfigArray.getValue().iterator();

            while (lIter.hasNext())
            {
                final JSONValue lEntry = lIter.next();
                TestCase.assertTrue(lEntry.isString());
                final JSONString lEntryResource = (JSONString) lEntry;

                try
                {
                    System.out.println(" --------- " + lEntryResource.getValue());
                    final JSONParser lExampleParser = new JSONParser(JSONTest.class.getResourceAsStream(lEntryResource.getValue()));
                    JSONValue lExampleVal = lExampleParser.nextValue();
                    System.out.println(lExampleVal.render(true));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    TestCase.fail(e.getMessage());
                }
            }
        }
        catch (TokenStreamException e)
        {
            e.printStackTrace();
            TestCase.fail(e.getMessage());
        }
        catch (RecognitionException e)
        {
           TestCase.fail(e.getMessage());
        }
    }

}

