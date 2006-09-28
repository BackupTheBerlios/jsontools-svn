package com.sdicons.json.serializer;

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
            Integer lID = new Integer(13);
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
//            Assert.assertEquals(lLitmus.getName(), "SISE Rules!");
//            Assert.assertEquals(lLitmus.getId(), 100);
//
//            // Different physical objects should remain different, even if they
//            // are equal.
//            Assert.assertTrue(!(lLitmus.getInt1() == lLitmus.getInt2()));
        }
        catch(Exception e)
        {
             e.printStackTrace(System.out);
            Assert.fail();
        }
    }
}
