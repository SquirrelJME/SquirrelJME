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
 * This contains an immutable rectangle which starts at a given point and has
 * a width and height.
 *
 * @since 2017/02/17
 */
public final class Rectangle
	implements Comparable<Rectangle>
{
	/** X position. */
	public final int x;
	
	/** Y position. */
	public final int y;
	
	/** Width. */
	public final int width;
	
	/** Height. */
	public final int height;
	
	/** String form. */
	private volatile Reference<String> _string;
	
	/** As a point. */
	private volatile Reference<Point> _point;
	
	/** As an end point. */
	private volatile Reference<Point> _endpoint;
	
	/** As a dimension. */
	private volatile Reference<Dimension> _dimension;
	
	/**
	 * Initializes the rectangle.
	 *
	 * @param __x X coordinate.
	 * @param __y Y coordinate.
	 * @param __w Width.
	 * @param __h Height.
	 * @since 2017/02/17
	 */
	public Rectangle(int __x, int __y, int __w, int __h)
	{
		this.x = __x;
		this.y = __y;
		this.width = __w;
		this.height = __h;
	}
	
	/**
	 * Parses a rectanle from the given string.
	 *
	 * @param __s The string to parse.
	 * @throws IllegalArgumentException If the string is not valid.
	 * @since 2018/03/18
	 */
	public Rectangle(String __s)
		throws IllegalArgumentException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/17
	 */
	@Override
	public int compareTo(Rectangle __o)
	{
		int rv,
			aw = this.width,
			as = aw * this.height,
			bw = __o.width,
			bs = bw * __o.height;
		
		// Compare x
		rv = this.x - __o.x;
		if (rv != 0)
			return rv;
		
		// Compare y
		rv = this.y - __o.y;
		if (rv != 0)
			return rv;
		
		// Compare surface area
		rv = as - bs;
		if (rv != 0)
			return rv;
		
		// Compare the width
		return aw - bw;
	}
	
	/**
	 * Returns this rectangle as a dimension.
	 *
	 * @return The dimension of this rectangle.
	 * @since 2017/02/17
	 */
	public Dimension dimension()
	{
		Reference<Dimension> ref = this._dimension;
		Dimension rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._dimension = new WeakReference<>(
				rv = new Dimension(this.width, this.height));
		
		return rv;
	}
	
	/**
	 * Returns this tne end point of this rectangle.
	 *
	 * @return The end point of this rectangle.
	 * @since 2017/02/17
	 */
	public Point endPoint()
	{
		Reference<Point> ref = this._endpoint;
		Point rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._endpoint = new WeakReference<>(
				rv = new Point(this.x + this.width, this.y + this.height));
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/17
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof Rectangle))
			return false;
		
		Rectangle o = (Rectangle)__o;
		return this.x == o.x && this.y == o.y &&
			this.width == o.width && this.height == o.height;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/17
	 */
	@Override
	public int hashCode()
	{
		return this.x ^ ~(this.y) ^ this.width ^ ~(this.height);
	}
	
	/**
	 * Returns this rectangle as a point.
	 *
	 * @return The point of this rectangle.
	 * @since 2017/02/17
	 */
	public Point point()
	{
		Reference<Point> ref = this._point;
		Point rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._point = new WeakReference<>(rv = new Point(this.x, this.y));
		
		return rv;
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
				rv = String.format("(%d, %d, %d, %d]", this.x, this.y,
					this.width, this.height));
		
		return rv;
	}
}

