// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.collections;

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
 * This class is not thread safe.
 *
 * @param <K> The type of key to store.
 * @param <V> The type of value to store.
 * @since 2016/09/06
 */
public class SortedTreeMap<K, V>
	extends AbstractMap<K, V>
{
	/** Rotate left. */
	private static final boolean _LEFT =
		false;
	
	/** Rotate right. */
	private static final boolean _RIGHT =
		true;
	
	/** The comparison method to use. */
	final Comparator<K> _compare;
	
	/** The entry set. */
	private Reference<Set<Map.Entry<K, V>>> _entryset;
	
	/** The root node. */
	__Node__<K, V> _root;
	
	/** The minimum value. */
	__Data__<K, V> _min;
	
	/** The size of the tree. */
	int _size;
	
	/**
	 * Initializes a new empty map using the natural comparator.
	 *
	 * @since 2016/09/06
	 */
	public SortedTreeMap()
	{
		this(NaturalComparator.<K>instance());
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
		this(NaturalComparator.<K>instance(), (Map<? extends K, ? extends V>)__m);
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
		this._root = null;
		this._min = null;
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
		__Node__<K, V> now = __insert(null, this._root, found, __k, __v);
		
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
		// Delete node
		__Found__ found = new __Found__();
		__Node__<K, V> newroot = __remove(this._root, found, (K)__k);
		
		// The root of the tree is always black
		this._root = newroot;
		if (newroot != null)
			newroot._isred = false;
		
		// Old value
		return found._oldvalue;
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
	 * Corrects nodes going back up the tree.
	 *
	 * @param __at The node to potentially correct.
	 * @return The parent node and not a side node.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/30
	 */
	private __Node__<K, V> __correctNodes(__Node__<K, V> __at)
		throws NullPointerException
	{
		// Check
		if (__at == null)
			throw new NullPointerException("NARG");
		
		// Rotate right side value to the left
		if (__isRed(__at._right))
			__at = __rotate(__at, _LEFT);
		
		// If there are a bunch of dangling red nodes on the left balance
		// them
		if (__isRed(__at._left) && __isRed(__at._left._left))
			__at = __rotate(__at, _RIGHT);
		
		// If both side nodes are red then flip the color of this node
		if (__isRed(__at._left) && __isRed(__at._right))
			__flipColor(__at);
		
		// Return current node
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
	final __Node__<K, V> __findNode(Object __o)
	{
		// If there are no nodes then the tree is empty
		__Node__<K, V> rover = this._root;
		if (rover == null)
			return null;
		
		return __findNode(rover, __o);
	}
	
	/**
	 * Finds the node with the given key starting at the specified node.
	 *
	 * @param __at The node to start at.
	 * @param __k The key to find.
	 * @return The specified node or {@code null} if not found.
	 * @since 2017/03/30
	 */
	@SuppressWarnings({"unchecked"})
	final __Node__<K, V> __findNode(__Node__<K, V> __at, Object __k)
	{	
		// Constant search
		Comparator<K> compare = this._compare;
		while (__at != null)
		{
			// Compare
			K against = __at._data._key;
			int res = compare.compare((K)__k, against);
			
			// The same? stop here
			if (res == 0)
				return __at;
			
			// Go left
			else if (res < 0)
				__at = __at._left;
			
			// Otherwise go right
			else
				__at = __at._right;
		}
		
		// Not found
		return null;
	}
	
	/**
	 * Flips the color of the specified node.
	 *
	 * @param __at The node to flip colors for.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/30
	 */
	private final void __flipColor(__Node__<K, V> __at)
		throws NullPointerException
	{
		// Check
		if (__at == null)
			throw new NullPointerException("NARG");
		
		// Flip node colors
		__at._isred = !__at._isred;
		__at._left._isred = !__at._left._isred;
		__at._right._isred = !__at._right._isred;
	}
	
	/**
	 * Inserts the given node into the tree
	 *
	 * @param __from The node this iterated from.
	 * @param __at The current node iteration.
	 * @param __found The value information when a value is discovered.
	 * @param __k The key to use.
	 * @param __v The value to use.
	 * @return The root of the local segment, the first iteration of this call
	 * will always return the root of the tree.
	 * @since 2017/03/30
	 */
	private final __Node__<K, V> __insert(__Node__<K, V> __from,
		__Node__<K, V> __at, __Found__ __found, K __k, V __v)
	{
		// No root of the tree?
		if (__at == null)
		{
			// Setup data
			__Data__<K, V> data = new __Data__<>(this, __k, __v);
			
			// Create new node
			__at = new __Node__<>();
			__at._data = data;
			data._node = __at;
			
			// Need to link the data in with the source nodes data chain
			if (__from != null)
			{
				// Need to directly modify the from data links
				__Data__<K, V> fd = __from._data;
				
				// Link before the from node?
				if (data.__compare(fd) < 0)
				{
					// The from's previous data needs to point to this node
					// and not the from data
					__Data__<K, V> pp = fd._prev;
					if (pp != null)
						pp._next = data;
					
					// This links back to that from data
					data._prev = pp;
					
					// and then links to the from data
					data._next = fd;
					
					// Then the from's previous becomes this data
					fd._prev = data;
				}
				
				// Link after
				else
				{
					// The from's next has to point back to this data
					__Data__<K, V> nn = fd._next;
					if (nn != null)
						nn._prev = data;
					
					// This links back into the from node
					data._prev = fd;
					
					// And links to the original next in the from
					data._next = nn;
					
					// Then the from next links to this data
					fd._next = data;
				}
			}
			
			// If the tree has no minimum use this node as it
			// Otherwise always use the smaller value
			__Data__<K, V> oldmin = this._min;
			if (oldmin == null || data.__compare(oldmin) < 0)
				this._min = data;
			
			// Size of the tree increased
			this._size++;
			
			// Use this new node
			return __at;
		}
		
		// Matched key, set its value
		int comp = this._compare.compare(__k, __at._data._key);
		if (comp == 0)
		{
			__found._oldvalue = __at._data._value;
			__at._data._value = __v;
		}
		
		// Less than
		else if (comp < 0)
			__at._left = __insert(__at, __at._left, __found, __k, __v);
		
		// Greater
		else
			__at._right = __insert(__at, __at._right, __found, __k, __v);
		
		// Correct nodes going back up
		return __correctNodes(__at);
	}
	
	/**
	 * Returns {@code true} if the given node is red.
	 *
	 * @param __n The node to see if it is red.
	 * @return {@code true} if the node is red.
	 * @since 2017/03/30
	 */
	private final boolean __isRed(__Node__<K, V> __n)
	{
		if (__n == null)
			return false;
		return __n._isred;
	}
	
	/**
	 * Returns the minimum node.
	 *
	 * @return The minimum node.
	 * @since 2017/03/30
	 */
	private final __Node__<K, V> __min(__Node__<K, V> __at)
	{
		while (__at._left != null)
			__at = __at._left;
		return __at;
	}
	
	/**
	 * Moves the specified red node.
	 *
	 * @param __at The node to move.
	 * @return The node that is not a side node.
	 * @since 2017/03/30
	 */
	private final __Node__<K, V> __moveRed(__Node__<K, V> __at, boolean __r)
	{
		// Flip the node color
		__flipColor(__at);
		
		// Move to the right
		if (__r)
		{
			if (__isRed(__at._left._left))
			{
				__at = __rotate(__at, _RIGHT);
			
				__flipColor(__at);
			}
		}
		
		// Move to the left
		else
		{
			if (__isRed(__at._right._left))
			{
				__at._right = __rotate(__at._right, _RIGHT);
				__at = __rotate(__at, _LEFT);
				
				__flipColor(__at);
			}
		}
		
		// This would be the node at the top
		return __at;
	}
	
	/**
	 * Recursive node removal based on the given key.
	 *
	 * @param __at The current node being traversed.
	 * @param __found Node searching information.
	 * @param __k The key to remove the value from.
	 * @return The node at the top (will not be a leaf)
	 * @since 2017/03/30
	 */
	private final __Node__<K, V> __remove(__Node__<K, V> __at,
		__Found__ __found, K __k)
	{
		// Key is lower?
		Comparator<K> compare = this._compare;
		int comp = compare.compare(__k, __at._data._key);
		if (comp < 0)
		{
			// Move red node to the left
			if (!__isRed(__at._left) && !__isRed(__at._left._left))
				__at = __moveRed(__at, _LEFT);
			
			// Delete left side
			__at._left = __remove(__at._left, __found, __k);
		}
		
		// Equal or higher
		else
		{
			// If the left is red then rotate it to the right
			if (__isRed(__at._left))
			{
				__at = __rotate(__at, _RIGHT);
				
				// Compare value is trashed, recompute
				comp = compare.compare(__k, __at._data._key);
			}
			
			// If this is the key and there is no right then no values need
			// to be shifted in
			if (comp == 0 && __at._right == null)
			{
				__unlink(__at, __found);
				
				// Return no key
				return null;
			}
			
			// If the red side contains a black chain move red nodes to the
			// right
			if (!__isRed(__at._right) && !__isRed(__at._right._left))
			{
				__at = __moveRed(__at, _RIGHT);
				
				// Comparison is trashed
				comp = compare.compare(__k, __at._data._key);
			}
			
			// Keys are the same
			if (comp == 0)
			{
				// Get the node with the minimum value on the right side
				__Node__<K, V> right = __at._right;
				__Node__<K, V> minright = __min(right);
				
				// Unlink the current data because that is getting destroyed
				__unlink(__at, __found);
				
				// The current node gets the data for that key
				__at._data = minright._data;
				
				// Remove the minimum without unlinking (because it gets
				// re-associated)
				__removeMin(right, null, false);
			}
			
			// Delete right side of the tree
			else
				__at._right = __remove(__at._right, __found, __k);
		}
		
		// Correct tree on the way up
		return __correctNodes(__at);
	}
	
	/**
	 * Removes the minimum node.
	 *
	 * @param __at Current node.
	 * @param __found The found node information.
	 * @param __unlink If {@code true} the node is unlinked.
	 * @return The top node.
	 * @since 2017/03/30
	 */
	private final __Node__<K, V> __removeMin(__Node__<K, V> __at,
		__Found__ __found, boolean __unlink)
	{
		// If there is no left, remove the left node
		if (__at._left == null)
		{
			// Unlink our node
			if (__unlink)
				__unlink(__at, __found);
			
			// No left node
			return null;
		}
		
		// If the left side is black move red to the left
		if (!__isRed(__at._left) && !__isRed(__at._left._left))
			__at = __moveRed(__at, _LEFT);
		
		// Continue deleting the minimum
		__at._left = __removeMin(__at, __found, __unlink);
		
		// Correct nodes back up the tree
		return __correctNodes(__at);
	}
	
	/**
	 * Rotates the nodes in the given direction.
	 *
	 * @param __at The node to rotate.
	 * @param __r If {@code true} then rotation is to the right, otherwise it
	 * is to the left.
	 * @return The center node.
	 * @since 2017/03/27
	 */
	private final __Node__<K, V> __rotate(__Node__<K, V> __at, boolean __r)
		throws NullPointerException
	{
		// Check
		if (__at == null)
			throw new NullPointerException("NARG");
		
		// Rotate right
		if (__r)
		{
			__Node__<K, V> x = __at._left;
			__at._left = x._right;
			x._right = __at;
			x._isred = x._right._isred;
			x._right._isred = true;
			return x;
		}
		
		// Rotate left
		else
		{
			__Node__<K, V> x = __at._right;
			__at._right = x._left;
			x._left = __at;
			x._isred = x._left._isred;
			x._left._isred = true;
			return x;
		}
	}
	
	/**
	 * Unlinks the specified node.
	 *
	 * @param __at The node to unlink.
	 * @param __found The found node data.
	 * @since 2017/03/30
	 */
	private final void __unlink(__Node__<K, V> __at, __Found__ __found)
	{
		// Get the data to unlink
		__Data__<K, V> unlink = __at._data;
		if (__found != null)
			__found._oldvalue = unlink._value;
		
		// Link next node with the previous
		__Data__<K, V> prev = unlink._prev,
			next = unlink._next;
		if (next != null)
			next._prev = prev;
		
		// Link previous node with the next one
		if (prev != null)
			prev._next = next;
		
		// If this is the minimum node then the next one will be the
		// new minimum
		if (this._min == unlink)
			this._min = next;
		
		// Destroy chains
		unlink._value = null;
		unlink._node = null;
		unlink._prev = null;
		unlink._next = null;
		
		// Reduce count
		this._size--;
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

