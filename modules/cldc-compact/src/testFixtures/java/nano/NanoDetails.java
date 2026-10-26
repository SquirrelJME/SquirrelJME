// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package nano;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jetbrains.annotations.VisibleForTesting;

/**
 * Not Described.
 *
 * @since 2026/01/10
 */
@VisibleForTesting
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
@SuppressWarnings("unused")
public @interface NanoDetails
{
	/** Expected boolean array value. */
	boolean[] expectedBooleans() default {};
	
	/** Expected byte array value. */
	byte[] expectedBytes() default {};
	
	/** Expected char array value. */
	char[] expectedChars() default {};
	
	/** Expected double value. */
	double expectedDouble() default Double.NaN;
	
	/** Expected double array value. */
	double[] expectedDoubles() default {};
	
	/** Expected float value. */
	float expectedFloat() default Float.NaN;
	
	/** Expected float array value. */
	float[] expectedFloats() default {};
	
	/** Expected integer value. */
	int expectedInteger() default Integer.MIN_VALUE;
	
	/** Expected integer array value. */
	int[] expectedIntegers() default {};
	
	/** Expected long value. */
	long expectedLong() default Long.MIN_VALUE;
	
	/** Expected long array value. */
	long[] expectedLongs() default {};
	
	/** Expected short array value. */
	short[] expectedShorts() default {};
	
	/** Expected string value. */
	String expectedString() default "";
	
	/** Expected string array value. */
	String[] expectedStrings() default {};
	
	/** Expected void value. */
	boolean expectedVoid() default false;
	
	/** Expected thrown type. */
	String thrown() default "";
}
