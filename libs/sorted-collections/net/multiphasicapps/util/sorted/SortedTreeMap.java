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
	/** Left side. */
	private static final boolean _LEFT =
		false;
	
	/** Right side. */
	private static final boolean _RIGHT =
		true;
	
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
		__Search__ search = new __Search__();
		__Node__<K, V> was = this._root;
		__Node__<K, V> now = __insert(search, was, __k);

		// Changed?
		if (was != now)
		{
			this._root = now;
			now._parent = null;
		}

		// Force to black
		now._isred = true;
		
		// Set new value
		__Node__<K, V> act = search._node;
		if (act == null)
			throw new RuntimeException("OOPS");
		V old = act._value;
		act._value = __v;
		return old;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/29
	 */
	@Override
	@SuppressWarnings({"unchecked"})
	public V remove(Object __k)
	{
		// If the map is empty then do not bother
		__Node__<K, V> root = this._root;
		if (root == null)
			return null;
		
		// Start at the root and return the old value
		__Search__ search = new __Search__();
		__Node__<K, V> now = __remove(search, root, (K)__k);
		__Node__<K, V> act = search._node;
		return act._value;
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
	 * Corrects nodes traversing back up the tree.
	 *
	 * @param __at The node to correct.
	 * @return The currected rotated node which is at the root.
	 * @since 2017/03/29
	 */
	public __Node__<K, V> __correctUp(__Node__<K, V> __at)
		throws NullPointerException
	{
		// Check
		if (__at == null)
			throw new NullPointerException("NARG");
		
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
		
		// Flip parent
		__at._isred = !__at._isred;
		
		// Flip left
		__Node__<K, V> left = __at._left;
		left._isred = !left._isred;
		
		// Flip right
		__Node__<K, V> right = __at._right;
		right._isred = !right._isred;
	}
	
	/**
	 * Inserts a node into the tree.
	 *
	 * @param __s Node search information.
	 * @param __at The current node the algorithm is at.
	 * @param __k The key to insert.
	 * @return The newly created node.
	 * @throws NullPointerException If no search was specified.
	 * @since 2016/09/06
	 */
	private final __Node__<K, V> __insert(__Search__ __s, __Node__<K, V> __at,
		K __k)
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// The tree is empty, adding an element is trivial
		if (__at == null)
		{
			// Create new node
			__Node__<K, V> rv = new __Node__<K, V>(__k);
			
			// Set last found node
			__s._node = rv;
			
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
		{
			// Set as found
			__s._node = __at;
			
			// Return it
			return __at;
		}
		
		// Insert on left side
		else if (res < 0)
		{
			__Node__<K, V> was = __at._left;
			__Node__<K, V> now = __insert(__s, was, __k);
			
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
			__Node__<K, V> now = __insert(__s, was, __k);
			
			// Changed?
			if (was != now)
			{
				__at._right = now;
				
				// Set parent
				now._parent = __at;
			}
		}
		
		// Correct nodes up the tree
		return __correctUp(__at);
	}
	
	/**
	 * Moves the dangling red nodee around in the tree in the given direction.
	 *
	 * @param __at The node to move.
	 * @param __r If {@code true} then rotation is to the right.
	 * @throws NullPointerException On null arguments.
	 * @return The node at the top position.
	 * @since 2017/03/29
	 */
	final __Node__<K, V> __moveRed(__Node__<K, V> __at, boolean __r)
		throws NullPointerException
	{
		// Check
		if (__at == null)
			throw new NullPointerException("NARG");
		
		// Flip the nodes on the left and right
		__flipColor(__at);
		
		// Right direction
		if (__r)
		{
			// {@squirreljme.error CE04 Expected a node on the left side when
			// moving red node to the right.}
			__Node__<K, V> left = __at._left;
			if (left == null)
				throw new IllegalStateException("CE04");
			
			// If the node on the left is red then flip the current node right
			__Node__<K, V> leftleft = left._left;
			if (leftleft != null && leftleft._isred)
			{
				// Rotate
				__at = __rotate(__at, true);
				
				// Correct color
				__flipColor(__at);
			}
		}
		
		// Left direction
		else
		{
			// {@squirreljme.error CE03 Expected a node on the right side when
			// moving red node to the left.}
			__Node__<K, V> right = __at._right;
			if (right == null)
				throw new IllegalStateException("CE03");
		
			// If the node on the left of the right node is red then rotate it
			// to the top position
			__Node__<K, V> rightleft = right._left;
			if (rightleft != null && rightleft._isred)
			{
				// Rotate right node
				__Node__<K, V> nowright = __rotate(right, true);
				if (nowright != right)
				{
					__at._right = nowright;
					nowright._parent = __at;
				}
				
				// Rotate this one to the left
				__at = __rotate(__at, false);
			
				// Correct color
				__flipColor(__at);
			}
		}
		
		// Kept the same or is the top node
		return __at;
	}
	
	/**
	 * Removes the specified node from the tree.
	 *
	 * @param __s Node search information.
	 * @param __at The node to remove.
	 * @param __k The key value.
	 * @return The top of the node on removal.
	 * @throws NullPointerException On null arguments, except for the key.
	 * @since 2017/03/25
	 */
	final __Node__<K, V> __remove(__Search__ __s, __Node__<K, V> __at, K __k)
		throws NullPointerException
	{
		// Check
		if (__s == null || __at == null)
			throw new NullPointerException("NARG");
		
		// Compare node and value
		Comparator<K> compare = this._compare;
		K existval = __at._key;
		int res = compare.compare(__k, existval);
		
		// Less than
		if (res < 0)
		{
			if (true)
				throw new todo.TODO();
			/*
			// Move red node to the left if this is lower than the key
			__Node__<K, V> left = __at._left;
			if (!left._isred && !left.isSideColor(false, true))
				__at = __moveRed(__at, false);
			
			// Recursive removal
			__Node__<K, V> was = __at;
			__Node__<K, V> now = __remove(__at._left, __k);
			if (was != now)
			{
				__at._left = now;
				
				// Reparent if valid
				if (now != null)
					now._parent = __at;
			}*/
		}
		
		// Equal or greater
		else
		{
			if (true)
				throw new todo.TODO();
			/*
			// If the left node is red then rotate to the right
			__Node__<K, V> left = __at._left;
			if (left != null && left._isred)
				__at = __rotate(__at, true);
			
			// If equal and there is no right side then just unlink the node
			__Node__<K, V> right = __at._right;
			if (res == 0 && right == null)
			{
				// Set as found
				this._found = __at;
				
				// Count as removed
				this._size--;
				
				// Remove child links from parent node
				__Node__<K, V> oldparent = __at._parent;
				boolean wasleft, wasright;
				if (wasleft = (oldparent._left == __at))
					oldparent._left = null;
				if (wasright = (oldparent._right == __at))
					oldparent._right = null;
				
				// {@squirreljme.error CE05 A node was not linked into any
				// parent.}
				if (!wasright && !wasleft)
					throw new IllegalStateException("CE05");
				
				// {@squirreljme.error CE06 A node was linked into both the
				// left and right side of the parent.}
				if (wasright && wasleft)
					throw new IllegalStateException("CE06");
				
				// If we have a left node
				left = __at._left;
				if (left != null)
				{
					left._parent = oldparent;
					
					// Give that left node the side we were on
					if (wasleft)
						oldparent._left = left;
					else
						oldparent._right = left;
				}
				
				// Destroy node data, but keep the value
				__at._left = null;
				__at._right = null;
				__at._parent = null;
				
				// No node used
				return null;
			}
			
			// If the right side is black and its left side is also black
			// make the nodes red and move them to the left
			if (!right._isred && !right.isSideColor(false, true))
				__at = __moveRed(__at, true);
			
			// Potentially moved or rotated, recalculate key direction
			existval = __at._key;
			res = compare.compare(__k, existval);
			
			// Keys are equal
			if (res == 0)
			{
				// Set as found
				this._found = __at;
				
				// Unlink from the tree
				throw new todo.TODO();
			}
			
			// Traverse right side of tree
			else
			{
				__Node__<K, V> was = __at._right;
				__Node__<K, V> now = __remove(__at._right, __k);
				
				// Correct parent?
				if (was != now)
				{
					__at._right = now;
					
					// Reparent if valid
					if (now != null)
						now._parent = __at;
				}
			}*/
		}
		
		// Correct up the tree
		return __correctUp(__at);
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
		
		// Get the old parent of the current node since the one that becomes
		// the parent of the tail and the current node needs to be updated
		__Node__<K, V> parent = __at._parent;
		
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
		
		// Since the nodes were rotated, one's parent will point to the wrong
		// node which will cause the iterator to infinite loop going back and
		// forth
		lean._parent = parent;
		
		// Adjust colors
		lean._isred = __at._isred;
		__at._isred = true;
		
		// Return the leaning node
		return lean;
	}
	
	/**
	 * Search information.
	 *
	 * @since 2017/03/20
	 */
	private final class __Search__
	{
		/** The node that was found. */
		volatile __Node__<K, V> _node;
	}
}

