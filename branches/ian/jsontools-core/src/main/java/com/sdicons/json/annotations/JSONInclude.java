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
 * By default, the JSON Mapper will only serialize objects to JSON that have
 * public properties with both getters and setters. If this annotation is
 * applied to a getter, the values will be serialized as JSON, even if there is
 * not a public setter property for that element. If it is applied to a setter,
 * it will allow a field to be set even if it doesn't have a public property.
 *
 * @author itaylor
 */

@Target( { ElementType.METHOD })
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface JSONInclude
{

}
