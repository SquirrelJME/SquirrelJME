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

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class WeakHashMap<K, V>
	extends AbstractMap<K, V>
	implements Map<K, V>
{
	/** The default capacity. */
	private static final int _DEFAULT_CAPACITY =
		16;
	
	/** The default load factor. */
	private static final float _DEFAULT_LOAD =
		0.75F;
	
	/** The load factor. */
	private final float _load;
	
	/** This is used to clear keys when they are collected. */
	private final ReferenceQueue<K> _rq =
		new ReferenceQueue<>();
	
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
	public WeakHashMap(int __icap, float __load)
		throws IllegalArgumentException
	{
		// {@squirreljme.error ZZ25 The initial capacity of the weak
		// hash map is negative. (The negative initial capacity)}
		if (__icap < 0)
			throw new IllegalArgumentException(String.format("ZZ25 %d",
				__icap));
		
		// {@squirreljme.error ZZ26 The load factor of the weak hash map is
		// not positive. (The non-positive load factor)}
		if (__load <= 0.0F)
			throw new IllegalArgumentException(String.format("ZZ26 %f",
				__load));
		
		// Setup
		_load = __load;
	}
	
	/**
	 * Initializes the weak hash map with the given initial capacity.
	 *
	 * @param __icap The initial weak hash map capacity.
	 * @throws IllegalArgumentException If the capacity is negative.
	 * @since 2016/04/19
	 */
	public WeakHashMap(int __icap)
		throws IllegalArgumentException
	{
		this(__icap, _DEFAULT_LOAD);
	}
	
	/**
	 * Initializes the weak hash map with the default initial capacity and load
	 * factor.
	 *
	 * @since 2016/04/19
	 */
	public WeakHashMap()
	{
		this(_DEFAULT_CAPACITY, _DEFAULT_LOAD);
	}
	
	/**
	 * Initializes the weak hash map with the default initial capacity and load
	 * factor and then copies the keys and values of the given map to this one.
	 *
	 * @param __a The map to source entries from.
	 * @throws NullPointerException If no map was specified.
	 * @since 2016/04/19
	 */
	public WeakHashMap(Map<? extends K, ? extends V> __a)
	{
		// {@squirreljme.error ZZ27 No map to copy data from was specified.}
		if (__a == null)
			throw new NullPointerException("ZZ27");
		
		// Setup initial map
		_load = _DEFAULT_LOAD;
		
		// Add all entries to it
		for (Map.Entry<? extends K, ? extends V> e : __a.entrySet())
			put(e.getKey(), e.getValue());
	}
	
	@Override
	public void clear()
	{
		throw new todo.TODO();
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
	
	@Override
	public V get(Object __a)
	{
		throw new todo.TODO();
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
	
	@Override
	public V put(K __a, V __b)
	{
		throw new todo.TODO();
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

