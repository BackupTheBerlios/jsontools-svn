package com.sdicons.json.helper;

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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class HelperRepository<T extends Helper>
{
    private HelperTreeNode<T> root;

    private static class HelperTreeNode<T extends Helper>
    {
        private T helper;
        private List<HelperTreeNode<T>> children;

        public HelperTreeNode(T aClass)
        {
            helper = aClass;
            children = new LinkedList<HelperTreeNode<T>>();
        }

        public T getHelper()
        {
            return helper;
        }

        public boolean insertNode(HelperTreeNode<T> aNode)
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
                    HelperTreeNode<T> lChild = (HelperTreeNode<T>) aChildren;
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
                        HelperTreeNode<T> lChild = (HelperTreeNode<T>) lIter2.next();
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

        T findHelper(Class aClass)
        {
            if(helper.getHelpedClass() == aClass)
                return helper;
            else
            {
                for (HelperTreeNode<T> lChildNode : children)
                {
                    T lHelper = lChildNode.findHelper(aClass);
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
    public void addHelper(T aHelper)
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
    public T findHelper(Class aClass)
    {
        return root.findHelper(aClass);
    }
}
