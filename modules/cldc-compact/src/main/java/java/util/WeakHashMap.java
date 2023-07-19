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
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * This is a variant of {@link HashMap} which is backed by keys that are
 * {@link WeakReference}s.
 * 
 * @param <K> The key type.
 * @param <V> The value type.
 * @since 2023/02/09
 */
@Api
public class WeakHashMap<K, V>
	extends AbstractMap<K, V>
{
	/** Internal map. */
	private final __BucketMap__<__WeakHashMapKey__<K>, V> _map;
	
	/** The load factor. */
	private final float _load;
	
	/** Entry set cache. */
	private volatile Reference<Set<Entry<K, V>>> _entrySetCache; 
	
	/**
	 * Initializes the weak hash map with the given initial capacity and load
	 * factor.
	 *
	 * @param __icap The initial capacity.
	 * @param __load The initial load factor.
	 * @throws IllegalArgumentException If the capacity is negative or the
	 * load factor is not positive.
	 * @since 2016/04/19
	 */
	@Api
	public WeakHashMap(int __icap, float __load)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error ZZ33 The initial capacity of the weak
		hash map is negative. (The negative initial capacity)} */
		if (__icap < 0)
			throw new IllegalArgumentException(String.format("ZZ33 %d",
				__icap));
		
		/* {@squirreljme.error ZZ34 The load factor of the weak hash map is
		not positive. (The non-positive load factor)} */
		if (__load <= 0.0F)
			throw new IllegalArgumentException(String.format("ZZ34 %f",
				__load));
		
		// Setup
		this._load = __load;
		this._map = new __BucketMap__<>(false, false,
			__icap, __load);
	}
	
	/**
	 * Initializes the weak hash map with the given initial capacity.
	 *
	 * @param __icap The initial weak hash map capacity.
	 * @throws IllegalArgumentException If the capacity is negative.
	 * @since 2016/04/19
	 */
	@Api
	public WeakHashMap(int __icap)
		throws IllegalArgumentException
	{
		this(__icap, __BucketMap__._DEFAULT_LOAD);
	}
	
	/**
	 * Initializes the weak hash map with the default initial capacity and load
	 * factor.
	 *
	 * @since 2016/04/19
	 */
	@Api
	public WeakHashMap()
	{
		this(__BucketMap__._DEFAULT_CAPACITY, __BucketMap__._DEFAULT_LOAD);
	}
	
	/**
	 * Initializes the weak hash map with the default initial capacity and load
	 * factor and then copies the keys and values of the given map to this one.
	 *
	 * @param __a The map to source entries from.
	 * @throws NullPointerException If no map was specified.
	 * @since 2016/04/19
	 */
	@Api
	public WeakHashMap(Map<? extends K, ? extends V> __a)
	{
		/* {@squirreljme.error ZZ35 No map to copy data from was specified.} */
		if (__a == null)
			throw new NullPointerException("ZZ35");
		
		// Setup initial map
		this._load = __BucketMap__._DEFAULT_LOAD;
		this._map = new __BucketMap__<>(false, false,
			__BucketMap__._DEFAULT_CAPACITY, __BucketMap__._DEFAULT_LOAD);
		
		// Add all entries to it
		this.putAll(__a);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/02/09
	 */
	@Override
	public void clear()
	{
		this._map.clear();
	}
	
	@Override
	public Object clone()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/02/09
	 */
	@Override
	public Set<Map.Entry<K, V>> entrySet()
	{
		Reference<Set<Entry<K, V>>> ref = this._entrySetCache;
		Set<Map.Entry<K, V>> rv;
		
		if (ref == null || (rv = ref.get()) == null)
		{
			rv = new __WeakHashMapEntrySet__<K, V>(
				this._map.entrySet());
			this._entrySetCache = new WeakReference<>(rv);
		}
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/02/09
	 */
	@Override
	public V put(K __key, V __value)
	{
		return this._map.putEntry(new __WeakHashMapKey__<K>(__key))
			.setValue(__value);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/02/09
	 */
	@Override
	public int size()
	{
		return this._map._size;
	}
}

