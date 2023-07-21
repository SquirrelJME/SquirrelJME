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
import cc.squirreljme.runtime.cldc.annotation.ImplementationNote;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.IteratorToEnumeration;
import cc.squirreljme.runtime.cldc.util.MapKeySetView;
import cc.squirreljme.runtime.cldc.util.SynchronizedEntrySetNotNull;

/**
 * This is similar to {@link HashMap} except that it is thread safe and does
 * not permit null keys or values.
 *
 * @since 2019/05/05
 */
@Api
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
	@Api
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
	@Api
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
	@Api
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
	@Api
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
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/05
	 */
	@Override
	public void clear()
	{
		synchronized (this)
		{
			this._map.clear();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/05
	 */
	@Override
	@SuppressWarnings({"unchecked"})
	@ImplementationNote("This creates a new instance of this class and " +
		"then places all the entries into it.")
	public Object clone()
	{
		synchronized (this)
		{
			try
			{
				// Create a new instance of this class to put into, since the
				// class is always of the same type
				Map<K, V> copy = (Map<K, V>)this.getClass().newInstance();
				
				// Copy all the elements over
				copy.putAll(this);
				
				return copy;
			}
			
			// Oops
			catch (IllegalAccessException|InstantiationException e)
			{
				/* {@squirreljme.error ZZ2r Could not clone the hashtable.} */
				throw new RuntimeException("ZZ2r", e);
			}
		}
	}
	
	/**
	 * Checks if the map contains the specified value.
	 *
	 * @param __v The value to check.
	 * @return If the map contains the value or not.
	 * @since 2019/05/05
	 */
	@SuppressWarnings("RedundantCollectionOperation")
	public boolean contains(Object __v)
	{
		synchronized (this)
		{
			return this.values().contains(__v);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/05
	 */
	@Override
	public boolean containsKey(Object __k)
	{
		synchronized (this)
		{
			return null != this._map.getEntry(__k);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/05
	 */
	@SuppressWarnings("RedundantCollectionOperation")
	@Override
	public boolean containsValue(Object __v)
	{
		synchronized (this)
		{
			return this.values().contains(__v);
		}
	}
	
	/**
	 * Returns an enumeration over the elements.
	 *
	 * @return The enumerator over the elements.
	 * @since 2019/05/05
	 */
	@Api
	public Enumeration<V> elements()
	{
		synchronized (this)
		{
			return new IteratorToEnumeration<V>(this.values().iterator());
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/05
	 */
	@Override
	public Set<Map.Entry<K, V>> entrySet()
	{
		synchronized (this)
		{
			return new SynchronizedEntrySetNotNull<K, V>(this,
				this._map.entrySet());
		}
	}
	
	@Override
	public boolean equals(Object __a)
	{
		synchronized (this)
		{
			throw Debugging.todo();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @throws NullPointerException If the requested key is null.
	 * @since 2019/05/05
	 * @param __k
	 */
	@Override
	public V get(Object __k)
		throws NullPointerException
	{
		if (__k == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			__BucketMapEntry__<K, V> e = this._map.getEntry(__k);
			if (e == null)
				return null;
			return e.getValue();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/05
	 */
	@Override
	public int hashCode()
	{
		synchronized (this)
		{
			int rv = 0;
			for (Map.Entry<K, V> e : this.entrySet())
				rv += e.hashCode();
			return rv;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/05
	 */
	@Override
	public boolean isEmpty()
	{
		synchronized (this)
		{
			return this._map.isEmpty();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/05
	 */
	@Override
	public Set<K> keySet()
	{
		return new MapKeySetView<K, V>(this, false);
	}
	
	/**
	 * Returns an enumeration over the keys.
	 *
	 * @return The key enumeration.
	 * @since 2019/05/05
	 */
	@Api
	public Enumeration<K> keys()
	{
		synchronized (this)
		{
			return new IteratorToEnumeration<K>(this.keySet().iterator());
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @throws NullPointerException If the key or value is null.
	 * @since 2019/05/05
	 */
	@Override
	public V put(K __k, V __v)
		throws NullPointerException
	{
		if (__k == null || __v == null)
			throw new NullPointerException("NARG");
		
		__BucketMap__<K, V> map = this._map;
		synchronized (this)
		{
			// To detect rehashing
			int numrehash = map._numrehash;
			
			// Set value, remember old one
			V rv = this._map.putEntry(__k).setValue(__v);
			
			// If the map was ever rehashed, then call this method
			if (map._numrehash != numrehash)
				this.rehash();
			
			// Done
			return rv;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/05
	 */
	@Override
	public void putAll(Map<? extends K, ? extends V> __m)
		throws NullPointerException
	{
		if (__m == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			// Copy everything
			for (Map.Entry<? extends K, ? extends V> e : __m.entrySet())
				this.put(e.getKey(), e.getValue());
		}
	}
	
	/**
	 * This reorganizes and reorders the entries internally in the hash map,
	 * this is called automatically when the map has been reordered.
	 *
	 * @since 2019/05/05
	 */
	@Api
	@ImplementationNote("In SquirrelJME this has no actual effect because " +
		"it is internally handled, however code might rely on overriding " +
		"this method to determine when the map has been rehashed.")
	protected void rehash()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @throws NullPointerException If a null key was specified.
	 * @since 2019/05/05
	 */
	@Override
	public V remove(Object __k)
		throws NullPointerException
	{
		if (__k == null)
			throw new NullPointerException("NARG");
		
		__BucketMap__<K, V> map = this._map;
		synchronized (this)
		{
			// To detect rehashing
			int numrehash = map._numrehash;
			
			// Remove key
			V rv = this._map.remove(__k);
			
			// If the map was ever rehashed, then call this method
			if (map._numrehash != numrehash)
				this.rehash();
			
			// Done
			return rv;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/05
	 */
	@Override
	public int size()
	{
		synchronized (this)
		{
			return this._map.size();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/05
	 */
	@Override
	public String toString()
	{
		synchronized (this)
		{
			StringBuilder sb = new StringBuilder("{");
			
			// Go through and append
			boolean comma = false;
			for (Map.Entry<?, ?> e : this.entrySet())
			{
				// Space comma
				if (comma)
					sb.append(", ");
				comma = true;
				
				// Key is equal to the value
				sb.append(e.getKey());
				sb.append('=');
				sb.append(e.getValue());
			}
			
			sb.append('}');
			return sb.toString();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/05
	 */
	@Override
	public Collection<V> values()
	{
		return new __AbstractMapValues__<K, V>(this);
	}
}

