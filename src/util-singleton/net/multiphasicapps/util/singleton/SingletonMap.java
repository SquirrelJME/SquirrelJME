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

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * This is an unmodifiable map which contains only a single key/value pair.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 * @since 2016/05/12
 */
public final class SingletonMap<K, V>
	extends AbstractMap<K, V>
{
	/** The singular entry. */
	protected final Map.Entry<K, V> entry;
	
	/** The singular entry set. */
	protected final Set<Map.Entry<K, V>> set;
	
	/** The key set. */
	protected final Set<K> keys;
	
	/** The value set. */
	protected final Set<V> values;
	
	/**
	 * Initializes the singleton map.
	 *
	 * @param __k The key to use.
	 * @param __v The value to use.
	 * @since 2016/05/12
	 */
	public SingletonMap(K __k, V __v)
	{
		// Set
		entry = new SingletonMapEntry<>(__k, __v);
		set = new SingletonSet<>(entry);
		keys = new SingletonSet<>(__k);
		values = new SingletonSet<>(__v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public boolean containsKey(Object __o)
	{
		return Objects.equals(entry.getKey(), __o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public boolean containsValue(Object __o)
	{
		return Objects.equals(entry.getValue(), __o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public Set<Map.Entry<K, V>> entrySet()
	{
		return set;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public V get(Object __o)
	{
		if (containsKey(__o))
			return entry.getValue();
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public boolean isEmpty()
	{
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public Set<K> keySet()
	{
		return keys;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public int size()
	{
		return 1;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public Set<V> values()
	{
		return values;
	}
}

