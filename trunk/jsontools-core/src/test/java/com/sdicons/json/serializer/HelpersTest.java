package com.sdicons.json.serializer;

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

import junit.framework.*;
import java.util.Date;

import com.sdicons.json.serializer.marshall.Marshall;
import com.sdicons.json.serializer.marshall.MarshallValue;
import com.sdicons.json.serializer.marshall.JSONMarshall;

public class HelpersTest
extends TestCase
{

    public HelpersTest(String lName)
    {
        super(lName);
    }

    Marshall marshall;

    public void setUp()
    throws Exception
    {
        marshall = new JSONMarshall();
    }

    public void testByte()
    {
        try
        {
            Byte lByte = new Byte((byte) 5);
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(lByte));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            Byte lByte2 = (Byte) lResult.getReference();
            Assert.assertTrue(lByte2.equals(lByte));
        }
        catch(Exception e)
        {
            Assert.fail();
        }

    }

    public void testShort()
    {
        try
        {
            Short lShort = new Short((short) 3);
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(lShort));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            Short lShort2 = (Short) lResult.getReference();
            Assert.assertTrue(lShort2.equals(lShort));
        }
        catch(Exception e)
        {
            Assert.fail();
        }

    }

    public void testChar()
    {
        try
        {
            Character lchar = new Character('x');
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(lchar));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            Character lChar2 = (Character) lResult.getReference();
            Assert.assertTrue(lChar2.equals(lchar));
        }
        catch(Exception e)
        {
            Assert.fail();
        }

    }

    public void testInteger()
    {
        try
        {
            Integer lInt = new Integer(17);
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(lInt));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            Integer lInt2 = (Integer) lResult.getReference();
            Assert.assertTrue(lInt2.equals(lInt));
        }
        catch(Exception e)
        {
            Assert.fail();
        }
    }

    public void testLong()
    {
        try
        {
            Long lLong = new Long(21);
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(lLong));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            Long lLong2 = (Long) lResult.getReference();
            Assert.assertTrue(lLong2.equals(lLong));
        }
        catch(Exception e)
        {
            Assert.fail();
        }
    }

    public void testFloat()
    {
        try
        {
            Float lFloat = new Float(21.331);
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(lFloat));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            Float lFloat2 = (Float) lResult.getReference();
            Assert.assertTrue(lFloat2.equals(lFloat));
        }
        catch(Exception e)
        {
            Assert.fail();
        }
    }

    public void testDouble()
    {
        try
        {
            Double lDouble = new Double(21.156331);
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(lDouble));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            Double lDouble2 = (Double) lResult.getReference();
            Assert.assertTrue(lDouble2.equals(lDouble));
        }
        catch(Exception e)
        {
            Assert.fail();
        }
    }

    public void testDate()
    {
        try
        {
            final Date lDate = new Date();
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(lDate));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            final Date lDate2 = (Date) lResult.getReference();
            Assert.assertTrue(lDate2.getTime() == lDate.getTime());
        }
        catch(Exception e)
        {
            Assert.fail();
        }
    }
}
