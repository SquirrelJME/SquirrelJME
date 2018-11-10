// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.runtime.cldc.annotation.ImplementationNote;

/**
 * This is a hash table where keys are mapped to values.
 *
 * This class is not thread safe.
 *
 * If keys are added or removed during iteration then
 * {@link ConcurrentModificationException} will be thrown.
 *
 * @since 2018/10/07
 */
public class HashMap<K, V>
	extends AbstractMap<K, V>
	implements Map<K, V>, Cloneable
{
	/** Internal map. */
	private final __BucketMap__<K, V> _map;
	
	/**
	 * Initializes the map with the given capacity and load factor.
	 *
	 * @param __cap The capacity used.
	 * @param __load The load factor used.
	 * @throws IllegalArgumentException If the capacity is negative or the
	 * load factor is not positive.
	 * @since 2018/10/07
	 */
	public HashMap(int __cap, float __load)
		throws IllegalArgumentException
	{
		this._map = new __BucketMap__<K, V>((this instanceof LinkedHashMap),
			false, __cap, __load);
	}
	
	/**
	 * Initializes the map with the given capacity and the default load factor.
	 *
	 * @param __cap The capacity used.
	 * @throws IllegalArgumentException If the capacity is negative.
	 * @since 2018/10/07
	 */
	public HashMap(int __cap)
		throws IllegalArgumentException
	{
		this._map = new __BucketMap__<K, V>((this instanceof LinkedHashMap),
			__cap);
	}
	
	/**
	 * Initializes the map with the default capacity and load factor.
	 *
	 * @since 2018/10/07
	 */
	public HashMap()
	{
		this._map = new __BucketMap__<K, V>((this instanceof LinkedHashMap));
	}
	
	/**
	 * Initializes a map which is a copy of the other map.
	 *
	 * The default load factor is used and the capacity is set to the
	 * capacity of the input map.
	 *
	 * @param __m The map to copy from.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/07
	 */
	public HashMap(Map<? extends K, ? extends V> __m)
		throws NullPointerException
	{
		if (__m == null)
			throw new NullPointerException("NARG");
		
		// Capacity is just the number of entries in the map
		this._map = new __BucketMap__<K, V>((this instanceof LinkedHashMap),
			Math.max(__BucketMap__._DEFAULT_CAPACITY, __m.size()));
		
		// Put all entries
		this.putAll(__m);
	}
	
	/**
	 * Initializes the map with the given capacity and load factor in the
	 * given access order.
	 *
	 * This is an internal constructor.
	 *
	 * @param __cap The capacity used.
	 * @param __load The load factor used.
	 * @param __ao Is access order used?
	 * @throws IllegalArgumentException If the capacity is negative or the
	 * load factor is not positive.
	 * @since 2018/11/01
	 */
	HashMap(int __cap, float __load, boolean __ao)
		throws IllegalArgumentException
	{
		this._map = new __BucketMap__<K, V>((this instanceof LinkedHashMap),
			__ao, __cap, __load);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/01
	 */
	@Override
	public void clear()
	{
		this._map.clear();
	}
	
	/**
	 * Returns a shallow copy of this map which just shares the same key and
	 * value mappings, the actual keys and values are not cloned.
	 *
	 * {@inheritDoc}
	 * @since 2018/11/01
	 */
	@Override
	@SuppressWarnings({"unchecked"})
	@ImplementationNote("This creates a new instance of this class and " +
		"then places all the entries into it.")
	public Object clone()
	{
		try
		{
			// Create a new instance of this class to put into, since the class
			// is always of the same type
			Map<K, V> copy = (Map<K, V>)this.getClass().newInstance();
			
			// Copy all the elements over
			copy.putAll(this);
			
			return copy;
		}
		
		// Oops
		catch (IllegalAccessException|InstantiationException e)
		{
			// {@squirreljme.error ZZ1v Could not clone the map.}
			throw new RuntimeException("ZZ1v", e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/26
	 */
	@Override
	public boolean containsKey(Object __k)
	{
		return null != this._map.getEntry(__k);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/07
	 */
	@Override
	@SuppressWarnings({"unchecked"})
	public Set<Map.Entry<K, V>> entrySet()
	{
		return this._map.entrySet();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/10
	 */
	@Override
	public V get(Object __k)
	{
		__BucketMapEntry__<K, V> e = this._map.getEntry(__k);
		if (e == null)
			return null;
		return e.getValue();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/07
	 */
	@Override
	public V put(K __k, V __v)
	{
		// Just operates on that key
		return this._map.putEntry(__k).setValue(__v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/01
	 */
	@Override
	public V remove(Object __k)
	{
		return this._map.remove(__k);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/13
	 */
	@Override
	public int size()
	{
		return this._map.size();
	}
}


