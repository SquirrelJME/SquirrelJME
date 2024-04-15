// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.collections;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

/**
 * This represents an identity map.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 * @since 2021/11/28
 */
public final class IdentityMap<K, V>
	extends AbstractMap<K, V>
{
	/** The backing map. */
	private final Map<Identity<K>, V> _map;
	
	/**
	 * Initializes the base map.
	 * 
	 * @param __backing The backing map.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/11/28
	 */
	public IdentityMap(Map<Identity<K>, V> __backing)
		throws NullPointerException
	{
		if (__backing == null)
			throw new NullPointerException("NARG");
		
		this._map = __backing;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/28
	 */
	@Override
	public void clear()
	{
		this._map.clear();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/28
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean containsKey(Object __k)
	{
		return this._map.containsKey(new Identity<K>((K)__k));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/28
	 */
	@Override
	public Set<Entry<K, V>> entrySet()
	{
		return new __IdentityKeySetView__<K, V>(this._map.entrySet());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/28
	 * @param __k
	 */
	@SuppressWarnings("unchecked")
	@Override
	public V get(Object __k)
	{
		return this._map.get(new Identity<K>((K)__k));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/28
	 */
	@Override
	public V put(K __k, V __v)
	{
		return this._map.put(new Identity<K>(__k), __v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/28
	 */
	@Override
	public int size()
	{
		return this._map.size();
	}
}
