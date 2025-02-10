// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.collections;

import java.util.Iterator;
import java.util.Map;

/**
 * An iterator over a key set view for identity maps.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 * @since 2021/11/28
 */
final class __IdentityKeySetIterator__<K, V>
	implements Iterator<Map.Entry<K, V>>
{
	/** The iterator over map entries. */
	private final Iterator<Map.Entry<Identity<K>, V>> _iterator;
	
	/**
	 * Initializes the iterator for identity items.
	 * 
	 * @param __iterator The iterator to move over.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/11/28
	 */
	__IdentityKeySetIterator__(
		Iterator<Map.Entry<Identity<K>, V>> __iterator)
		throws NullPointerException
	{
		if (__iterator == null)
			throw new NullPointerException("NARG");
		
		this._iterator = __iterator;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/28
	 */
	@Override
	public boolean hasNext()
	{
		return this._iterator.hasNext();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/28
	 */
	@Override
	public Map.Entry<K, V> next()
	{
		return new __IdentityMapEntry__<K, V>(this._iterator.next());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/28
	 */
	@Override
	public void remove()
	{
		this._iterator.remove();
	}
}
