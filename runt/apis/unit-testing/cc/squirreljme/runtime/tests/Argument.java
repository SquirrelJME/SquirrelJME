// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.tests;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This defines a single inpuit paramater which is mapped to the test input
 * arguments.
 *
 * @since 2018/03/06
 */
@Documented
@Retention(RetentionPolicy.CLASS)
public @interface Argument
{
	/**
	 * Indicates the type of value this parameter stores.
	 *
	 * @since 2018/03/06
	 */
	ArgumentType type();
	
	/**
	 * Specifies the boolean value.
	 *
	 * @since 2018/03/06
	 */
	boolean booleanValue() default false;
	
	/**
	 * Specifies the byte value.
	 *
	 * @since 2018/03/06
	 */
	byte byteValue() default 0;
	
	/**
	 * Specifies the short value.
	 *
	 * @since 2018/03/06
	 */
	short shortValue() default 0;
	
	/**
	 * Specifies the char value.
	 *
	 * @since 2018/03/06
	 */
	char charValue() default 0;
	
	/**
	 * Specifies the int value.
	 *
	 * @since 2018/03/06
	 */
	int intValue() default 0;
	
	/**
	 * Specifies the long value.
	 *
	 * @since 2018/03/06
	 */
	long longValue() default 0;
	
	/**
	 * Specifies the float value.
	 *
	 * @since 2018/03/06
	 */
	float floatValue() default 0;
	
	/**
	 * Specifies the double value.
	 *
	 * @since 2018/03/06
	 */
	double doubleValue() default 0;
	
	/**
	 * Specifies the String value.
	 *
	 * @since 2018/03/06
	 */
	String stringValue() default "";
	
	/**
	 * Specifies the class value.
	 *
	 * @since 2018/03/06
	 */
	Class<?> classValue() default Object.class;
	
	/**
	 * Specifies the enum type.
	 *
	 * @since 2018/03/06
	 */
	Class<? extends Enum> enumType() default Enum.class;
	
	/**
	 * Specifies the enum value.
	 *
	 * @since 2018/03/06
	 */
	String enumValue() default "";
}

