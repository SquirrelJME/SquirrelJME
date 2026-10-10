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
public @interface NanoDetails
{
	/** Expected void value. */
	boolean expectedVoid() default false;
	
	/** Expected integer value. */
	int expectedInteger() default Integer.MIN_VALUE;
	
	/** Expected long value. */
	long expectedLong() default Long.MIN_VALUE;
	
	/** Expected string value. */
	String expectedString() default "";
	
	/** Expected exception. */
	String expectedException() default "";
}
