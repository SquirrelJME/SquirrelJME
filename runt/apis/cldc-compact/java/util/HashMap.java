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
		this._map = new __BucketMap__<K, V>(__cap, __load);
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
		this._map = new __BucketMap__<K, V>(__cap);
	}
	
	/**
	 * Initializes the map with the default capacity and load factor.
	 *
	 * @since 2018/10/07
	 */
	public HashMap()
	{
		this._map = new __BucketMap__<K, V>();
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
		this._map = new __BucketMap__<K, V>(
			Math.max(__BucketMap__._DEFAULT_CAPACITY, __m.size()));
		
		throw new todo.TODO();
	}
	
	@Override
	public void clear()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns a shallow copy of this map which just shares the same key and
	 * value mappings, the actual keys and values are not cloned.
	 *
	 * {@inheritDoc}
	 * @since 2018/10/10
	 */
	@Override
	public Object clone()
	{
		return new HashMap<>(this);
	}
	
	@Override
	public boolean containsKey(Object __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public boolean containsValue(Object __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public Set<Map.Entry<K, V>> entrySet()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/10
	 */
	@Override
	public V get(Object __k)
	{
		__BucketMap__.__Entry__<K, V> e = this._map.get(__k);
		if (e == null)
			return null;
		return e.getValue();
	}
	
	@Override
	public boolean isEmpty()
	{
		throw new todo.TODO();
	}
	
	@Override
	public Set<K> keySet()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/07
	 */
	@Override
	public V put(K __k, V __v)
	{
		// Just operates on that key
		return this._map.put(__k).setValue(__v);
	}
	
	@Override
	public void putAll(Map<? extends K, ? extends V> __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public V remove(Object __a)
	{
		throw new todo.TODO();
	}
	
	public boolean remove(Object __a, Object __b)
	{
		throw new todo.TODO();
	}
	
	@Override
	public int size()
	{
		throw new todo.TODO();
	}
	
	@Override
	public Collection<V> values()
	{
		throw new todo.TODO();
	}
}


