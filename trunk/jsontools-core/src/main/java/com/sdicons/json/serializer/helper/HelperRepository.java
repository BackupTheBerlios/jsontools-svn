package com.sdicons.json.serializer.helper;

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

import com.sdicons.json.serializer.marshall.MarshallException;
import com.sdicons.json.serializer.marshall.JSONMarshall;
import com.sdicons.json.model.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This class groups together the helpers to convert JavaBeans to/from JSON.
 * The repository is used by the Marshaller to find the helper instances.
 */
public class HelperRepository
{
    HelperTreeNode root;

    private static class HelperTreeNode
    {
        private Helper helper;
        private List children;

        public HelperTreeNode(Helper aClass)
        {
            helper = aClass;
            children = new LinkedList();
        }

        public Helper getHelper()
        {
            return helper;
        }

        public boolean insertNode(HelperTreeNode aNode)
        {
            if(aNode.getHelper().getHelpedClass() == helper.getHelpedClass())
            {
                helper = aNode.getHelper();
                return true;
            }
            else if(helper.getHelpedClass().isAssignableFrom(aNode.getHelper().getHelpedClass()))
            {
                boolean insertedToSomeChild = false;
                for (Object aChildren : children)
                {
                    HelperTreeNode lChild = (HelperTreeNode) aChildren;
                    boolean lSuccess = lChild.insertNode(aNode);
                    if (lSuccess)
                    {
                        insertedToSomeChild = true;
                        break;
                    }
                }

                // Add node
                if(!insertedToSomeChild)
                {
                    // Rebalance tree.
                    Iterator lIter2 = children.iterator();
                    while(lIter2.hasNext())
                    {
                        HelperTreeNode lChild = (HelperTreeNode) lIter2.next();
                        if(aNode.getHelper().getHelpedClass().isAssignableFrom(lChild.getHelper().getHelpedClass()))
                        {
                            lIter2.remove();
                            aNode.insertNode(lChild);
                        }
                    }

                    // Add the new balanced tree.
                    children.add(aNode);
                }
                return true;
            }
            else
                return false;
        }

        Helper findHelper(Class aClass)
        {
            if(helper.getHelpedClass() == aClass)
                return helper;
            else
            {
                for (Object aChildren : children)
                {
                    HelperTreeNode lNode = (HelperTreeNode) aChildren;
                    Helper lHelper = lNode.findHelper(aClass);
                    if (lHelper != null) return lHelper;
                }
            }

            if(helper.getHelpedClass().isAssignableFrom(aClass))
                return helper;
            else
                return null;
        }
    }

    private class RootHelper
    implements Helper
    {
        public void renderValue(Object aObj, JSONObject aObjectElement, JSONMarshall aMarshall, HashMap aPool)
        throws MarshallException
        {
        }

        public Object parseValue(JSONObject aObjectElement, JSONMarshall aMarshall, HashMap aPool)
        throws MarshallException
        {
            throw new MarshallException("Not implemented.");
        }

        public Class getHelpedClass()
        {
            return Object.class;
        }
    }

    public HelperRepository()
    {
        root = new HelperTreeNode(new RootHelper());
    }

    /**
     * Add a helper to the repository.
     * @param aHelper
     */
    public void addHelper(Helper aHelper)
    {
        root.insertNode(new HelperTreeNode(aHelper));
    }

    /**
     * Lookup a helper in the repository.
     * @param aClass The class for which a helper is wanted.
     * @return The corresponding helper. There is always a general fallback helper which uses introspection to
     *         serialize the properties of a JavaBean. This property helper is always returned as a last possibility.
     *         So this method always returns a helper.
     */
    public Helper findHelper(Class aClass)
    {
        return root.findHelper(aClass);
    }
}