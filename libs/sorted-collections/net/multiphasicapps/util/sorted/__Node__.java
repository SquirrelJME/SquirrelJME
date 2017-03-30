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
	private volatile boolean _isred =
		true;
	
	/** The currently associated data for this node. */
	volatile __Data__<K, V> _data;
	
	/** The node to the left. */
	volatile __Node__<K, V> _left;
	
	/** The node to the right. */
	volatile __Node__<K, V> _right;
}

