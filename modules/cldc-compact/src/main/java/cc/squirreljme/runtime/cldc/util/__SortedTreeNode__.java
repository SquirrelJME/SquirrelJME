// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.util;

/**
 * This represents a single node within the tree.
 *
 * @param <K> The key used.
 * @param <V> The value used.
 * @since 2016/09/06
 */
class __SortedTreeNode__<K, V>
{
	/** The color, defaults to red. */
	volatile boolean _isred =
		true;
	
	/** The currently associated data for this node. */
	volatile __SortedTreeData__<K, V> _data;
	
	/** The node to the left. */
	volatile __SortedTreeNode__<K, V> _left;
	
	/** The node to the right. */
	volatile __SortedTreeNode__<K, V> _right;
	
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

