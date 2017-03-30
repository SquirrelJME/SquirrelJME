// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.util.sorted;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

/**
 * This is a sorted map which is internally implemented by using
 * {@link SortedTreeSet} and special handlers.
 *
 * @param <K> The type of key to store.
 * @param <V> The type of value to store.
 * @since 2016/09/06
 */
public class SortedTreeMap<K, V>
	extends AbstractMap<K, V>
{
	/** The comparison method to use. */
	private final Comparator<K> _compare;
	
	/** The entry set. */
	private volatile Reference<Set<Map.Entry<K, V>>> _entryset;
	
	/** The root node. */
	volatile __Node__<K, V> _root;
	
	/** The minimum value. */
	volatile __Data__<K, V> _min;
	
	/** The size of the tree. */
	volatile int _size;
	
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
	@SuppressWarnings({"unchecked"})
	public SortedTreeMap(Comparator<? extends K> __comp)
		throws NullPointerException
	{
		// Check
		if (__comp == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._compare = (Comparator<K>)__comp;
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
	@SuppressWarnings({"unchecked"})
	public SortedTreeMap(Comparator<? extends K> __comp,
		Map<? extends K, ? extends V> __m)
		throws NullPointerException
	{
		// Check
		if (__comp == null || __m == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._compare = (Comparator<K>)__comp;
		
		// Put everything
		putAll(__m);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/07
	 */
	@Override
	public void clear()
	{
		// Clear the root and the size
		this._root = null;
		this._size = 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/07
	 */
	@Override
	public boolean containsKey(Object __o)
	{
		return (null != __findNode(__o));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/06
	 */
	@Override
	public Set<Map.Entry<K, V>> entrySet()
	{
		// Get
		Reference<Set<Map.Entry<K, V>>> ref = this._entryset;
		Set<Map.Entry<K, V>> rv;
		
		// Check
		if (ref == null || null == (rv = ref.get()))
			this._entryset = new WeakReference<>(
				(rv = new __EntrySet__<>(this)));
		
		// Return
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/07
	 */
	@Override
	public V get(Object __k)
	{
		__Node__<K, V> node = __findNode(__k);
		if (node == null)
			return null;
		return node._data._value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/06
	 */
	@Override
	@SuppressWarnings({"unchecked"})
	public V put(K __k, V __v)
	{
		// Insert node
		__Found__ found = new __Found__();
		__Node__<K, V> now = __insert(this._root, found, __k, __v);
		
		// The root of the tree always becomes black
		now.__makeBlack();
		this._root = now;
		
		// Old value
		return found._oldvalue;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/29
	 */
	@Override
	@SuppressWarnings({"unchecked"})
	public V remove(Object __k)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/06
	 */
	@Override
	public int size()
	{
		return this._size;
	}
	
	/**
	 * Finds the node with the given value.
	 *
	 * @param __o The object to find.
	 * @return The node for the given object or {@code null} if it was not
	 * found.
	 * @since 2016/09/06
	 */
	@SuppressWarnings({"unchecked"})
	final __Node__<K, V> __findNode(Object __o)
	{
		// If there are no nodes then the tree is empty
		__Node__<K, V> rover = this._root;
		if (rover == null)
			return null;
		
		// Constant search
		Comparator<K> compare = this._compare;
		while (rover != null)
		{
			// Compare
			K against = rover._data._key;
			int res = compare.compare((K)__o, against);
			
			// The same? stop here
			if (res == 0)
				return rover;
			
			// The object is lower, go left
			else if (res < 0)
				rover = rover._left;
			
			// The object is higher, go right
			else
				rover = rover._right;
		}
		
		// Not found
		return null;
	}
	
	/**
	 * Inserts the given node into the tree
	 *
	 * @param __at The current node iteration.
	 * @param __f The previous node found information.
	 * @param __k The key to use.
	 * @param __v The value to use.
	 * @return The root of the local segment, the first iteration of this call
	 * will always return the root of the tree.
	 * @since 2017/03/30
	 */
	private final __Node__<K, V> __insert(__Node__<K, V> __at, __Found__ __f,
		K __k, V __v)
	{
		// No root of the tree?
		if (__at == null)
		{
			// Setup data
			__Data__<K, V> data = new __Data__<>(__k, __v);
			
			// Create new node
			__at = new __Node__<>();
			__at._data = data;
			
			// The minimum value of the tree is this only node
			this._min = data;
			
			// Use this new node
			return __at;
		}
		
		throw new todo.TODO();
	}
	
	/**
	 * The data which used to be at the given position.
	 *
	 * @since 2017/03/30
	 */
	private final class __Found__
	{
		V _oldvalue;
	}
}

