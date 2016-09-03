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
import java.util.List;

/**
 * This is a class which provides a multiple {@link Deque} compatible interface
 * where every element within the queue is unique as if it were a member of
 * a {@link Set}. Note that the subdeques can be given elements that are shared
 * in multiple deques, however if one is removed then it is removed from the
 * other.
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

