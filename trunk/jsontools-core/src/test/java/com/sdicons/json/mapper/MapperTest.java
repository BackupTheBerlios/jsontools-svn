package com.sdicons.json.mapper;

import com.sdicons.json.model.JSONValue;
import junit.framework.Assert;
import junit.framework.TestCase;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class MapperTest
extends TestCase
{
    public static class TestBean
    {
        private Boolean booleanMbr;
        private String stringMbr;
        private Integer integerMbr;
        private int intMbr;

        private Short shortMbr;
        private Byte byteMbr;
        private Long longMbr;
        private Float floatMbr;
        private Double doubleMbr;
        private BigInteger bigIntMbr;
        private BigDecimal bigDecimalMbr;
        private Character charMbr;
        private Date date;

        private LinkedList<String> linkedList;
        public ArrayList<Date> arrayList;

        public Integer getIntegerMbr()
        {
            return integerMbr;
        }

        public void setIntegerMbr(Integer integerMbr)
        {
            this.integerMbr = integerMbr;
        }

        public Short getShortMbr()
        {
            return shortMbr;
        }

        public void setShortMbr(Short shortMbr)
        {
            this.shortMbr = shortMbr;
        }

        public String getStringMbr()
        {
            return stringMbr;
        }

        public void setStringMbr(String stringMbr)
        {
            this.stringMbr = stringMbr;
        }

        public Boolean getBooleanMbr()
        {
            return booleanMbr;
        }

        public void setBooleanMbr(Boolean booleanMbr)
        {
            this.booleanMbr = booleanMbr;
        }

        public Byte getByteMbr()
        {
            return byteMbr;
        }

        public void setByteMbr(Byte byteMbr)
        {
            this.byteMbr = byteMbr;
        }

        public long getLongMbr()
        {
            return longMbr;
        }

        public void setLongMbr(Long longMbr)
        {
            this.longMbr = longMbr;
        }

        public Double getDoubleMbr()
        {
            return doubleMbr;
        }

        public void setDoubleMbr(Double doubleMbr)
        {
            this.doubleMbr = doubleMbr;
        }

        public Float getFloatMbr()
        {
            return floatMbr;
        }

        public void setFloatMbr(Float floatMbr)
        {
            this.floatMbr = floatMbr;
        }

        public BigDecimal getBigDecimalMbr()
        {
            return bigDecimalMbr;
        }

        public void setBigDecimalMbr(BigDecimal bigDecimalMbr)
        {
            this.bigDecimalMbr = bigDecimalMbr;
        }

        public BigInteger getBigIntMbr()
        {
            return bigIntMbr;
        }

        public void setBigIntMbr(BigInteger bigIntMbr)
        {
            this.bigIntMbr = bigIntMbr;
        }

        public Character getCharMbr()
        {
            return charMbr;
        }

        public void setCharMbr(Character charMbr)
        {
            this.charMbr = charMbr;
        }

        public int getIntMbr()
        {
            return intMbr;
        }

        public void setIntMbr(int intMbr)
        {
            this.intMbr = intMbr;
        }

        public Date getDate()
        {
            return date;
        }

        public void setDate(Date date)
        {
            this.date = date;
        }


        public ArrayList<Date> getArrayList()
        {
            return arrayList;
        }

        public void setArrayList(ArrayList<Date> lArrayList)
        {
            this.arrayList = lArrayList;
        }

        public LinkedList<String> getLinkedList()
        {
            return linkedList;
        }

        public void setLinkedList(LinkedList<String> lLinkedList)
        {
            this.linkedList = lLinkedList;
        }
    }

    public MapperTest(String lName)
    {
        super(lName);
    }

    public void testIt()
    {
        try
        {
            MapperTest.TestBean lDuupje = new MapperTest.TestBean();
            lDuupje.setIntegerMbr(new Integer(13));
            lDuupje.setIntMbr(17);
            lDuupje.setShortMbr(new Short((short) 17));
            lDuupje.setStringMbr("It is not fair!");
            lDuupje.setBooleanMbr(true);
            lDuupje.setByteMbr((byte) 32);
            lDuupje.setLongMbr(12345l);
            lDuupje.setFloatMbr(123.12f);
            lDuupje.setDoubleMbr(987.89);
            lDuupje.setBigIntMbr(new BigInteger("123654789555"));
            lDuupje.setBigDecimalMbr(new BigDecimal("1111111465465.676476545"));
            lDuupje.setCharMbr('A');
            lDuupje.setDate(new Date());
                        
            LinkedList lLinkedList = new LinkedList();
            lLinkedList.add("uno");
            lLinkedList.add("duo");
            lDuupje.setLinkedList(lLinkedList);

            ArrayList<Date> lArrayList = new ArrayList<Date>();
            lArrayList.add(new Date());
            lArrayList.add(new Date());
            lDuupje.setArrayList(lArrayList);

            JSONValue lObj = JSONMapper.toJSON(lDuupje);
            System.out.println(lObj.render(true));

            MapperTest.TestBean lLitmus = (MapperTest.TestBean) JSONMapper.toJava(lObj, TestBean.class);
            Assert.assertNotNull(lLitmus);
        }
        catch(Exception e)
        {
            e.printStackTrace(System.out);
            Assert.fail();
        }
    }
}
