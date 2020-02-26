package org.testng.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom attributes for a test.
 *
 * @since 2020/02/26
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface CustomAttribute {
	/** @retrun The name of the attribute. */
	String name();
	
	/** @return The values of the attributes. */
	String[] values() default {};
}
