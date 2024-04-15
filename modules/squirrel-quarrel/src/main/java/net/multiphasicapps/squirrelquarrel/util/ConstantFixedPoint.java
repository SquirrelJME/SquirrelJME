// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.util;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This represents a constant fixed point number.
 *
 * @since 2018/03/18
 */
public final class ConstantFixedPoint
	implements Comparable<FixedPoint>, FixedPoint
{
	/** Bit shift. */
	public static final int SHIFT =
		16;
	
	/** Mask. */
	public static final int MASK =
		0xFFFF;
	
	/** The value. */
	private int _value;
	
	/**
	 * Initializes the fixed point value with the given whole number.
	 *
	 * @param __whole The whole number, only the lowest 16-bits are used.
	 * @since 2018/03/18
	 */
	public ConstantFixedPoint(int __whole)
	{
		this._value = __whole << ConstantFixedPoint.SHIFT;
	}
	
	/**
	 * Initializes the fixed point value with the given whole number and
	 * fraction.
	 *
	 * @param __whole The whole number, only the lowest 16-bits are used.
	 * @param __frac The fraction, only the lowest 16-bits are used.
	 * @since 2018/03/18
	 */
	public ConstantFixedPoint(int __whole, int __frac)
	{
		this._value = (__whole << ConstantFixedPoint.SHIFT) | (__frac & ConstantFixedPoint.MASK);
	}
	
	/**
	 * Initializes the fixed point value with the given fixed point value.
	 *
	 * @param __fp The fixed point number to copy.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/18
	 */
	public ConstantFixedPoint(FixedPoint __fp)
		throws NullPointerException
	{
		if (__fp == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
	
	/**
	 * Parses the given string and reads a fixed point value.
	 *
	 * @param __s The input string
	 * @throws IllegalArgumentException If the string is invalid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/18
	 */
	public ConstantFixedPoint(String __s)
		throws IllegalArgumentException, NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		int value;
		
		// Is a whole number
		int dot = __s.indexOf('.');
		if (dot < 0)
			value = Integer.parseInt(__s, 10) << ConstantFixedPoint.SHIFT;
		
		// Is fractional value
		else
			throw Debugging.todo();
		
		this._value = value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/18
	 */
	@Override
	public final int compareTo(FixedPoint __o)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/18
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/18
	 */
	@Override
	public final int fraction()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/18
	 */
	@Override
	public final int hashCode()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/18
	 */
	@Override
	public final String toString()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/18
	 */
	@Override
	public final int whole()
	{
		throw Debugging.todo();
	}
}

