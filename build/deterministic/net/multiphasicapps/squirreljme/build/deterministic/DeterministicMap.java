// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.deterministic;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This is a determinsitic map which sorts all entries it contains while
 * using a simple array backed implementation.
 *
 * This class is not thread safe.
 *
 * The mapping is backed by an array.
 *
 * @param <K> The key type to store.
 * @param <V> The value type to store.
 * @since 2016/10/28
 */
public class DeterministicMap<K extends Comparable<K>, V>
	extends AbstractMap<K, V>
{
	/** Keys in the map. */
	private final List<K> _keys =
		new ArrayList<>();
	
	/** Values in the map. */
	private final List<V> _values =
		new ArrayList<>();
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/28
	 */
	@Override
	public void clear()
	{
		this._keys.clear();
		this._values.clear();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/28
	 */
	@Override
	public boolean containsKey(Object __k)
	{
		return this._keys.contains(__k);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/28
	 */
	@Override
	public boolean containsValue(Object __v)
	{
		return this._values.contains(__v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/28
	 */
	@Override
	public Set<Map.Entry<K, V>> entrySet()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/28
	 */
	@Override
	public V put(K __k, V __v)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/28
	 */
	@Override
	public int size()
	{
		return this._keys.size();
	}
}

