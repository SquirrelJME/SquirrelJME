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
		return node._value;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/06
	 */
	@Override
	@SuppressWarnings({"unchecked"})
	public V put(K __k, V __v)
	{
		// Replace the root
		__Node__<K, V> was = this._root;
		__Node__<K, V> now = __insert(was, __k, __v);

		// Changed?
		if (was != now)
		{
			this._root = now;
			now._parent = null;
		}

		// Force to black
		now._color = __Color__.BLACK;
		
		// Set new value
		V old = now._value;
		now._value = __v;
		return old;
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
		for (; rover != null;)
		{
			// Compare
			K against = rover._key;
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
	 * Flips the color of the sub-nodes so that red becomes black and black
	 * becomes red.
	 *
	 * @param __at The node to flip.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/06
	 */
	private final void __flipColor(__Node__<K, V> __at)
		throws NullPointerException
	{
		// Check
		if (__at == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Inserts a node into the tree.
	 *
	 * @param __at The current node the algorithm is at.
	 * @param __k The key to insert.
	 * @param __v The value to insert.
	 * @return The newly created node.
	 * @since 2016/09/06
	 */
	private final __Node__<K, V> __insert(__Node__<K, V> __at, K __k, V __v)
	{
		// The tree is empty, adding an element is trivial
		if (__at == null)
		{
			// Create new node
			__Node__<K, V> rv = new __Node__<K, V>(__k, __v);
			
			// Increase size
			this._size++;
			
			// Return it
			return rv;
		}
		
		// Compare node and value
		Comparator<K> compare = this._compare;
		K existval = __at._key;
		int res = compare.compare(__k, existval);
		
		// If replacing, do nothing because only a single value may exist
		// at a time.
		if (res == 0)
			return __at;
		
		// Insert on left side
		else if (res < 0)
		{
			__Node__<K, V> was = __at._left;
			__Node__<K, V> now = __insert(was, __k, __v);
			
			// Changed?
			if (was != now)
			{
				__at._left = now;
				
				// Set parent
				now._parent = __at;
			}
		}
		
		// Insert on right side
		else if (res > 0)
		{
			__Node__<K, V> was = __at._right;
			__Node__<K, V> now = __insert(was, __k, __v);
			
			// Changed?
			if (was != now)
			{
				__at._right = now;
				
				// Set parent
				now._parent = __at;
			}
		}
		
		// If the right side is red then rotate left
		if (__at.isSideColorRed(true))
			__at = __rotate(__at, false);
		
		// If the left node is red and it has another left node which is also
		// red then rotate right
		__Node__<K, V> atleft = __at._left;
		if (atleft != null && __at.isSideColorRed(false) &&
			atleft.isSideColorRed(false))
			__at = __rotate(__at, true);
		
		// If both sides are red then they must become black while the node
		// above becomes red
		if (__at.isSideColorRed(false) && __at.isSideColorRed(true))
			__flipColor(__at);
		
		// Return the node, which may have been rotated
		return __at;
	}
	
	/**
	 * Rotates a node a given direction.
	 *
	 * @param __at The node to rotate.
	 * @param __r If {@code true} then the node is rotated right, otherwise
	 * it is rotated to the left.
	 * @return The resulting node to place in the tree.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/06
	 */
	private final __Node__<K, V> __rotate(__Node__<K, V> __at, boolean __r)
		throws NullPointerException
	{
		// Check
		if (__at == null)
			throw new NullPointerException("NARG");
		
		// The node to be leaned and the other node to balance off it
		__Node__<K, V> lean, tail;
		
		// Lean 3 left leaning nodes to the right
		if (__r)
		{
			// Get nodes to move
			lean = __at._left;
			tail = lean._right;
			
			// Move them
			__at._left = tail;
			lean._right = __at;
		}
		
		// Lean 3 right leaning nodes to the left
		else
		{
			// Get nodes to move
			lean = __at._right;
			tail = lean._left;
			
			// Move them
			__at._right = tail;
			lean._left = __at;
		}
		
		// Tail points to the current node
		if (tail != null)
			tail._parent = __at;
		
		// The parent node was rotated to one side, so adjust the parent
		__at._parent = lean;
		
		// Adjust colors
		lean._color = __at._color;
		__at._color = __Color__.RED;
		
		// Return the leaning node
		return lean;
	}
}

