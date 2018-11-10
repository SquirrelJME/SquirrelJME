// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang.ref;

import java.util.Deque;
import java.util.LinkedList;

/**
 * This is a queue of references, when a reference is freed or detected to be
 * freed then the object references will be placed here.
 *
 * @param <T> The type of reference to store.
 * @since 2018/09/23
 */
public class ReferenceQueue<T>
{
	/** Internal queue of references. */
	private final Deque<Reference<? extends T>> _queue =
		new LinkedList<>();
	
	/**
	 * Initializes the queue.
	 *
	 * @since 2018/09/23
	 */
	public ReferenceQueue()
	{
	}
	
	/**
	 * Checks the queue and returns a reference immedietely if one is
	 * available.
	 *
	 * @return The next removed reference or {@code null} if there is none.
	 * @since 2018/09/23
	 */
	public Reference<? extends T> poll()
	{
		// Lock and remove
		Deque<Reference<? extends T>> queue = this._queue;
		synchronized (queue)
		{
			return queue.poll();
		}
	}
	
	/**
	 * Removes the next reference from the queue for the given duration.
	 *
	 * @param __ms The number of milliseconds to wait, zero means to wait
	 * forever.
	 * @return The reference or {@code null} if the timeout was reached.
	 * @throws IllegalArgumentException If the timeout is negative.
	 * @throws InterruptedException If the thread was interrupted while
	 * waiting.
	 * @since 2018/09/23
	 */
	public Reference<? extends T> remove(long __ms)
		throws IllegalArgumentException, InterruptedException
	{
		// {@squirreljme.error ZZ1i A negative timeout was specified.}
		if (__ms < 0)
			throw new IllegalArgumentException("ZZ1i");
		
		// A timeout of zero means to wait forever
		if (__ms == 0)
			return this.remove();
		
		// Determine the time to just stop waiting
		long endtime = System.nanoTime() + (__ms * 1_000_000L);
		
		// Lock on the queue
		Deque<Reference<? extends T>> queue = this._queue;
		synchronized (queue)
		{
			for (;;)
			{
				// Is there an item in the queue?
				Reference<? extends T> rv = queue.poll();
				if (rv != null)
					return rv;
				
				// No time left
				long difftime = (endtime - System.nanoTime()) / 1_000_000L;
				if (difftime <= 0)
					return null;
				
				// Wait for the time difference, InterruptedException drops
				// out if it occurs
				queue.wait(difftime);
			}
		}
	}
	
	/**
	 * Removes the next reference from the queue, waiting forever until there
	 * is one.
	 *
	 * @return The next reference in the queue.
	 * @throws InterruptedException If the thread was interrupted.
	 * @since 2018/09/23
	 */
	public Reference<? extends T> remove()
		throws InterruptedException
	{
		// Lock on the queue
		Deque<Reference<? extends T>> queue = this._queue;
		synchronized (queue)
		{
			for (;;)
			{
				// Is there an item in the queue?
				Reference<? extends T> rv = queue.poll();
				if (rv != null)
					return rv;
				
				// Otherwise wait for a signal, InterruptedException is tossed
				// on the outside
				queue.wait();
			}
		}
	}
	
	/**
	 * Enqueues the reference into this queue.
	 *
	 * @param __ref The reference to enqueue.
	 * @since 2018/09/23
	 */
	final void __enqueue(Reference<? extends T> __ref)
	{
		// Just ignore and do nothing
		if (__ref == null)
			return;
		
		// Lock on the queue to add it
		Deque<Reference<? extends T>> queue = this._queue;
		synchronized (queue)
		{
			queue.add(__ref);
			
			// Signal all waiting threads, one will grab it
			queue.notifyAll();
		}
	}
}

