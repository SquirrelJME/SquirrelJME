// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.util;

/**
 * This represents a mutable fixed point number.
 *
 * @since 2018/03/18
 */
public final class MutableFixedPoint
	implements Comparable<FixedPoint>, FixedPoint
{
	/**
	 * Initializes the fixed point value with the given whole number.
	 *
	 * @param __whole The whole number, only the lowest 16-bits are used.
	 * @since 2018/03/18
	 */
	public MutableFixedPoint(int __whole)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Initializes the fixed point value with the given whole number and
	 * fraction.
	 *
	 * @param __whole The whole number, only the lowest 16-bits are used.
	 * @param __frac The fraction, only the lowest 16-bits are used.
	 * @since 2018/03/18
	 */
	public MutableFixedPoint(int __whole, int __frac)
	{
	}
	
	/**
	 * Initializes the fixed point value with the given fixed point value.
	 *
	 * @param __fp The fixed point number to copy.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/18
	 */
	public MutableFixedPoint(FixedPoint __fp)
		throws NullPointerException
	{
		if (__fp == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Parses the given string and reads a fixed point value.
	 *
	 * @param __s The input string
	 * @throws IllegalArgumentException If the string is invalid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/18
	 */
	public MutableFixedPoint(String __s)
		throws IllegalArgumentException, NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/18
	 */
	@Override
	public final int compareTo(FixedPoint __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/18
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/18
	 */
	@Override
	public final int fraction()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/18
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/18
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/18
	 */
	@Override
	public final int whole()
	{
		throw new todo.TODO();
	}
}

