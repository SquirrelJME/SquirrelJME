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
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Entry set which uses an array base.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 * @since 2023/09/03
 */
final class __ArrayEntrySet__<K, V>
	extends AbstractSet<Map.Entry<K, V>>
{
	/** The keys. */
	private final K[] _keys;
	
	/** The values. */
	private final Object[] _values;
	
	/**
	 * Initializes the array based entry set.
	 *
	 * @param __keys The keys used.
	 * @param __values The values used.
	 * @throws IllegalArgumentException If the lengths do not match.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/03
	 */
	__ArrayEntrySet__(K[] __keys, Object[] __values)
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
	public Iterator<Map.Entry<K, V>> iterator()
	{
		return new __ArrayEntrySetIterator__(this._keys, this._values);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/09/03
	 */
	@Override
	public int size()
	{
		return this._keys.length;
	}
}
