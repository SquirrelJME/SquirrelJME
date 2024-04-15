// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.oracle.json;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * This is an immutable numerical value.
 *
 * @since 2014/07/25
 */
@SuppressWarnings("InterfaceWithOnlyOneDirectInheritor")
@Api
public interface JsonNumber
	extends JsonValue
{
	/**
	 * Invokes {@code numberValue().doubleValue()}, information may be
	 * lost.
	 *
	 * @return A {@code double}.
	 * @since 2014/07/25
	 */
	@Api
	double doubleValue();
	
	/**
	 * Compares this {@link JsonNumber} with another, returns {@code true} only
	 * if the other object is a {@link JsonNumber} and both values are equal.
	 *
	 * @return {@code true} if these match.
	 * @since 2014/07/25
	 */
	@Override
	boolean equals(Object __o);
	
	/**
	 * Invokes {@code numberValue().hashCode()}.
	 *
	 * @return The hash code.
	 * @since 2014/07/25
	 */
	@Override
	int hashCode();
	
	/**
	 * Invokes {@code numberValue().intValue()}, information may be lost.
	 *
	 * @return An integer value.
	 * @since 2014/07/25
	 */
	@Api
	int intValue();
	
	/**
	 * Invokes {@code numberValue().intValueExact()}.
	 *
	 * @return An integer value.
	 * @throws ArithmeticException If the integer has a fraction or does not
	 * fit within an int.
	 * @since 2014/07/25
	 */
	@Api
	int intValueExact();
	
	/**
	 * Checks whether the specified value is an integer,
	 * {@code numberValue().scale()} is used and if the scale is zero it is
	 * considered an integer.
	 *
	 * @return {@code true} if this is an integer.
	 * @since 2014/07/25
	 */
	@Api
	boolean isIntegral();
	
	/**
	 * Returns the value that represents this value.
	 * 
	 * @return The value that represents this number.
	 * @since 2022/07/12
	 */
	@Api
	Number numberValue();
	
	/**
	 * Invokes {@code numberValue().longValue()}, information may be lost.
	 *
	 * @return The long value.
	 * @since 2014/07/25
	 */
	@Api
	long longValue();
	
	/**
	 * Invokes {@code numberValue().longValueExact()}.
	 *
	 * @return A long value.
	 * @throws ArithmeticException If the integer has a fraction or does not
	 * fit within a long.
	 * @since 2014/07/25
	 */
	@Api
	long longValueExact();
	
	/**
	 * Invokes {@code numberValue().toString()}.
	 *
	 * @return The value as a string.
	 * @since 2014/07/25
	 */
	@Override
	String toString();
}

