// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.jsr353;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.json.JsonNumber;

/**
 * This represents a single numerical value.
 *
 * @since 2014/08/07
 */
public class ImplValueNumber
	implements JsonNumber
{
	/** Value of the number. */
	private final BigDecimal _val;	
	
	/**
	 * Sets the value of this big decimal value.
	 *
	 * @param __v Value to set.
	 * @since 2014/08/07
	 */
	public ImplValueNumber(BigDecimal __v)
	{
		// Cannot be null
		if (__v == null)
			throw new NullPointerException(
				"No value specified.");
		
		// Set
		this._val = __v;
	}
	
	/**
	 * Returns the type of value this is.
	 *
	 * @return The type of value that this is.
	 * @since 2014/08/07
	 */
	@Override
	public ValueType getValueType()
	{
		return ValueType.NUMBER;
	}
	
	/**
	 * Returns the current value as a {@link BigDecimal}.
	 *
	 * @return A {@link BigDecimal}.
	 * @since 2014/08/07
	 */
	@Override
	public BigDecimal bigDecimalValue()
	{
		return this._val;
	}
	
	/**
	 * Invokes {@code bigDecimalValue().toBigInteger()}, information may be
	 * lost.
	 *
	 * @return A {@link BigInteger}.
	 * @since 2014/08/07
	 */
	@Override
	public BigInteger bigIntegerValue()
	{
		return this.bigDecimalValue().toBigInteger();
	}
	
	/**
	 * Invokes {@code bigDecimalValue().toBigIntegerExact()}.
	 *
	 * @return A {@link BigInteger}.
	 * @throws ArithmeticException If the number has a fraction.
	 * @since 2014/08/07
	 */
	@Override
	public BigInteger bigIntegerValueExact()
	{
		return this.bigDecimalValue().toBigIntegerExact();
	}
	
	/**
	 * Invokes {@code bigDecimalValue().doubleValue()}, information may be
	 * lost.
	 *
	 * @return A {@code double}.
	 * @since 2014/08/07
	 */
	@Override
	public double doubleValue()
	{
		return this.bigDecimalValue().doubleValue();
	}
	
	/**
	 * Compares this {@link JsonNumber} with another, returns {@code true} only
	 * if the other object is a {@link JsonNumber} and both
	 * {@link #bigDecimalValue()}s are equal.
	 *
	 * @return {@code true} if these match.
	 * @since 2014/08/07
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Numbers only
		if (!(__o instanceof JsonNumber))
			return false;
		
		// Compare big values
		return this.bigDecimalValue().equals(((JsonNumber)__o).bigDecimalValue());
	}
	
	/**
	 * Invokes {@code bigDecimalValue().hashCode()}.
	 *
	 * @return The hash code.
	 * @since 2014/08/07
	 */
	@Override
	public int hashCode()
	{
		return this.bigDecimalValue().hashCode();
	}
	
	/**
	 * Invokes {@code bigDecimalValue().intValue()}, information may be lost.
	 *
	 * @return An integer value.
	 * @since 2014/08/07
	 */
	@Override
	public int intValue()
	{
		return this.bigDecimalValue().intValue();
	}
	
	/**
	 * Invokes {@code bigDecimalValue().intValueExact()}.
	 *
	 * @return An integer value.
	 * @throws ArithmeticException If the integer has a fraction or does not
	 * fit within an int.
	 * @since 2014/08/07
	 */
	@Override
	public int intValueExact()
	{
		return this.bigDecimalValue().intValueExact();
	}
	
	/**
	 * Checks whether the specified value is an integer,
	 * {@code bigDecimalValue().scale()} is used and if the scale is zero it is
	 * considered an integer.
	 *
	 * @return {@code true} if this is an integer.
	 * @since 2014/08/07
	 */
	@Override
	public boolean isIntegral()
	{
		// Convert to BigInteger, that throws exception if there is a fraction
		// part.
		try
		{
			this.bigIntegerValueExact();
			return true;
		}
		
		// Conversion failed, not an integer
		catch (ArithmeticException ae)
		{
			return false;
		}
	}
	
	/**
	 * Invokes {@code bigDecimalValue().longValue()}, information may be lost.
	 *
	 * @return The long value.
	 * @since 2014/08/07
	 */
	@Override
	public long longValue()
	{
		return this.bigDecimalValue().longValue();
	}
	
	/**
	 * Invokes {@code bigDecimalValue().longValueExact()}.
	 *
	 * @return A long value.
	 * @throws ArithmeticException If the integer has a fraction or does not
	 * fit within a long.
	 * @since 2014/08/07
	 */
	@Override
	public long longValueExact()
	{
		return this.bigDecimalValue().longValueExact();
	}
	
	/**
	 * Invokes {@code bigDecimalValue().toString()}.
	 *
	 * @return The value as a string.
	 * @since 2014/08/07
	 */
	@Override
	public String toString()
	{
		return this.bigDecimalValue().toString();
	}
}

