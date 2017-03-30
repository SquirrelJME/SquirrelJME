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

import java.util.Map;

/**
 * This stores a key and value pair which is referenced by a node.
 *
 * @since 2017/03/30
 */
class __Data__<K, V>
	implements Map.Entry<K, V>
{
	/** The key for this data. */
	final K _key;
	
	/** The value of the data. */
	volatile V _value;
	
	/** The data before this one. */
	volatile __Data__<K, V> _prev;
	
	/** The data after this one. */
	volatile __Data__<K, V> _next;
	
	/**
	 * Initializes the data.
	 *
	 * @param __k The key used for this data.
	 * @param __v The value to initially store.
	 * @since 2017/03/30
	 */
	__Data__(K __k, V __v)
	{
		this._key = __k;
		this._value = __v;
	}
}

