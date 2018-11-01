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

public abstract class AbstractMap<K, V>
	implements Map<K, V>
{
	protected AbstractMap()
	{
	}
	
	public abstract Set<Map.Entry<K, V>> entrySet();
	
	public void clear()
	{
		throw new todo.TODO();
	}
	
	@Override
	protected Object clone()
		throws CloneNotSupportedException
	{
		if (false)
			throw new CloneNotSupportedException();
		throw new todo.TODO();
	}
	
	public boolean containsKey(Object __a)
	{
		throw new todo.TODO();
	}
	
	public boolean containsValue(Object __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public boolean equals(Object __a)
	{
		throw new todo.TODO();
	}
	
	public V get(Object __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public int hashCode()
	{
		throw new todo.TODO();
	}
	
	public boolean isEmpty()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/10
	 */
	@Override
	public Set<K> keySet()
	{
		return new __AbstractMapKeySet__<K, V>(this);
	}
	
	public V put(K __a, V __b)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/13
	 */
	@Override
	public void putAll(Map<? extends K, ? extends V> __m)
		throws NullPointerException
	{
		if (__m == null)
			throw new NullPointerException("NARG");
		
		// Copy everything
		for (Map.Entry<? extends K, ? extends V> e : __m.entrySet())
			this.put(e.getKey(), e.getValue());
	}
	
	public V remove(Object __a)
	{
		throw new todo.TODO();
	}
	
	public int size()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/13
	 */
	@Override
	public String toString()
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
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/29
	 */
	@Override
	public Collection<V> values()
	{
		return new __AbstractMapValues__<K, V>(this);
	}
}


