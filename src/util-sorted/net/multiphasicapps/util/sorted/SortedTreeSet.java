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

import java.util.AbstractSet;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * This is a sorted {@link Set} which internally uses a red-black tree to sort
 * the entries.
 *
 * The algorithm is derived from Robert Sedgewick's (of Princeton University)
 * 2008 variant of Red-Black Trees called Left Leaning Red-Black Trees.
 *
 * @param <V> The type of value stored in the set.
 * @since 2016/09/06
 */
public class SortedTreeSet<V>
	extends AbstractSet<V>
{
	/** The element already exists. */
	private static final __AlreadyExists__ _ALREADY_EXISTS =
		new __AlreadyExists__();
	
	/** The comparison method to use. */
	private final Comparator<V> _compare;
	
	/** The root node. */
	volatile __Node__<V> _root;
	
	/** The size of the tree. */
	volatile int _size;
	
	/**
	 * Initializes an empty red/black set using the natural comparator.
	 *
	 * @since 2016/09/06
	 */
	public SortedTreeSet()
	{
		this(__Natural__.<V>instance());
	}
	
	/**
	 * Initializes a red/black set using the natural comparator which is
	 * initialized with the given values.
	 *
	 * @param __s The collection to copy values from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/06
	 */
	@SuppressWarnings({"unchecked"})
	public SortedTreeSet(Collection<? extends Comparable<V>> __s)
		throws NullPointerException
	{
		this(__Natural__.<V>instance(), (Collection<? extends V>)__s);
	}
	
	/**
	 * Initializes an empty red/black set using the given comparator.
	 *
	 * @param __comp The comparator to use for values.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/06
	 */
	@SuppressWarnings({"unchecked"})
	public SortedTreeSet(Comparator<? extends V> __comp)
		throws NullPointerException
	{
		// Check
		if (__comp == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._compare = (Comparator<V>)__comp;
	}
	
	/**
	 * Initializes a red/black set using the given comparator which is
	 * initialized with the given values.
	 *
	 * @param __comp The comparator to use for values.
	 * @param __s The collection to copy values from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/06
	 */
	@SuppressWarnings({"unchecked"})
	public SortedTreeSet(Comparator<? extends V> __comp,
		Collection<? extends V> __s)
		throws NullPointerException
	{
		// Check
		if (__comp == null || __s == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._compare = (Comparator<V>)__comp;
		
		// Just call add all from collection
		addAll(__s);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/06
	 */
	@Override
	public boolean add(V __v)
	{
		try
		{
			// Replace the root
			__Node__<V> was = this._root;
			__Node__<V> now = __insert(was, __v);
			
			// Changed?
			if (was != now)
			{
				this._root = now;
				now._parent = null;
			}
			
			// Force to black
			now._color = __Color__.BLACK;
		
			// Was changed
			return true;
		}
		
		// In the set already
		catch (__AlreadyExists__ e)
		{
			return false;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/06
	 */
	@Override
	public boolean contains(Object __o)
	{
		// Find node
		return null != __findNode(__o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/06
	 */
	@Override
	public Iterator<V> iterator()
	{
		return new __SetIterator__<V>(this);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/06
	 */
	@Override
	public boolean remove(Object __o)
	{
		throw new Error("TODO");
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
	 * Inserts a node into the tree.
	 *
	 * @param __at The current node the algorithm is at.
	 * @param __v The value to insert.
	 * @return The newly created node.
	 * @throws __AlreadyExists__ If the value already exists.
	 * @since 2016/09/06
	 */
	private final __Node__<V> __insert(__Node__<V> __at, V __v)
		throws __AlreadyExists__
	{
		// The tree is empty, adding an element is trivial
		if (__at == null)
		{
			// Create new node
			__Node__<V> rv = new __Node__<V>(__v);
			
			// Increase size
			this._size++;
			
			// Return it
			return rv;
		}
		
		// Compare node and value
		Comparator<V> compare = this._compare;
		V existval = __at._value;
		int res = compare.compare(__v, existval);
		
		// If replacing, do nothing because only a single value may exist
		// at a time.
		if (res == 0)
			throw _ALREADY_EXISTS;
		
		// Insert on left side
		else if (res < 0)
		{
			__Node__<V> was = __at._left;
			__Node__<V> now = __insert(was, __v);
			
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
			__Node__<V> was = __at._right;
			__Node__<V> now = __insert(was, __v);
			
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
		__Node__<V> atleft = __at._left;
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
	 * Finds the node with the given value.
	 *
	 * @param __o The object to find.
	 * @return The node for the given object or {@code null} if it was not
	 * found.
	 * @since 2016/09/06
	 */
	@SuppressWarnings({"unchecked"})
	private final __Node__<V> __findNode(Object __o)
	{
		// If there are no nodes then the tree is empty
		__Node__<V> rover = this._root;
		if (rover == null)
			return null;
		
		// Constant search
		Comparator<V> compare = this._compare;
		for (; rover != null;)
		{
			// Compare
			V against = rover._value;
			int res = compare.compare((V)__o, against);
			
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
	private final void __flipColor(__Node__<V> __at)
		throws NullPointerException
	{
		// Check
		if (__at == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
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
	private final __Node__<V> __rotate(__Node__<V> __at, boolean __r)
		throws NullPointerException
	{
		// Check
		if (__at == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

