// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package util;

import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Performs tests on deque.
 *
 * @since 2019/01/20
 */
abstract class __TestDeque__
	extends TestRunnable
{
	/** The deque to test. */
	protected final Deque<Integer> deque;
	
	/** Can we hash code? */
	private final boolean _canHashCode;
	
	/**
	 * Initializes the base test.
	 *
	 * @param __d The deque to test on.
	 * @since 2019/01/20
	 */
	public __TestDeque__(Deque<Integer> __d, boolean __canHash)
	{
		this.deque = __d;
		this._canHashCode = __canHash;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/01/20
	 */
	@Override
	public void test()
	{
		Deque<Integer> q = this.deque;
		
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
		Iterator<Integer> it = q.iterator();
		for (int i = 0; it.hasNext(); i++)
			ita[i] = it.next().intValue();
		this.secondary("iterator-ascending", ita);
		
		// And descend it
		it = q.descendingIterator();
		for (int i = 0; it.hasNext(); i++)
			ita[i] = it.next().intValue();
		this.secondary("iterator-descending", ita);
		
		// Hashcode, but not all collections have a valid hashcode
		if (this._canHashCode)
			this.secondary("hashcode", q.hashCode());
		
		// As array form
		this.secondary("array", q.<Integer>toArray(new Integer[q.size()]));
		this.secondary("arraygrow",
			q.<Integer>toArray(new Integer[q.size() / 2]));
		this.secondary("arrayover",
			q.<Integer>toArray(new Integer[q.size() + 17]));
		
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
		
		// Contains some things?
		this.secondary("has7", q.contains(7));
		this.secondary("has14", q.contains(14));
		this.secondary("has500", q.contains(500));
		
		// Add a bunch of numbers
		for (int i = 0; i < 10; i++)
			this.secondary("other-add-" + i, q.add(i));
			
		// Array form again!
		this.secondary("array3", q.<Integer>toArray(new Integer[q.size()]));
		
		// Remove first occurance of value
		for (int i = 0; i < 15; i += 2)
			this.secondary("remove-first-" + i, q.removeFirstOccurrence(i));
		this.secondary("array4", q.<Integer>toArray(new Integer[q.size()]));
		
		// Remove last occurance of value
		for (int i = 1; i < 15; i += 2)
			this.secondary("remove-last-" + i, q.removeLastOccurrence(i));
		this.secondary("array5", q.<Integer>toArray(new Integer[q.size()]));
		
		// Clear it
		q.clear();
		this.secondary("array6", q.<Integer>toArray(new Integer[q.size()]));
		
		// Various attempts to add null elements
__outer:
		for (int i = 0;; i++)
		{
			try
			{
				switch (i)
				{
					case 0: q.add(null); break;
					case 1: q.addFirst(null); break;
					case 2: q.addLast(null); break;
					case 3: q.offer(null); break;
					case 4: q.offerFirst(null); break;
					case 5: q.offerLast(null); break;
					case 6: q.push(null); break;
					
					default:
						break __outer;
				}
				
				// Succeeded
				this.secondary("null-add-" + i, false);
			}
			
			// Not found
			catch (NullPointerException e)
			{
				this.secondary("null-add-" + i, true);
			}
		}
		
		// Clear the queue out
		q.clear();
		
		// Various attempts to remove from an empty queue
__outer:
		for (int i = 0;; i++)
		{
			try
			{
				switch (i)
				{
					case 0: q.remove(); break;
					case 1: q.removeFirst(); break;
					case 2: q.removeLast(); break;
					case 3: q.poll(); break;
					case 4: q.pollFirst(); break;
					case 5: q.pollLast(); break;
					case 6: q.pop(); break;
					
					default:
						break __outer;
				}
				
				// Succeeded
				this.secondary("empty-remove-" + i, false);
			}
			
			// Not found
			catch (NoSuchElementException e)
			{
				this.secondary("empty-remove-" + i, true);
			}
		}
	}
}

