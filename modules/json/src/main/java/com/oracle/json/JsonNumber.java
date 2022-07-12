// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.oracle.json;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * This is an immutable numerical value, some methods use the
 * semantics of {@link BigDecimal}.
 *
 * @since 2014/07/25
 */
public interface JsonNumber
	extends JsonValue
{
	/**
	 * Returns the current value as a {@link BigDecimal}.
	 *
	 * @return A {@link BigDecimal}.
	 * @since 2014/07/25
	 */
	BigDecimal bigDecimalValue();
	
	/**
	 * Invokes {@code bigDecimalValue().toBigInteger()}, information may be
	 * lost.
	 *
	 * @return A {@link BigInteger}.
	 * @since 2014/07/25
	 */
	BigInteger bigIntegerValue();
	
	/**
	 * Invokes {@code bigDecimalValue().toBigIntegerExact()}.
	 *
	 * @return A {@link BigInteger}.
	 * @throws ArithmeticException If the number has a fraction.
	 * @since 2014/07/25
	 */
	BigInteger bigIntegerValueExact();
	
	/**
	 * Invokes {@code bigDecimalValue().doubleValue()}, information may be
	 * lost.
	 *
	 * @return A {@code double}.
	 * @since 2014/07/25
	 */
	double doubleValue();
	
	/**
	 * Compares this {@link JsonNumber} with another, returns {@code true} only
	 * if the other object is a {@link JsonNumber} and both
	 * {@link #bigDecimalValue()}s are equal.
	 *
	 * @return {@code true} if these match.
	 * @since 2014/07/25
	 */
	@Override
	boolean equals(Object __o);
	
	/**
	 * Invokes {@code bigDecimalValue().hashCode()}.
	 *
	 * @return The hash code.
	 * @since 2014/07/25
	 */
	@Override
	int hashCode();
	
	/**
	 * Invokes {@code bigDecimalValue().intValue()}, information may be lost.
	 *
	 * @return An integer value.
	 * @since 2014/07/25
	 */
	int intValue();
	
	/**
	 * Invokes {@code bigDecimalValue().intValueExact()}.
	 *
	 * @return An integer value.
	 * @throws ArithmeticException If the integer has a fraction or does not
	 * fit within an int.
	 * @since 2014/07/25
	 */
	int intValueExact();
	
	/**
	 * Checks whether the specified value is an integer,
	 * {@code bigDecimalValue().scale()} is used and if the scale is zero it is
	 * considered an integer.
	 *
	 * @return {@code true} if this is an integer.
	 * @since 2014/07/25
	 */
	boolean isIntegral();
	
	/**
	 * Invokes {@code bigDecimalValue().longValue()}, information may be lost.
	 *
	 * @return The long value.
	 * @since 2014/07/25
	 */
	long longValue();
	
	/**
	 * Invokes {@code bigDecimalValue().longValueExact()}.
	 *
	 * @return A long value.
	 * @throws ArithmeticException If the integer has a fraction or does not
	 * fit within a long.
	 * @since 2014/07/25
	 */
	long longValueExact();
	
	/**
	 * Invokes {@code bigDecimalValue().toString()}.
	 *
	 * @return The value as a string.
	 * @since 2014/07/25
	 */
	@Override
	String toString();
}

