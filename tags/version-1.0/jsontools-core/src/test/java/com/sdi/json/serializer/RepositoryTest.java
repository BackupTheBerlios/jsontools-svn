package com.sdi.json.serializer;

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

import java.util.*;

import com.sdi.json.serializer.helper.impl.*;
import com.sdi.json.serializer.helper.Helper;
import com.sdi.json.serializer.helper.HelperRepository;
import com.sdi.json.serializer.marshall.MarshallException;
import com.sdi.json.serializer.marshall.JSONMarshall;
import com.sdi.json.model.JSONObject;

public class RepositoryTest
extends TestCase
{
    public RepositoryTest(String lName)
    {
        super(lName);
    }

    HelperRepository repo;

    public void setUp()
    throws Exception
    {
        repo = new HelperRepository();

        repo.addHelper(new ObjectHelper());
        repo.addHelper(new StringHelper());
        repo.addHelper(new BooleanHelper());
        repo.addHelper(new ByteHelper());
        repo.addHelper(new ShortHelper());
        repo.addHelper(new IntegerHelper());
        repo.addHelper(new LongHelper());
        repo.addHelper(new FloatHelper());
        repo.addHelper(new DoubleHelper());
        repo.addHelper(new CharacterHelper());
        repo.addHelper(new DateHelper());
    }

    public void testBasicHelpers()
    {
        {
            Class lClass = String.class;
            Helper lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }

        {
            Class lClass = Boolean.class;
            Helper lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class lClass = Byte.class;
            Helper lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class lClass = Short.class;
            Helper lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class lClass = Integer.class;
            Helper lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class lClass = Short.class;
            Helper lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class lClass = Long.class;
            Helper lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class lClass = Float.class;
            Helper lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class lClass = Double.class;
            Helper lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class lClass = Character.class;
            Helper lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
        {
            Class lClass = String.class;
            Helper lHelper = repo.findHelper(lClass);
            Assert.assertEquals(lClass, lHelper.getHelpedClass());
        }
    }

    public void testInheritance()
    {
        class A
        {
        }

        class B
        extends A
        {
        }

        class C
        extends A
        {
        }

        class AHelper
        implements Helper
        {
            public void renderValue(Object aObj, JSONObject aObjectElement, JSONMarshall aMarshall, HashMap aPool)
            throws MarshallException
            {
            }

            public Object parseValue(JSONObject aObjectElement, JSONMarshall aMarshall, HashMap aPool)
            throws MarshallException
            {
                return null;
            }

            public Class getHelpedClass()
            {
                return A.class;
            }

        }

        class BHelper
        implements Helper
        {
            public void renderValue(Object aObj, JSONObject aObjectElement, JSONMarshall aMarshall, HashMap aPool)
            throws MarshallException
            {
            }

            public Object parseValue(JSONObject aObjectElement, JSONMarshall aMarshall, HashMap aPool)
            throws MarshallException
            {
                return null;
            }

            public Class getHelpedClass()
            {
                return B.class;
            }
        }

        class CHelper
        implements Helper
        {
            public void renderValue(Object aObj, JSONObject aObjectElement, JSONMarshall aMarshall, HashMap aPool)
            throws MarshallException
            {
            }

            public Object parseValue(JSONObject aObjectElement, JSONMarshall aMarshall, HashMap aPool)
            throws MarshallException
            {
                return null;
            }

            public Class getHelpedClass()
            {
                return C.class;
            }

        }

        repo.addHelper(new CHelper()); // C first.
        repo.addHelper(new BHelper()); // B first.
        repo.addHelper(new AHelper()); // C and B will be part of rebalancing.

        Helper lH1 = repo.findHelper(B.class);
        Assert.assertEquals(B.class, lH1.getHelpedClass());

        Helper lH2 = repo.findHelper(A.class);
        Assert.assertEquals(A.class, lH2.getHelpedClass());

        Helper lH3 = repo.findHelper(C.class);
        Assert.assertEquals(C.class, lH3.getHelpedClass());
    }

}
