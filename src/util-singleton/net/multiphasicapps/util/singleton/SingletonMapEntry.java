// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.util.singleton;

import java.util.Map;
import java.util.Objects;

/**
 * This is an unmodifiable entry.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 * @since 2016/05/12
 */
public final class SingletonMapEntry<K, V>
	implements Map.Entry<K, V>
{
	/** The stored key. */
	protected final K key;
	
	/** The stored value. */
	protected final V value;
	
	/**
	 * Initializes the map entry.
	 *
	 * @param __k The associated key.
	 * @param __v The associated value.
	 * @since 2016/05/12
	 */
	public SingletonMapEntry(K __k, V __v)
	{
		// Set
		key = __k;
		value = __v;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Not another entry?
		if (!(__o instanceof Map.Entry))
			return false;
		
		// Cast
		Map.Entry<?, ?> o = (Map.Entry<?, ?>)__o;
		return Objects.equals(key, o.getKey()) &&
			Objects.equals(value, o.getValue());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public K getKey()
	{
		return key;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public V getValue()
	{
		return value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public int hashCode()
	{
		return Objects.hashCode(key) ^ Objects.hashCode(value);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public V setValue(V __v)
	{
		throw new UnsupportedOperationException("RORO");
	}
}

