// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.util.sorted;

/**
 * This represents a single node within the tree.
 *
 * @param <K> The key used.
 * @param <V> The value used.
 * @since 2016/09/06
 */
class __Node__<K, V>
{
	/** The value. */
	final K _key;
	
	/** The value. */
	volatile V _value;
	
	/** The color, defaults to red. */
	volatile boolean _isred =
		true;
	
	/** The node to the left. */
	volatile __Node__<K, V> _left;
	
	/** The node to the right. */
	volatile __Node__<K, V> _right;
	
	/** The parent node. */
	volatile __Node__<K, V> _parent;
	
	/**
	 * Initializes a node with no value.
	 *
	 * @param __k The key.
	 * @since 2016/09/06
	 */
	__Node__(K __k)
	{
		// Set
		this._key = __k;
	}
	
	/**
	 * Checks if a node on a given side is of the specified color.
	 *
	 * @param __r If {@code true} then the right side is checked, otherwise
	 * the left.
	 * @param __red If {@code true} then red is checked.
	 * @return {@code true} if the side is the given color.
	 * @since 2016/09/06
	 */
	final boolean isSideColor(boolean __r, boolean __red)
	{
		// Get node, if there is no node there then never a color
		__Node__<K, V> n = (__r ? this._right : this._left);
		if (n == null)
			return false;
		
		// Check
		return __red == n._isred;
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
		return isSideColor(__r, true);
	}
}

