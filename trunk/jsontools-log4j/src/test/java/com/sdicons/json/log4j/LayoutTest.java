package com.sdicons.json.log4j;

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
import org.apache.log4j.*;

import java.io.File;
import java.io.IOException;


public class LayoutTest
extends TestCase
{
    public void testLayout()
    {
        File lTempLog = null;

        try
        {
            lTempLog = File.createTempFile("json-log-test", ".txt");
            lTempLog.createNewFile();

            final Logger lLog = Logger.getLogger(LayoutTest.class);

            final Layout lLayout = new JSONLayout();
            final FileAppender lAppender = new FileAppender();
            lAppender.setFile(lTempLog.getAbsolutePath());
            lAppender.setLayout(lLayout);
            lAppender.activateOptions();

            final Logger lRootLogger = Logger.getRootLogger();
            lRootLogger.addAppender(new ConsoleAppender(lLayout));
            lRootLogger.addAppender(lAppender);

            lLog.info("This is an info message.");
            lLog.debug("This is a debug message.");
            lLog.error("This is an error message.");
            lLog.fatal("This is a fatal message.", new IllegalArgumentException("This is an illegal argument."));

            for(int i = 0; i < 10; i++)
            {
                new Thread(new Runnable()
                {
                    public void run()
                    {
                        lLog.info("This is an info message from a thread.");
                        lLog.debug("This is a debug message from a thread.");
                        lLog.error("This is an error message from a tread.");
                    }
                }).start();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(lTempLog != null) lTempLog.delete();
        }
    }
}
