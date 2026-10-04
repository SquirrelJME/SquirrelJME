// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.util;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;

/**
 * This is the key set for an abstract map.
 *
 * @param <K> The key type.
 * @param <V> The value stored.
 * @since 2018/10/10
 */
public final class MapKeySetView<K, V>
	extends AbstractSet<K>
{
	/** The backing map. */
	protected final Map<K, V> map;
	
	/** Is adding allowed? */
	protected final boolean allowAdd;
	
	/**
	 * Initializes the set.
	 *
	 * @param __map The backing map
	 * @param __allowAdd Is adding allowed in the map?
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/01
	 */
	public MapKeySetView(Map<K, V> __map, boolean __allowAdd)
		throws NullPointerException
	{
		if (__map == null)
			throw new NullPointerException("NARG");
		
		this.map = __map;
		this.allowAdd = __allowAdd;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/28
	 */
	@Override
	public boolean add(K __k)
	{
		if (!this.allowAdd)
			throw new UnsupportedOperationException("RORO");
		
		// Store a null value here
		Map<K, V> map = this.map;
		boolean contained = map.containsKey(__k);
		map.put(__k, null);
		
		// Only if it is actually contained here
		return !contained;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/01
	 */
	@SuppressWarnings("SuspiciousMethodCalls")
	@Override
	public final boolean contains(Object __o)
	{
		return this.map.containsKey(__o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/10
	 */
	@Override
	public final Iterator<K> iterator()
	{
		return new MapKeySetIterator<K, V>(this.map.entrySet().iterator());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/11/28
	 */
	@SuppressWarnings("SuspiciousMethodCalls")
	@Override
	public boolean remove(Object __o)
	{
		// Remove the item from the map, but check to ensure it has it before
		// that is actually done
		Map<K, V> map = this.map;
		boolean hasKey = map.containsKey(__o);
		map.remove(__o);
		
		return hasKey;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/10
	 */
	@Override
	public final int size()
	{
		return this.map.size();
	}
}

