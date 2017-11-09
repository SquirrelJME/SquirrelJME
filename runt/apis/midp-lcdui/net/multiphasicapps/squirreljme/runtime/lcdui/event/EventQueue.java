// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.lcdui.event;

import java.util.Deque;
import java.util.LinkedList;
import javax.microedition.lcdui.Canvas;

/**
 * This class is used to manage the queue of events which may be generated on
 * native LCDUI widgets and such. This enables everything about the UI system
 * to be natively serialized as needed.
 *
 * @since 2017/10/24
 */
public final class EventQueue
{
	/** The internal event queue. */
	private final Deque<Event> _queue =
		new LinkedList<>();
	
	/**
	 * Waits for the next event to be generated.
	 *
	 * @return The next event.
	 * @throws InterruptedException If the thread was interrupted while waiting
	 * for the next event.
	 * @since 2017/10/24
	 */
	public final Event next()
		throws InterruptedException
	{
		// Try to get a single event
		Deque<Event> queue = this._queue;
		for (;;)
			synchronized (queue)
			{
				// Can immedietely return an event? Do that
				Event rv = queue.pollFirst();
				if (rv != null)
					return rv;
			
				// Wait for an event
				queue.wait();
			}
	}
	
	/**
	 * Pushes the specified event to the queue.
	 *
	 * @param __e The event to push.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/24
	 */
	public final void push(Event __e)
		throws NullPointerException
	{
		if (__e == null)
			throw new NullPointerException("NARG");
		
		// Lock on queue to add the event
		Deque<Event> queue = this._queue;
		synchronized (queue)
		{
			queue.offerLast(__e);
			
			// Notify that an event happened
			queue.notifyAll();
		}
	}
	
	/**
	 * Specifies that the given coordinates should be repainted.
	 *
	 * @param __id The display head ID.
	 * @param __x The x coordinate.
	 * @param __y The y coordinate.
	 * @param __w The width.
	 * @param __h The height.
	 * @since 2017/10/24
	 */
	public final void repaint(int __id, int __x, int __y, int __w, int __h)
	{
		push(new RepaintEvent(__id, __x, __y, __w, __h));
	}
}

