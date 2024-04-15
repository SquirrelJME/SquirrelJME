// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * This is a map which is backed by a hash table except that the iterator
 * order is in the order of which elements were added first.
 *
 * Otherwise this class is exactly the same as {@link HashMap}.
 *
 * @param <K> The key type.
 * @param <V> The value type.
 * @see HashMap
 * @since 2019/05/09
 */
@Api
public class LinkedHashMap<K, V>
	extends HashMap<K, V>
	implements Map<K, V>
{
	/**
	 * Initializes the set with the given capacity and load factor.
	 *
	 * @param __cap The capacity used.
	 * @param __load The load factor used.
	 * @throws IllegalArgumentException If the capacity is negative or the
	 * load factor is not positive.
	 * @since 2019/05/09
	 */
	@Api
	public LinkedHashMap(int __cap, float __load)
		throws IllegalArgumentException
	{
		super(__cap, __load);
	}
	
	/**
	 * Initializes the set with the given capacity and the default load factor.
	 *
	 * @param __initcap The capacity used.
	 * @throws IllegalArgumentException If the capacity is negative.
	 * @since 2019/05/09
	 */
	@Api
	public LinkedHashMap(int __initcap)
		throws IllegalArgumentException
	{
		super(__initcap);
	}
	
	/**
	 * Initializes an empty map.
	 *
	 * @since 2019/05/09
	 */
	@Api
	public LinkedHashMap()
	{
	}
	
	/**
	 * Initializes a map which is a copy of the other map.
	 *
	 * The default load factor is used and the capacity is set to the
	 * capacity of the input map.
	 *
	 * @param __m The map to copy from.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/09
	 */
	@Api
	public LinkedHashMap(Map<? extends K, ? extends V> __m)
		throws NullPointerException
	{
		super(__m);
	}
	
	/**
	 * Initializes the set with the given capacity and load factor, but the
	 * may be ordered based on what is first accessed.
	 *
	 * @param __cap The capacity used.
	 * @param __load The load factor used.
	 * @param __ao If the iterator should always return the same order for
	 * accesses.
	 * @throws IllegalArgumentException If the capacity is negative or the
	 * load factor is not positive.
	 * @since 2019/05/09
	 */
	@Api
	public LinkedHashMap(int __cap, float __load, boolean __ao)
	{
		super(__cap, __load, __ao);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/09
	 */
	@Override
	public V put(K __k, V __v)
	{
		__BucketMap__<K, V> map = this._map;
		
		// Put entry into the map and store the old value
		__BucketMapEntry__<K, V> entry = map.putEntry(__k);
		V rv = entry.setValue(__v);
		
		// Remove the eldest entry (which is the oldest/first item in the map)
		__BucketMapEntry__<K, V> eldest = map._links.peekFirst();
		if (eldest != null && this.removeEldestEntry(eldest))
			map.removeEntry(eldest.getKey(), false);
		
		// Return the former value
		return rv;
	}
	
	/**
	 * After a {@code put} or {@code putAll} operation this will be called with
	 * the eldest entry to determine if it should be removed or not. This may
	 * be used for cache purposes.
	 *
	 * The default implementation returns {@code false}.
	 *
	 * If the implementation of this method modifies the map then this should
	 * always return {@code null}.
	 *
	 * @param __e The entry being
	 * @return If the map was modified.
	 * @since 2019/05/09
	 */
	@Api
	protected boolean removeEldestEntry(Map.Entry<K, V> __e)
	{
		return false;
	}
}

