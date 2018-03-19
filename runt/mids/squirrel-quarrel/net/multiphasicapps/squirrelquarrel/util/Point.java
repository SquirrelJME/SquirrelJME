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
	private volatile Reference<String> _string;
	
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

