// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package util;

import java.util.Deque;
import java.util.Iterator;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Performs tests on deques.
 *
 * @since 2019/01/20
 */
abstract class __TestDeque__
	extends TestRunnable
{
	/** The deque to test. */
	protected final Deque<Number> deque;
	
	/**
	 * Initializes the base test.
	 *
	 * @param __d The deque to test on.
	 * @since 2019/01/20
	 */
	public __TestDeque__(Deque<Number> __d)
	{
		this.deque = __d;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/01/20
	 */
	@Override
	public void test()
	{
		Deque<Number> q = this.deque;
		
		// Add a bunch of numbers
		for (int i = 0; i < 10; i++)
			this.secondary("init-add-" + i, q.add(i));
		
		// Offer first values
		q.addFirst(100);
		this.secondary("offer-first", q.offerFirst(200));
		
		// Size of this
		this.secondary("size-a", q.size());
		
		// Offer last values
		q.addLast(300);
		this.secondary("offer-last", q.offerLast(400));
		
		// Queue
		q.add(500);
		q.offer(600);
		
		// Stack
		q.push(1234);
		
		// Size of this
		int size;
		this.secondary("size-b", (size = q.size()));
		
		// Get iterators from both positions
		int[] ita = new int[size];
		Iterator<Number> it = q.iterator();
		for (int i = 0; it.hasNext(); i++)
			ita[i] = it.next().intValue();
		this.secondary("iterator-ascending", ita);
		
		// And descend it
		it = q.descendingIterator();
		for (int i = 0; it.hasNext(); i++)
			ita[i] = it.next().intValue();
		this.secondary("iterator-descending", ita);
		
		// As array form
		this.secondary("array", q.<Integer>toArray(new Integer[q.size()]));
		
		// Do removals
		this.secondary("removefirst", q.removeFirst());
		this.secondary("getfirst", q.getFirst());
		this.secondary("pollfirst", q.pollFirst());
		this.secondary("peekfirst", q.peekFirst());
		this.secondary("removelast", q.removeLast());
		this.secondary("polllast", q.pollLast());
		this.secondary("getlast", q.getLast());
		this.secondary("peeklast", q.peekLast());
		this.secondary("remove", q.remove());
		this.secondary("poll", q.poll());
		this.secondary("element", q.element());
		this.secondary("peek", q.peek());
		this.secondary("pop", q.pop());
		
		// Array form again
		this.secondary("array2", q.<Integer>toArray(new Integer[q.size()]));
	}
}

