// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.util;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Entry set iterator for array entry sets.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 * @since 2023/09/03
 */
final class __ArrayEntrySetIterator__<K, V>
	implements Iterator<Map.Entry<K, V>>
{
	/** The keys. */
	private final K[] _keys;
	
	/** The values. */
	private final Object[] _values;
	
	/** The index this is at. */
	private volatile int _nextIndex;
	
	/**
	 * Initializes the array based entry set.
	 *
	 * @param __keys The keys used.
	 * @param __values The values used.
	 * @throws IllegalArgumentException If the lengths do not match.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/03
	 */
	__ArrayEntrySetIterator__(K[] __keys, Object[] __values)
		throws IllegalArgumentException, NullPointerException
	{
		if (__keys == null || __values == null)
			throw new NullPointerException("NARG");
		if (__keys.length != __values.length)
			throw new IllegalArgumentException("ILLG");
		
		this._keys = __keys;
		this._values = __values;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/09/03
	 */
	@Override
	public boolean hasNext()
	{
		return this._nextIndex < this._keys.length;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/09/03
	 */
	@Override
	public Map.Entry<K, V> next()
		throws NoSuchElementException
	{
		K[] keys = this._keys;
		
		// Are there still values left?
		int nextIndex = this._nextIndex;
		if (nextIndex >= keys.length)
			throw new NoSuchElementException("NSEE");
		
		// Setup entry
		Map.Entry<K, V> result = new __ArrayEntrySetEntry__<K, V>(
			keys[nextIndex], nextIndex, this._values);
		
		// Next call
		this._nextIndex = nextIndex + 1;
		
		// Give the result
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/09/03
	 */
	@Override
	public void remove()
		throws IllegalStateException, UnsupportedOperationException
	{
		throw new UnsupportedOperationException("RORO");
	}
}
