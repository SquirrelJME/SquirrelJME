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
 * This is similar to {@link HashMap} except that it is thread safe and does
 * not permit null keys or values.
 *
 * @since 2019/05/05
 */
public class Hashtable<K, V>
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
	 * @since 2019/05/05
	 */
	public Hashtable(int __cap, float __load)
	{
		this._map = new __BucketMap__<K, V>(false, false, __cap, __load);
	}
	
	/**
	 * Initializes the map with the given capacity and the default load factor.
	 *
	 * @param __cap The capacity used.
	 * @throws IllegalArgumentException If the capacity is negative.
	 * @since 2019/05/05
	 */
	public Hashtable(int __cap)
		throws IllegalArgumentException
	{
		this._map = new __BucketMap__<K, V>(false, __cap);
	}
	
	/**
	 * Initializes the map with the default capacity and load factor.
	 *
	 * @since 2019/05/05
	 */
	public Hashtable()
	{
		this._map = new __BucketMap__<K, V>(false);
	}
	
	/**
	 * Initializes a map which is a copy of the other map.
	 *
	 * The default load factor is used and the capacity is set to the
	 * capacity of the input map.
	 *
	 * @param __m The map to copy from.
	 * @throws NullPointerException If {@code __m} is null or it contains
	 * null keys or values.
	 * @since 2018/10/07
	 */
	public Hashtable(Map<? extends K, ? extends V> __m)
		throws NullPointerException
	{
		if (__m == null)
			throw new NullPointerException("NARG");
		
		// Capacity is just the number of entries in the map
		this._map = new __BucketMap__<K, V>(false,
			Math.max(__BucketMap__._DEFAULT_CAPACITY, __m.size()));
		
		// Put all entries
		this.putAll(__m);
	}
	
	public void clear()
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	@Override
	@SuppressWarnings({"unchecked"})
	@ImplementationNote("This creates a new instance of this class and " +
		"then places all the entries into it.")
	public Object clone()
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	public boolean contains(Object __a)
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	public boolean containsKey(Object __a)
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	public boolean containsValue(Object __a)
	{
		throw new todo.TODO();
	}
	
	public Enumeration<V> elements()
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	public Set<Map.Entry<K, V>> entrySet()
	{
		throw new todo.TODO();
	}
	
	@Override
	public boolean equals(Object __a)
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	@Override
	public V get(Object __a)
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	public V getOrDefault(Object __a, V __b)
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	@Override
	public int hashCode()
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	@Override
	public boolean isEmpty()
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	public Set<K> keySet()
	{
		throw new todo.TODO();
	}
	
	public Enumeration<K> keys()
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	@Override
	public V put(K __a, V __b)
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	public void putAll(Map<? extends K, ? extends V> __a)
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	public V putIfAbsent(K __a, V __b)
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	/**
	 * This reorganizes and reorders the entries internally in the hash map,
	 * this is called automatically when the map has been reordered.
	 *
	 * @since 2019/05/05
	 */
	@ImplementationNote("In SquirrelJME this has no actual effect because " +
		"it is internally handled, however code might rely on overriding " +
		"this method to determine when the map has been rehashed.")
	protected void rehash()
	{
	}
	
	@Override
	public V remove(Object __a)
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	public boolean remove(Object __a, Object __b)
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	public boolean replace(K __a, V __b, V __c)
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	public V replace(K __a, V __b)
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	@Override
	public int size()
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	@Override
	public String toString()
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	public Collection<V> values()
	{
		throw new todo.TODO();
	}
}

