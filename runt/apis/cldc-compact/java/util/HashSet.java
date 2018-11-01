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

public class HashSet<E>
	extends AbstractSet<E>
	implements Set<E>, Cloneable
{
	/** Internal map. */
	private final __BucketMap__<E, Object> _map;
	
	/**
	 * Initializes the set with the given capacity and load factor.
	 *
	 * @param __cap The capacity used.
	 * @param __load The load factor used.
	 * @throws IllegalArgumentException If the capacity is negative or the
	 * load factor is not positive.
	 * @since 2018/11/01
	 */
	public HashSet(int __cap, float __load)
		throws IllegalArgumentException
	{
		this._map = new __BucketMap__<E, Object>(
			(this instanceof LinkedHashSet), __cap, __load);
	}
	
	/**
	 * Initializes the set with the given capacity and the default load factor.
	 *
	 * @param __cap The capacity used.
	 * @throws IllegalArgumentException If the capacity is negative.
	 * @since 2018/11/01
	 */
	public HashSet(int __cap)
		throws IllegalArgumentException
	{
		this._map = new __BucketMap__<E, Object>(
			(this instanceof LinkedHashSet), __cap);
	}
	
	/**
	 * Initializes the set with the default capacity and load factor.
	 *
	 * @since 2018/11/01
	 */
	public HashSet()
	{
		this._map = new __BucketMap__<E, Object>(
			(this instanceof LinkedHashSet));
	}
	
	/**
	 * Initializes a set which is a copy of the other map.
	 *
	 * The default load factor is used and the capacity is set to the
	 * capacity of the input set.
	 *
	 * @param __s The set to copy from.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/07
	 */
	public HashSet(Collection<? extends E> __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Capacity is just the number of entries in the set
		this._map = new __BucketMap__<E, Object>(
			(this instanceof LinkedHashSet),
			Math.max(__BucketMap__._DEFAULT_CAPACITY, __s.size()));
		
		// Add all entries
		this.addAll(__s);
	}
	
	@Override
	public boolean add(E __a)
	{
		throw new todo.TODO();
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
			Set<E> copy = (Set<E>)this.getClass().newInstance();
			
			// Copy all the elements over
			copy.addAll(this);
			
			return copy;
		}
		
		// Oops
		catch (IllegalAccessException|InstantiationException e)
		{
			// {@squirreljme.error ZZ2w Could not clone the map.}
			throw new RuntimeException("ZZ2w", e);
		}
	}
	
	@Override
	public boolean contains(Object __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public boolean isEmpty()
	{
		throw new todo.TODO();
	}
	
	@Override
	public Iterator<E> iterator()
	{
		throw new todo.TODO();
	}
	
	@Override
	public boolean remove(Object __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public int size()
	{
		throw new todo.TODO();
	}
}

