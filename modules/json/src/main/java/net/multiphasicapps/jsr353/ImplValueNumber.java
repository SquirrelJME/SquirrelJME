// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.jsr353;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import com.oracle.json.JsonNumber;

/**
 * This represents a single numerical value.
 *
 * @since 2014/08/07
 */
public class ImplValueNumber
	implements JsonNumber
{
	/** Value of the number. */
	private final Number _val;
	
	/**
	 * Parses a string to get the number value.
	 * 
	 * @param __s The string to parse.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/07/12
	 */
	public ImplValueNumber(String __s)
		throws NullPointerException
	{
		this(ImplValueNumber.__parseNumber(__s));
	}
	
	/**
	 * Sets the value of this big decimal value.
	 *
	 * @param __v Value to set.
	 * @since 2014/08/07
	 */
	public ImplValueNumber(Number __v)
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
	 * Invokes {@code numberValue().doubleValue()}, information may be
	 * lost.
	 *
	 * @return A {@code double}.
	 * @since 2014/08/07
	 */
	@Override
	public double doubleValue()
	{
		return this.numberValue().doubleValue();
	}
	
	/**
	 * Compares this {@link JsonNumber} with another, returns {@code true} only
	 * if the other object is a {@link JsonNumber} and both
	 * {@link #numberValue()}s are equal.
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
		
		// Compare multiple values accordingly
		JsonNumber o = (JsonNumber)__o;
		return this.longValue() == o.longValue() &&
			this.doubleValue() == o.doubleValue();
	}
	
	/**
	 * Invokes {@code numberValue().hashCode()}.
	 *
	 * @return The hash code.
	 * @since 2014/08/07
	 */
	@Override
	public int hashCode()
	{
		return this.numberValue().hashCode();
	}
	
	/**
	 * Invokes {@code numberValue().intValue()}, information may be lost.
	 *
	 * @return An integer value.
	 * @since 2014/08/07
	 */
	@Override
	public int intValue()
	{
		return this.numberValue().intValue();
	}
	
	/**
	 * Invokes {@code numberValue().intValueExact()}.
	 *
	 * @return An integer value.
	 * @throws ArithmeticException If the integer has a fraction or does not
	 * fit within an int.
	 * @since 2014/08/07
	 */
	@Override
	public int intValueExact()
	{
		long val = this.longValueExact();
		
		if (val < Integer.MIN_VALUE || val > Integer.MAX_VALUE)
			throw new ArithmeticException("ARIT");
		
		return (int)val;
	}
	
	/**
	 * Checks whether the specified value is an integer,
	 * {@code numberValue().scale()} is used and if the scale is zero it is
	 * considered an integer.
	 *
	 * @return {@code true} if this is an integer.
	 * @since 2014/08/07
	 */
	@Override
	public boolean isIntegral()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/12
	 */
	@Override
	public Number numberValue()
	{
		return this._val;
	}
	
	/**
	 * Invokes {@code numberValue().longValue()}, information may be lost.
	 *
	 * @return The long value.
	 * @since 2014/08/07
	 */
	@Override
	public long longValue()
	{
		return this.numberValue().longValue();
	}
	
	/**
	 * Invokes {@code numberValue().longValueExact()}.
	 *
	 * @return A long value.
	 * @throws ArithmeticException If the integer has a fraction or does not
	 * fit within a long.
	 * @since 2014/08/07
	 */
	@Override
	public long longValueExact()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Invokes {@code numberValue().toString()}.
	 *
	 * @return The value as a string.
	 * @since 2014/08/07
	 */
	@Override
	public String toString()
	{
		return this.numberValue().toString();
	}
	
	/**
	 * Attempts to parse the given number.
	 * 
	 * @param __s The string to parse.
	 * @return The parsed number.
	 * @throws NullPointerException On null arguments.
	 * @throws NumberFormatException If the number is not correct.
	 * @since 2022/07/12
	 */
	static Number __parseNumber(String __s)
		throws NullPointerException, NumberFormatException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		try
		{
			return Long.parseLong(__s, 10);
		}
		catch (NumberFormatException ignored)
		{
			return Double.parseDouble(__s);
		}
	}
}

