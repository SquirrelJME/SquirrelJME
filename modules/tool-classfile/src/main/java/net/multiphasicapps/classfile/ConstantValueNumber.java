// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

/**
 * This represents a constant value which is a number.
 *
 * @since 2018/05/21
 */
public abstract class ConstantValueNumber
	extends ConstantValue
{
	/** The number used. */
	protected final Number value;
	
	/**
	 * Initializes the constant value that uses a number.
	 *
	 * @param __v The value.
	 * @param __t The type of value used.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/21
	 */
	public ConstantValueNumber(Number __v, ConstantValueType __t)
		throws NullPointerException
	{
		super(__v, __t);
		
		if (__v == null)
			throw new NullPointerException("NARG");
		
		this.value = __v;
	}
	
	/**
	 * Returns the double value.
	 *
	 * @return The double value.
	 * @since 2018/05/16
	 */
	public final double doubleValue()
	{
		return this.value.doubleValue();
	}
	
	/**
	 * Returns the float value.
	 *
	 * @return The float value.
	 * @since 2018/05/16
	 */
	public final float floatValue()
	{
		return this.value.floatValue();
	}
	
	/**
	 * Returns the integer value.
	 *
	 * @return The integer value.
	 * @since 2018/05/16
	 */
	public final int intValue()
	{
		return this.value.intValue();
	}
	
	/**
	 * Returns the long value.
	 *
	 * @return The long value.
	 * @since 2018/05/16
	 */
	public final long longValue()
	{
		return this.value.longValue();
	}
	
	/**
	 * Returns the value as a number.
	 *
	 * @return The number used.
	 * @since 2018/05/21
	 */
	public final Number number()
	{
		return this.value;
	}
}

