// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.util.msd;

import java.util.ArrayList;
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
	
	/** The master set of added elements. */
	final Set<V> _master =
		new HashSet<>();
	
	/**
	 * Initializes the multi set deque with the specified number of internal
	 * queues.
	 *
	 * @since 2016/09/03
	 */
	public MultiSetDeque()
	{
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
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns a new sub-{@link Deque} which acts as part of the multi-deque.
	 *
	 * @return A new deque which shares the set restrictions.
	 * @since 2016/09/03
	 */
	public final Deque<V> subDeque()
	{
		return subDeque(Integer.MAX_VALUE);
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
		// {@squirreljme.error BZ01 The sub-deque limit is zero or negative.}
		if (__l <= 0)
			throw new IllegalArgumentException("BZ01");
		
		// Create it
		__Sub__<V> rv = new __Sub__<>(this, __l);
		this._subs.add(rv);
		return rv;
	}
}

