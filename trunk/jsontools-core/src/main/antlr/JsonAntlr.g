grammar JsonAntlr;

options
{
	language=Java;
	output=AST;
	//charVocabulary='\u0000'..'\uFFFE';
	k=2;
}

@lexer::header
{
package com.sdicons.json.parser.impl;
}

@header
{
package com.sdicons.json.parser.impl;
import com.sdicons.json.model.*;
import java.math.BigDecimal;
import java.math.BigInteger;
}

@rulecatch 
{
	catch (RecognitionException e) 
	{
	  throw e;
	}
}

values[String aStreamName] : value[aStreamName]* EOF!;

value[String aStreamName] returns [JSONValue val=JSONNull.NULL] :
     obj=object[aStreamName] {$val = obj.result;}
   | arr=array[aStreamName] {$val = arr.result;}
   | ato=atomic[aStreamName] {$val = ato.val;}
   ;

atomic[String aStreamName] returns [JSONValue val=JSONNull.NULL] :
     t=TRUE         {$val = new JSONBoolean(true); $val.setLineCol($t.getLine(), $t.getCharPositionInLine()); $val.setStreamName(aStreamName);}
   | f=FALSE        {$val = new JSONBoolean(false);$val.setLineCol($f.getLine(), $f.getCharPositionInLine()); $val.setStreamName(aStreamName);}
   | n=NULL         {$val.setLineCol($n.getLine(), $n.getCharPositionInLine()); $val.setStreamName(aStreamName);}
   | str=STRING     {$val = new JSONString($str.getText());$val.setLineCol($str.getLine(), $str.getCharPositionInLine()); $val.setStreamName(aStreamName);}
   | num=NUMBER     { String lTxt = $num.getText().toLowerCase();
                      if(lTxt.indexOf('e')>=0 || lTxt.indexOf('.')>=0) $val = new JSONDecimal(new BigDecimal(lTxt));
                      else $val = new JSONInteger(new BigInteger(lTxt));
                      $val.setLineCol($num.getLine(), $num.getCharPositionInLine());
                      $val.setStreamName(aStreamName); }
   ;

object[String aStreamName] returns [JSONObject result= new JSONObject()] :
   begin=LCURLY                                {$result.setLineCol(begin.getLine(), begin.getCharPositionInLine()); $result.setStreamName(aStreamName);}
   (
        i=STRING COLON lVal=value[aStreamName]              {$result.getValue().put(i.getText(), $lVal.val);}
        (
            COMMA j=STRING COLON lVal=value[aStreamName]    {$result.getValue().put(j.getText(), $lVal.val);}
        )*
   )?
   RCURLY
   ;

array[String aStreamName]  returns [JSONArray result=new JSONArray()] :
   begin=LBRACK                                 {$result.setLineCol(begin.getLine(), begin.getCharPositionInLine()); $result.setStreamName(aStreamName);}
   (
        lVal=value[aStreamName]                              {$result.getValue().add($lVal.val);}
        (
            COMMA lVal=value[aStreamName]                    {$result.getValue().add($lVal.val);}
        )*
   )?
   RBRACK
   ;

LPAREN: '(' ;
RPAREN: ')' ;
LCURLY: '{' ;
RCURLY: '}' ;
LBRACK: '[' ;
RBRACK: ']' ;
COMMA:  ',' ;
QUOTES: '"' ;
COLON:  ':' ;
TRUE:   'true';
FALSE:  'false';
NULL:   'null';

// string literals
STRING		
@init{StringBuilder lBuf = new StringBuilder();}
	:	'"' (escaped=ESC {lBuf.append(escaped.getText());}| normal=~('"'|'\\'|'\n'|'\r') {lBuf.appendCodePoint(normal);} )* '"' {setText(lBuf.toString());}
	;

fragment
ESC
	:	'\\'
		(	'n'    {setText("\n");}
		|	'r'    {setText("\r");}
		|	't'    {setText("\t");}
		|	'b'    {setText("\b");}
		|	'f'    {setText("\f");}
		|	'"'    {setText("\"");}
		|	'\''   {setText("\'");}
		|	'/'    {setText("/");}
		|	'\\'   {setText("\\");}
		|	('u')+ i=HEX_DIGIT j=HEX_DIGIT k=HEX_DIGIT l=HEX_DIGIT   {setText(ParserUtil.hexToChar(i.getText(),j.getText(),k.getText(),l.getText()));}

		)
	;

fragment
HEX_DIGIT
	:	('0'..'9'|'A'..'F'|'a'..'f')
	;

fragment
ZERO: '0' ;

fragment
NONZERO: '1'..'9';

fragment
DIGIT: ZERO | NONZERO;

fragment
INTEGER: ZERO | NONZERO (DIGIT)* ;

NUMBER
	:	('-')?
	    INTEGER
		(	(
				'.'
				(DIGIT)+ (EXPONENT)?
			)?
		|	EXPONENT 	// 'E' means we are float
		)
	;


EXPONENT
	:	('e') ('+'|'-')? INTEGER
	;

// Whitespace -- ignored
WS	:	(	' '
		|	'\t'
		|	'\f'
			// handle newlines
		|	(	
			:	
				'\r'    // Macintosh
			|	'\n'    // Unix (the right way)
			)
		)+
		{ skip(); }
	;

// Single-line comments
SL_COMMENT
	:	'#'
		(~('\n'|'\r'))* ('\n'|'\r'('\n')?)
		{$channel=HIDDEN;}
	;
