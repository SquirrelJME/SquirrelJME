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
import cc.squirreljme.runtime.cldc.util.MapKeySetView;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

@Api
public abstract class AbstractMap<K, V>
	implements Map<K, V>
{
	/** Key set cache. */
	private volatile Reference<Set<K>> _keySetCache;
	
	/** Values cache. */
	private volatile Reference<Collection<V>> _valuesCache;
	
	@Api
	protected AbstractMap()
	{
	}
	
	@Override
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
	
	/**
	 * {@inheritDoc}
	 * @since 2023/02/09
	 */
	@Override
	public boolean containsKey(Object __key)
	{
		// Manual scan through to find it
		for (Map.Entry<K, V> e : this.entrySet())
			if (Objects.equals(e.getKey(), __key))
				return true;
		
		// Not found
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/02/09
	 */
	@Override
	public boolean containsValue(Object __value)
	{
		// Manual scan through to find it
		for (Map.Entry<K, V> e : this.entrySet())
			if (Objects.equals(e.getValue(), __value))
				return true;
		
		// Not found
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/12
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Quick?
		if (this == __o)
			return true;
		
		// Not another map?
		if (!(__o instanceof Map))
			return false;
		
		// Compares on the entry set
		return this.entrySet().equals(((Map<?, ?>)__o).entrySet());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/12
	 * @param __key
	 */
	@Override
	public V get(Object __key)
	{
		// Manual scan through to find it
		for (Map.Entry<K, V> e : this.entrySet())
			if (Objects.equals(e.getKey(), __key))
				return e.getValue();
		
		// Not found
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/09
	 */
	@Override
	public int hashCode()
	{
		int rv = 0;
		for (Map.Entry<K, V> e : this.entrySet())
			rv += e.hashCode();
		return rv;
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
		Reference<Set<K>> ref = this._keySetCache;
		Set<K> rv;
		if (ref == null || (rv = ref.get()) == null)
		{
			rv = new MapKeySetView<K, V>(this, false);
			this._keySetCache = new WeakReference<>(rv);
		}
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/02/09
	 */
	@Override
	public V put(K __a, V __b)
	{
		throw new UnsupportedOperationException("RORO");
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
		Reference<Collection<V>> ref = this._valuesCache;
		Collection<V> rv;
		
		if (ref == null || (rv = ref.get()) == null)
		{
			rv = new __AbstractMapValues__<K, V>(this);
			this._valuesCache = new WeakReference<>(rv);
		}
		
		return rv;
	}
}


