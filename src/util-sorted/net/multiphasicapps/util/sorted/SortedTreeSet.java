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
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
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
	/** The comparison method to use. */
	private final Comparator<? extends V> _compare;
	
	/** The root node. */
	volatile __Node__<V> _root;
	
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
	public SortedTreeSet(Comparator<? extends V> __comp)
		throws NullPointerException
	{
		// Check
		if (__comp == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._compare = __comp;
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
	public SortedTreeSet(Comparator<? extends V> __comp,
		Collection<? extends V> __s)
		throws NullPointerException
	{
		// Check
		if (__comp == null || __s == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._compare = __comp;
		
		// Just call add all from collection
		addAll(__s);
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
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/06
	 */
	@Override
	public int size()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Finds the node with the given value.
	 *
	 * @param __o The object to find.
	 * @return The node for the given object.
	 * @since 2016/09/06
	 */
	private final __Node__<V> __findNode(Object __o)
	{
		// If there are no nodes then the tree is empty
		__Node__<V> root = this._root;
		if (root == null)
			return null;
		
		throw new Error("TODO");
	}
}

