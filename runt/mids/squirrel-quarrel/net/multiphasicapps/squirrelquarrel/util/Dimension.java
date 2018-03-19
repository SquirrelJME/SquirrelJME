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
 * This contains immutable dimension information for width and height.
 *
 * @since 2017/02/17
 */
public final class Dimension
	implements Comparable<Dimension>
{
	/** Width. */
	public final int width;
	
	/** Height. */
	public final int height;
	
	/** String form. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the dimension.
	 *
	 * @param __w Width.
	 * @param __h Height.
	 * @since 2017/02/17
	 */
	public Dimension(int __w, int __h)
	{
		this.width = __w;
		this.height = __h;
	}
	
	/**
	 * Parses a dimension from the given string.
	 *
	 * @param __s The string to parse.
	 * @throws IllegalArgumentException If the string is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/18
	 */
	public Dimension(String __s)
		throws IllegalArgumentException, NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/17
	 */
	@Override
	public int compareTo(Dimension __o)
	{
		int rv,
			aw = this.width,
			as = aw * this.height,
			bw = __o.width,
			bs = bw * __o.height;
		
		// Compare surface area first
		rv = as - bs;
		if (rv != 0)
			return rv;
		
		// Compare the width
		return aw - bw;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/17
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof Dimension))
			return false;
		
		Dimension o = (Dimension)__o;
		return this.width == o.width && this.height == o.height;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/17
	 */
	@Override
	public int hashCode()
	{
		return this.width ^ ~(this.height);
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
				rv = String.format("[%d, %d]", this.width, this.height));
		
		return rv;
	}
}

