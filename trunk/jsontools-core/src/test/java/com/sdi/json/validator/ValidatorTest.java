package com.sdi.json.validator;

import junit.framework.TestCase;
import com.sdi.json.parser.JSONParser;
import com.sdi.json.model.JSONValue;
import com.sdi.json.model.JSONArray;
import com.sdi.json.model.JSONObject;
import com.sdi.json.model.JSONString;
import com.sdi.json.validator.JSONValidator;

import antlr.TokenStreamException;
import antlr.RecognitionException;

public class ValidatorTest
extends TestCase
{
    public void testSimple()
    {
        try
        {
            // This is a good illustration of the use of a validator.
            // The code is kept as simple and compelete as possible.
            final JSONParser lParser = new JSONParser(ValidatorTest.class.getResourceAsStream("/rules/simple.json"));
            final Validator lChecker = new JSONValidator((JSONObject) lParser.nextValue());
            lChecker.validate(new JSONObject());                  
        }
        catch (TokenStreamException e)
        {
            e.printStackTrace();
            TestCase.fail(e.getMessage());
        }
        catch (RecognitionException e)
        {
            TestCase.fail(e.getMessage());
        }
        catch (ValidationException e)
        {
            e.printStackTrace();
            TestCase.fail(e.getMessage());
        }
    }

    public void testValidations()
    {
        try
        {
            final JSONParser lParser1 = new JSONParser(ValidatorTest.class.getResourceAsStream("/rules/validator-validator.json"));
            final JSONObject lValidatorObject = (JSONObject) lParser1.nextValue();
            final Validator lValidatorValidator = new JSONValidator(lValidatorObject);

            final JSONParser lParser = new JSONParser(ValidatorTest.class.getResourceAsStream("/rules/config.json"));
            final JSONValue lConfig = lParser.nextValue();

            final JSONArray lConfigArray = (JSONArray) lConfig;

            for (final JSONValue lEntry : lConfigArray.getValue())
            {
                final JSONString lEntryResource = (JSONString) lEntry;

                try
                {
                    System.out.println(" --------- " + lEntryResource.getValue());
                    testCase(lEntryResource.getValue(), lValidatorValidator);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    TestCase.fail(e.getMessage());
                }
            }
        }
        catch (TokenStreamException e)
        {
            e.printStackTrace();
            TestCase.fail(e.getMessage());
        }
        catch (RecognitionException e)
        {
            e.printStackTrace();
            TestCase.fail(e.getMessage());
        }
        catch (ValidationException e)
        {
            e.printStackTrace();
            TestCase.fail(e.getMessage());
        }
    }


    private void testCase(String aResource, Validator aValidatorValidator)
    {
        try
        {
            final JSONParser lParser = new JSONParser(ValidatorTest.class.getResourceAsStream(aResource));
            final JSONObject lTestData = (JSONObject) lParser.nextValue();

            final JSONObject lCheckerObject = (JSONObject) lTestData.get("validator");

            // Note that in practice it is not necessary to validate a validator. The validtor code
            // is independent of the validator mechanism, otherwise we would have a chicken-egg problem.
            // The check is here to test the validator-validator definition.
            aValidatorValidator.validate(lCheckerObject);

            final Validator lChecker = new JSONValidator(lCheckerObject);

            final JSONArray lGoods = (JSONArray) lTestData.get("good");
            final JSONArray lBads = (JSONArray) lTestData.get("bad");

            for (JSONValue jsonValue : lGoods.getValue())
            {
                JSONValue lVal = (JSONValue) jsonValue;
                try
                {
                    lChecker.validate(lVal);
                }
                catch (ValidationException e)
                {
                    // Failure if exception.
                    e.printStackTrace(System.out);
                    TestCase.fail("Should have succeeded: " + lVal.toString());
                }
            }

            for (JSONValue jsonValue1 : lBads.getValue())
            {
                JSONValue lVal = (JSONValue) jsonValue1;
                try
                {
                    lChecker.validate(lVal);
                    // Failure if success.
                    TestCase.fail("Should have failed: " + lVal.toString());
                }
                catch (ValidationException eIgnore)
                {
                }
            }
        }
        catch (TokenStreamException e)
        {
            e.printStackTrace();
            TestCase.fail(e.getMessage());
        }
        catch (RecognitionException e)
        {
            TestCase.fail(e.getMessage());
        }
        catch (ValidationException e)
        {
            e.printStackTrace();
            TestCase.fail(e.getMessage());
        }
    }
}
