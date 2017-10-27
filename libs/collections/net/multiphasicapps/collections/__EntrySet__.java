// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.collections;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;

/**
 * This represents the entry set of the map.
 *
 * @param <K> The keys to use.
 * @param <V> The values to use.
 * @since 2016/09/07
 */
final class __EntrySet__<K, V>
	extends AbstractSet<Map.Entry<K, V>>
{
	/** The owning map. */
	private final SortedTreeMap<K, V> _map;
	
	/**
	 * Initializes the entry set.
	 *
	 * @param __m The owning map.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/07
	 */
	__EntrySet__(SortedTreeMap<K, V> __m)
		throws NullPointerException
	{
		// Check
		if (__m == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._map = __m;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/07
	 */
	@Override
	public Iterator<Map.Entry<K, V>> iterator()
	{
		return new __MapIterator__<K, V>(this._map);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/07
	 */
	@Override
	public int size()
	{
		return this._map.size();
	}
}

