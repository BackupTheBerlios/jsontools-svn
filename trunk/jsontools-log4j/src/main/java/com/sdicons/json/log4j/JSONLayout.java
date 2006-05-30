package com.sdicons.json.log4j;

import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

public class JSONLayout
extends Layout
{
    public java.lang.String format(LoggingEvent loggingEvent)
    {
        return null;
    }

    public void activateOptions()
    {

    }

    public boolean ignoresThrowable()
    {
        return false;  
    }
}
