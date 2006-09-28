package com.sdicons.json.mapper;

public class MapperException
extends Exception
{
    public MapperException()
    {
    }

    public MapperException(Throwable cause)
    {
        super(cause);
    }

    public MapperException(String message)
    {
        super(message);
    }

    public MapperException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
