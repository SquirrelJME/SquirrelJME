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
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/05
	 */
	@Override
	public void clear()
	{
		this.entrySet().clear();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/07
	 */
	@Override
	protected Object clone()
		throws CloneNotSupportedException
	{
		return (AbstractMap<?, ?>)super.clone();
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
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/04
	 */
	@Override
	public boolean isEmpty()
	{
		return this.size() == 0;
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
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/04
	 */
	@Override
	public V remove(Object __k)
	{
		// Linearly search through the hash map to remove the key
		for (Iterator<Map.Entry<K, V>> it = this.entrySet().iterator();
			it.hasNext();)
		{
			Map.Entry<K, V> e = it.next();
			
			// If the key matches, then it is removed
			if (Objects.equals(e.getKey(), __k))
			{
				V rv = e.getValue();
				it.remove();
				return rv;
			}
		}
		
		// If this point was reached, there is no matching key
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/04
	 */
	@Override
	public int size()
	{
		return this.entrySet().size();
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


