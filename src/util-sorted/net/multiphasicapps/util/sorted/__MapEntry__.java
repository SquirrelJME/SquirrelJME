// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.util.sorted;

import java.util.Map;
import java.util.Objects;

/**
 * This represents a map entry.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 * @since 2016/09/07
 */
final class __MapEntry__<K, V>
	implements Map.Entry<K, V>
{
	/** The map node being referenced. */
	protected final __Node__<K, V> _node;
	
	/**
	 * Initializes the map entry node.
	 *
	 * @param __n The node to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/07
	 */
	__MapEntry__(__Node__<K, V> __n)
		throws NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._node = __n;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/07
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Must be another entry
		if (!(__o instanceof Map.Entry))
			return false;
		
		// Compare
		Map.Entry<?, ?> o = (Map.Entry<?, ?>)__o;
		return Objects.equals(getKey(), o.getKey()) &&
			Objects.equals(getValue(), o.getValue());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/07
	 */
	@Override
	public K getKey()
	{
		return this._node._key;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/07
	 */
	@Override
	public V getValue()
	{
		return this._node._value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/07
	 */
	@Override
	public int hashCode()
	{
		return Objects.hashCode(getKey()) ^ Objects.hashCode(getValue());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/07
	 */
	@Override
	public V setValue(V __a)
	{
		__Node__<K, V> node = this._node;
		V rv = node._value;
		node._value = __a;
		return rv;
	}
}

