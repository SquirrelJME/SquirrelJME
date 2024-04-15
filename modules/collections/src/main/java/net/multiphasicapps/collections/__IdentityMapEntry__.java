// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.collections;

import java.util.Map;

/**
 * Wraps a single entry within an identity map.
 *
 * @since 2021/11/28
 */
final class __IdentityMapEntry__<K, V>
	implements Map.Entry<K, V>
{
	/** The entry to wrap. */
	private final Map.Entry<Identity<K>, V> _entry;
	
	/**
	 * Initializes the wrapped entry.
	 * 
	 * @param __entry The entry to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/11/28
	 */
	__IdentityMapEntry__(Map.Entry<Identity<K>, V> __entry)
		throws NullPointerException
	{
		if (__entry == null)
			throw new NullPointerException("NARG");
		
		this._entry = __entry;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/28
	 */
	@Override
	public K getKey()
	{
		Identity<K> key = this._entry.getKey();
		return (key == null ? null : key.value);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/28
	 */
	@Override
	public V getValue()
	{
		return this._entry.getValue();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/28
	 */
	@Override
	public V setValue(V __v)
	{
		return this._entry.setValue(__v);
	}
}
