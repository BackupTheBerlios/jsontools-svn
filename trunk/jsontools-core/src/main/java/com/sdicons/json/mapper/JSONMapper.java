package com.sdicons.json.mapper;

import com.sdicons.json.helper.HelperRepository;
import com.sdicons.json.mapper.helper.MapperHelper;
import com.sdicons.json.mapper.helper.impl.*;
import com.sdicons.json.model.JSONNull;
import com.sdicons.json.model.JSONValue;

public class JSONMapper
{
    private static HelperRepository<MapperHelper> repo = new HelperRepository<MapperHelper>();

    static
    {
        repo.addHelper(new ObjectMapper());
        repo.addHelper(new StringMapper());
        repo.addHelper(new BooleanMapper());
        repo.addHelper(new ByteMapper());
        repo.addHelper(new ShortMapper());
        repo.addHelper(new IntegerMapper());
        repo.addHelper(new LongMapper());
        repo.addHelper(new FloatMapper());
        repo.addHelper(new DoubleMapper());
//        repo.addHelper(new BigIntegerHelper());
//        repo.addHelper(new BigDecimalHelper());
//        repo.addHelper(new CharacterHelper());
//        repo.addHelper(new DateHelper());
//        repo.addHelper(new CollectionHelper());
//        repo.addHelper(new MapHelper());
//        repo.addHelper(new ColorHelper());
//        repo.addHelper(new FontHelper());
//        repo.addHelper(new EnumHelper());
    }

    public static Object toJava(JSONValue aValue, Class aPojoClass)
    throws MapperException
    {
        if(aValue == null)
        {
            final String lMsg = "Mapper does not support null values.";
            throw new MapperException(lMsg);
        }
        else if(aValue.isNull()) return null;

        final MapperHelper lHelper = repo.findHelper(aPojoClass);

        if(lHelper == null)
        {
            final String lMsg = "Could not find a mapper helper for class: " + aPojoClass.getName();
            throw new MapperException(lMsg);
        }

        return lHelper.toJava(aValue, aPojoClass);
    }

    public static JSONValue toJSON(Object aPojo)
    throws MapperException
    {
        if(aPojo == null) return JSONNull.NULL;

        final MapperHelper lHelper = repo.findHelper(aPojo.getClass());

        if(lHelper == null)
        {
            final String lMsg = "Could not find a mapper helper for class: " + aPojo.getClass().getName();
            throw new MapperException(lMsg);
        }

        return lHelper.toJSON(aPojo);
    }
}
