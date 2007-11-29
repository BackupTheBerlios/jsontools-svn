/**
 *
 */
package com.sdicons.json.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author itaylor
 *
 * This class is used to define a Method that can be called from JavaScript.
 *
 * It must be used inside a Servlet that extends JSONServlet
 * The params are the name of the params that will be taken from the HTTP request and passed to the Java Method that
 * this annotation is applied to.
 *
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JSONMethod
{
	public String[] params();
	public String JsFunctionName();
}
