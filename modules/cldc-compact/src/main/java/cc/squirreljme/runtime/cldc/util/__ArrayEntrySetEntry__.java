// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.util;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Map;

/**
 * Single entry for array entry sets.
 *
 * @since 2023/09/03
 */
final class __ArrayEntrySetEntry__<K, V>
	implements Map.Entry<K, V>
{
	/** The value index. */
	private final int _index;
	
	/** The key used. */
	private final K _key;
	
	/** The values array, for setting. */
	private final Object[] _values;
	
	/**
	 * Initializes the array entry.
	 *
	 * @param __key The key used.
	 * @param __index The index of the entry.
	 * @param __values The values used.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/03
	 */
	__ArrayEntrySetEntry__(K __key, int __index, Object[] __values)
		throws NullPointerException
	{
		if (__key == null || __values == null)
			throw new NullPointerException("NARG");
		
		this._key = __key;
		this._index = __index;
		this._values = __values;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/09/03
	 */
	@Override
	public K getKey()
	{
		return this._key;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/09/03
	 */
	@Override
	@SuppressWarnings("unchecked")
	public V getValue()
	{
		return (V)this._values[this._index];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/09/03
	 */
	@SuppressWarnings("unchecked")
	@Override
	public V setValue(V __v)
	{
		Object[] values = this._values;
		int index = this._index;
		
		V old = (V)values[index];
		values[index] = __v;
		return old;
	}
}
