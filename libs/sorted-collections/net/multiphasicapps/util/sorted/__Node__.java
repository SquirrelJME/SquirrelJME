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
	/** The color, defaults to red. */
	volatile boolean _isred =
		true;
	
	/** The currently associated data for this node. */
	volatile __Data__<K, V> _data;
	
	/** The node to the left. */
	volatile __Node__<K, V> _left;
	
	/** The node to the right. */
	volatile __Node__<K, V> _right;
	
	/**
	 * Compares this node against the given key.
	 *
	 * @param __n The key to compare against.
	 * @return The comparison result.
	 * @since 2017/03/30
	 */
	final int __compare(K __k)
	{
		return this._data.__compare(__k);
	}
	
	/**
	 * Compares this node against the given data.
	 *
	 * @param __n The data to compare against.
	 * @return The comparison result.
	 * @since 2017/03/30
	 */
	final int __compare(__Data__<K, V> __d)
	{
		return this._data.__compare(__d);
	}
	
	/**
	 * Compares this node against the given node.
	 *
	 * @param __n The node to compare against.
	 * @return The comparison result.
	 * @since 2017/03/30
	 */
	final int __compare(__Node__<K, V> __n)
	{
		return this._data.__compare(__n);
	}
	
	/**
	 * Makes the node black.
	 *
	 * @since 2017/03/30
	 */
	final void __makeBlack()
	{
		this._isred = false;
	}
}

