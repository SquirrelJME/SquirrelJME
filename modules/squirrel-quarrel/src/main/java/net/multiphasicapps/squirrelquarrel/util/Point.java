// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.util;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This is an immutable class which contains X and Y coordinates.
 *
 * @since 2017/02/17
 */
public final class Point
	implements Comparable<Point>
{
	/** X coordinate. */
	public final int x;
	
	/** Y coordinate. */
	public final int y;
	
	/** String form. */
	private Reference<String> _string;
	
	/**
	 * Initializes the point.
	 *
	 * @param __x X coordinate.
	 * @param __y Y coordinate.
	 * @since 2017/02/17
	 */
	public Point(int __x, int __y)
	{
		this.x = __x;
		this.y = __y;
	}
	
	/**
	 * Parses a point from the given string.
	 *
	 * @param __s The string to parse.
	 * @throws IllegalArgumentException If the string is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/18
	 */
	public Point(String __s)
		throws IllegalArgumentException, NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error BE0g Point must start and end with parenthesis.
		(The input string)} */
		if (!__s.startsWith("(") || !__s.endsWith(")"))
			throw new IllegalArgumentException("BE0g " + __s);
		
		// Trim those out
		__s = __s.substring(1, __s.length() - 1);
		
		/* {@squirreljme.error BE0h Point must have a comma between the
		units. (The input string)} */
		int com = __s.indexOf(',');
		if (com < 0)
			throw new IllegalArgumentException("BE0h " + __s);
		
		this.x = Integer.parseInt(__s.substring(0, com).trim(), 10);
		this.y = Integer.parseInt(__s.substring(com + 1).trim(), 10);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/17
	 */
	@Override
	public int compareTo(Point __o)
	{
		int rv;
		
		// Compare x
		rv = this.x - __o.x;
		if (rv != 0)
			return rv;
		
		// Compare y
		return this.y - __o.y;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/17
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof Point))
			return false;
		
		Point o = (Point)__o;
		return this.x == o.x && this.y == o.y;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/17
	 */
	@Override
	public int hashCode()
	{
		return this.x ^ ~(this.y);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/17
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>(
				rv = String.format("(%d, %d)", this.x, this.y));
		
		return rv;
	}
}

