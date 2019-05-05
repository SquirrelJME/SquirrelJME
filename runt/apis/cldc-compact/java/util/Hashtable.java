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
	
	public Hashtable(int __a)
	{
		super();
		throw new todo.TODO();
	}
	
	public Hashtable()
	{
		super();
		throw new todo.TODO();
	}
	
	public Hashtable(Map<? extends K, ? extends V> __a)
	{
		super();
		throw new todo.TODO();
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
	
	protected void rehash()
	{
		throw new todo.TODO();
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

