// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.util.sorted;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

/**
 * This is a sorted map which is internally implemented by using
 * {@link SortedTreeSet} and special handlers.
 *
 * The entries returned by the entry set do not support setting values.
 *
 * @param <K> The type of key to store.
 * @param <V> The type of value to store.
 * @since 2016/09/06
 */
public class SortedTreeMap<K, V>
	extends AbstractMap<K, V>
{
	/** The backing set. */
	private final SortedTreeSet<Object> _set;
	
	/**
	 * Initializes a new empty map using the natural comparator.
	 *
	 * @since 2016/09/06
	 */
	public SortedTreeMap()
	{
		this(__Natural__.<K>instance());
	}
	
	/**
	 * Initializes a map using the natural comparator where values are copied
	 * from the specified map.
	 *
	 * @param __m The map to copy from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/06
	 */
	@SuppressWarnings({"unchecked"})
	public SortedTreeMap(Map<? extends Comparable<K>, ? extends V> __m)
		throws NullPointerException
	{
		this(__Natural__.<K>instance(), (Map<? extends K, ? extends V>)__m);
	}
	
	/**
	 * Initializes a new empty map using the given comparator.
	 *
	 * @param __comp The comparator to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/06
	 */
	public SortedTreeMap(Comparator<? extends K> __comp)
		throws NullPointerException
	{
		// Check
		if (__comp == null)
			throw new NullPointerException("NARG");
		
		// Create set
		SortedTreeSet<Object> set = new SortedTreeSet<Object>(
			new __MapKeyComparator__<K>(__comp));
		this._set = set;
	}
	
	/**
	 * Initializes a map using the given comparator where values are copied
	 * from the specified map.
	 *
	 * @param __comp The comparator to use for key sorts.
	 * @param __m The map to copy from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/06
	 */
	public SortedTreeMap(Comparator<? extends K> __comp,
		Map<? extends K, ? extends V> __m)
		throws NullPointerException
	{
		// Check
		if (__comp == null || __m == null)
			throw new NullPointerException("NARG");
		
		// Create set
		SortedTreeSet<Object> set = new SortedTreeSet<Object>(
			new __MapKeyComparator__<K>(__comp));
		this._set = set;
		
		// Put everything
		putAll(__m);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/06
	 */
	@Override
	public Set<Map.Entry<K, V>> entrySet()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/06
	 */
	@Override
	@SuppressWarnings({"unchecked"})
	public V put(K __k, V __v)
	{
		// Get the backing set
		SortedTreeSet<__MapKey__> set = this._set;
		
		// If the node already exists then set the value of it
		__Node__<__MapKey__> node = set.__findNode(__k);
		__MapKey__ key;
		if (node != null)
			key = (__MapKey__)node._value;
		
		// Otherwise add it
		else
		{
			key = new __MapKey__(__k);
			set.add(key);
		}
		
		// Change values
		V old = (V)key._value;
		key._value = __v;
		return old;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/06
	 */
	@Override
	public int size()
	{
		return this._set.size();
	}
}

