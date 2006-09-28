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

import junit.framework.TestCase;
import junit.framework.Assert;
import com.sdicons.json.serializer.marshall.Marshall;
import com.sdicons.json.serializer.marshall.JSONMarshall;
import com.sdicons.json.serializer.marshall.MarshallValue;
import com.sdicons.json.model.JSONObject;

public class UsersJavaBeanTest
extends TestCase
{
    public static class Transportable
    {
        private String param1;
        private String param2;
        private Integer objectID;
        private Integer parentID;
        private Integer subObjectID;
        private Integer transportCode;
        private Integer eventType;

        public Integer getEventType()
        {
            return eventType;
        }

        public void setEventType(Integer eventType)
        {
            this.eventType = eventType;
        }

        public Integer getObjectID()
        {
            return objectID;
        }

        public void setObjectID(Integer objectID)
        {
            this.objectID = objectID;
        }

        public String getParam1()
        {
            return param1;
        }

        public void setParam1(String param1)
        {
            this.param1 = param1;
        }

        public String getParam2()
        {
            return param2;
        }

        public void setParam2(String param2)
        {
            this.param2 = param2;
        }

        public Integer getParentID()
        {
            return parentID;
        }

        public void setParentID(Integer parentID)
        {
            this.parentID = parentID;
        }

        public Integer getSubObjectID()
        {
            return subObjectID;
        }

        public void setSubObjectID(Integer subObjectID)
        {
            this.subObjectID = subObjectID;
        }

        public Integer getTransportCode()
        {
            return transportCode;
        }

        public void setTransportCode(Integer transportCode)
        {
            this.transportCode = transportCode;
        }
    }

    public UsersJavaBeanTest(String lName)
    {
        super(lName);
    }

    Marshall marshall;

    public void setUp()
    throws Exception
    {
        marshall = new JSONMarshall();
    }

    public void testIt()
    {
        try
        {
            Transportable lEvil = new Transportable();
            Integer lID = 13;
            lEvil.setEventType(lID);
            lEvil.setParentID(lID);
            lEvil.setSubObjectID(lID);
            lEvil.setObjectID(lID);
            String lStr = "Test";
            lEvil.setParam1(lStr);
            lEvil.setParam2(lStr);

            JSONObject lObj = marshall.marshall(lEvil);
            System.out.println(lObj.render(true));
            MarshallValue lResult = marshall.unmarshall(lObj);
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            Transportable lLitmus = (Transportable) lResult.getReference();

            // Test if the contents are intact.
            Assert.assertNotNull(lLitmus);
            Assert.assertEquals(lLitmus.getEventType(), lID);
            Assert.assertSame(lLitmus.getEventType(), lLitmus.getParentID());
            Assert.assertSame(lLitmus.getEventType(), lLitmus.getSubObjectID());
            Assert.assertSame(lLitmus.getEventType(), lLitmus.getObjectID());
            Assert.assertEquals(lLitmus.getParam1(), lStr);
            Assert.assertSame(lLitmus.getParam1(), lLitmus.getParam2());
        }
        catch(Exception e)
        {
            e.printStackTrace(System.out);
            Assert.fail();
        }
    }
}
