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
 * The logical opposite of JSONInclude annotation. When the JSONMapper goes to
 * serialize an object to JSON if this annotation is applied to the getter
 * method, the value will not be serialized to JSON even if it has public
 * getters and setters. This can be used to gate the JSON serializer from
 * venturing into infinite recursion (IE: person->address->person..etc).
 * Additionally the JSON Mapper will not write to property where either the
 * getter or the setter has JSONExclude set.
 *
 * @author itaylor
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JSONExclude
{

}
