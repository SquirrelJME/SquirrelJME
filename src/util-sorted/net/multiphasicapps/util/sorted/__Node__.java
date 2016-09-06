// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.util.sorted;

/**
 * This represents a single node within the tree.
 *
 * @since 2016/09/06
 */
class __Node__<V>
{
	/** The value. */
	volatile V _value;
	
	/** The color. */
	volatile __Color__ _color;
	
	/** The node to the left. */
	volatile __Node__<V> _left;
	
	/** The node to the right. */
	volatile __Node__<V> _right;
	
	/** The parent node. */
	volatile __Node__<V> _parent;
	
	/**
	 * Initializes a node with no value.
	 *
	 * @since 2016/09/06
	 */
	__Node__()
	{
	}
	
	/**
	 * Initializes a node with the given value.
	 *
	 * @param __v The initial value.
	 * @since 2016/09/06
	 */
	__Node__(V __v)
	{
		this._value = __v;
	}
	
	/**
	 * Checks if a node on a given side is of the specified color.
	 *
	 * @param __r If {@code true} then the right side is checked, otherwise
	 * the left.
	 * @param __c The color to check.
	 * @return {@code true} if the side is the given color.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/06
	 */
	final boolean isSideColor(boolean __r, __Color__ __c)
		throws NullPointerException
	{
		// Check
		if (__c == null)
			throw new NullPointerException("NARG");
		
		// Get node, if there is no node there then never a color
		__Node__<V> n = (__r ? this._right : this._left);
		if (n == null)
			return false;
		
		// Check
		return __c == n._color;
	}
	
	/**
	 * Checks if a node on a given side is of the red color.
	 *
	 * @param __r If {@code true} then the right side is checked, otherwise
	 * the left.
	 * @return {@code true} if the side is red.
	 * @since 2016/09/06
	 */
	final boolean isSideColorRed(boolean __r)
	{
		return isSideColor(__r, __Color__.RED);
	}
}

