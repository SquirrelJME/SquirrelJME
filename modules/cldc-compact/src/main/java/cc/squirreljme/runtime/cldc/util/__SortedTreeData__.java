// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.util;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

/**
 * This stores a key and value pair which is referenced by a node.
 *
 * @since 2017/03/30
 */
class __SortedTreeData__<K, V>
	implements Map.Entry<K, V>
{
	/** The comparison method to use. */
	final Comparator<K> _compare;
	
	/** The key for this data. */
	final K _key;
	
	/** The value of the data. */
	volatile V _value;
	
	/** The node owning this data (used only to detect modification). */
	volatile __SortedTreeNode__<K, V> _node;
	
	/** The data before this one. */
	volatile __SortedTreeData__<K, V> _prev;
	
	/** The data after this one. */
	volatile __SortedTreeData__<K, V> _next;
	
	/**
	 * Initializes the data.
	 *
	 * @param __m The owning map.
	 * @param __k The key used for this data.
	 * @param __v The value to initially store.
	 * @throws NullPointerException If no map was specified.
	 * @since 2017/03/30
	 */
	__SortedTreeData__(SortedTreeMap<K, V> __m, K __k, V __v)
		throws NullPointerException
	{
		// Check
		if (__m == null)
			throw new NullPointerException("NARG");
		
		this._compare = __m._compare;
		this._key = __k;
		this._value = __v;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/07
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Must be another entry
		if (!(__o instanceof Map.Entry))
			return false;
		
		// Compare
		Map.Entry<?, ?> o = (Map.Entry<?, ?>)__o;
		return Objects.equals(this._key, o.getKey()) &&
			Objects.equals(this._value, o.getValue());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/07
	 */
	@Override
	public K getKey()
	{
		return this._key;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/07
	 */
	@Override
	public V getValue()
	{
		return this._value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/07
	 */
	@Override
	public int hashCode()
	{
		return Objects.hashCode(this._key) ^ Objects.hashCode(this._value);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/07
	 */
	@Override
	public V setValue(V __v)
	{
		V rv = this._value;
		this._value = __v;
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/07
	 */
	@Override
	public String toString()
	{
		return String.format("[%s, %s]", this._key, this._value);
	}
	
	/**
	 * Compares this data against the given key.
	 *
	 * @param __k The key to compare against.
	 * @return The comparison result.
	 * @since 2017/03/30
	 */
	final int __compare(K __k)
	{
		return this._compare.compare(this._key, __k);
	}
	
	/**
	 * Compares this data against the given data.
	 *
	 * @param __d The data to compare against.
	 * @return The comparison result.
	 * @since 2017/03/30
	 */
	final int __compare(__SortedTreeData__<K, V> __d)
	{
		return this.__compare(__d._key);
	}
	
	/**
	 * Compares this data against the given node.
	 *
	 * @param __n The node to compare against.
	 * @return The comparison result.
	 * @since 2017/03/30
	 */
	final int __compare(__SortedTreeNode__<K, V> __n)
	{
		return this.__compare(__n._data);
	}
}

