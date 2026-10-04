// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This is a class which provides a multiple {@link Deque} compatible interface
 * where every element within the queue is unique as if it were a member of
 * a {@link Set}. Note that the subdeques can be given elements that are shared
 * in multiple deques.
 *
 * In the returned {@link Deque}, elements which are removed will be removed
 * from the other {@link Deque}s. As such, clearing of one {@link Deque} may
 * result in other ones getting smaller in size.
 *
 * {@code null} elements are not permitted.
 *
 * This class is not thread safe.
 *
 * @param <V> The type of value to store in the dequeues.
 * @since 2016/09/03
 */
public class MultiSetDeque<V>
{
	/** Sub-queue list. */
	private final List<__Sub__<V>> _subs =
		new ArrayList<>();
	
	/** The global set of added elements. */
	final Set<V> _global =
		new HashSet<>();
	
	/**
	 * Clears the multi-set deque and every sub-deque so that all are empty.
	 *
	 * @since 2017/03/25
	 */
	public final void clear()
	{
		// Clear the global set
		this._global.clear();
		
		// And all the subsets
		for (__Sub__<V> s : this._subs)
			s.__clear();
	}
	
	/**
	 * Checks if the any of the sub-deques contain the specified element.
	 *
	 * @param __v The element to check.
	 * @return {@code true} if the element is any in deque.
	 * @since 2017/04/25
	 */
	public final boolean contains(V __v)
	{
		return this._global.contains(__v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/25
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (!(__o instanceof MultiSetDeque))
			return false;
		
		return this._subs.equals(((MultiSetDeque<?>)__o)._subs);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/25
	 */
	@Override
	public final int hashCode()
	{
		return this._subs.hashCode();
	}
	
	/**
	 * Removes the given element from any of the sub-{@link Deque}s that are
	 * a part of the multi-set.
	 *
	 * @param __v The value to remove.
	 * @return {@code true} if it was in any {@link Deque}.
	 * @since 2016/09/03
	 */
	public final boolean remove(V __v)
	{
		// Null will never be in this deque
		if (__v == null)
			return false;
		
		// If not in the global set then it will not be in any deque
		Set<V> global = this._global;
		if (!global.contains(__v))
			return false;
		
		// Remove in all
		List<__Sub__<V>> subs = this._subs;
		int n = subs.size();
		boolean rv = false;
		for (int i = 0; i < n; i++)
			rv |= subs.get(i).__remove(__v);
		
		// Remove from the global set because it will be no sub-deque
		global.remove(__v);
		
		// Was it removed?
		return rv;
	}
	
	/**
	 * Returns a new sub-{@link Deque} which acts as part of the multi-deque.
	 *
	 * @return A new deque which shares the set restrictions.
	 * @since 2016/09/03
	 */
	public final Deque<V> subDeque()
	{
		return this.subDeque(Integer.MAX_VALUE);
	}
	
	/**
	 * Returns a new sub-{@link Deque} which acts as part of the multi-deque
	 * which is initialized using the given collection.
	 *
	 * @param __c The collection to add to the resulting {@link Deque}.
	 * @return A new deque which shares the set restrictions.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/15
	 */
	public final Deque<V> subDeque(Collection<V> __c)
		throws NullPointerException
	{
		// Check
		if (__c == null)
			throw new NullPointerException("NARG");
		
		// Create
		Deque<V> rv = this.subDeque();
		rv.addAll(__c);
		return rv;
	}
	
	/**
	 * Returns a new sub-{@link Deque} which acts as part of the multi-deque
	 * which is limited to the given number of elements.
	 *
	 * @param __l The number of elements to limit to.
	 * @return A new deque which shares the set restrictions.
	 * @throws IllegalArgumentException If the limit is zero or negative.
	 * @since 2016/09/03
	 */
	public final Deque<V> subDeque(int __l)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error AC05 The sub-deque limit is zero or negative.} */
		if (__l <= 0)
			throw new IllegalArgumentException("AC05");
		
		// Create it
		__Sub__<V> rv = new __Sub__<>(this, __l);
		this._subs.add(rv);
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/25
	 */
	@Override
	public final String toString()
	{
		return this._subs.toString();
	}
}

