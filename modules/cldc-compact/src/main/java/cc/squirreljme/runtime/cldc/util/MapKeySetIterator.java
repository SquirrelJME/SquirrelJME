// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
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
 * This is the iterator over the map's key set.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 * @since 2018/11/01
 */
public final class MapKeySetIterator<K, V>
	implements Iterator<K>
{
	/** The entry set iterator. */
	protected final Iterator<Map.Entry<K, V>> iterator;
	
	/**
	 * Initializes the iterator.
	 *
	 * @param __it The backing iterator.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/01
	 */
	public MapKeySetIterator(Iterator<Map.Entry<K, V>> __it)
		throws NullPointerException
	{
		if (__it == null)
			throw new NullPointerException("NARG");
		
		this.iterator = __it;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2018/11/01
	 */
	@Override
	public boolean hasNext()
	{
		return this.iterator.hasNext();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2018/11/01
	 */
	@Override
	public K next()
		throws NoSuchElementException
	{
		return this.iterator.next().getKey();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2018/11/01
	 */
	@Override
	public void remove()
	{
		this.iterator.remove();
	}
}
