// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui.event;

import java.util.Deque;
import java.util.LinkedList;
import javax.microedition.lcdui.Canvas;

/**
 * This class is used to manage the queue of events which may be generated on
 * native LCDUI widgets and such. This enables everything about the UI system
 * to be natively serialized as needed.
 *
 * This internally uses two monitors so that a thread may wait on an event
 * and additionally add more events to the queue without locking other
 * threads.
 *
 * @since 2017/10/24
 */
public final class EventQueue
{
	/** The internal event queue. */
	private final Deque<Event> _queue =
		new LinkedList<>();
	
	/** Monitor. */
	private final Object _monitor =
		new Object();
	
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
		Deque<Event> queue = this._queue;
		Object monitor = this._monitor;
		
		// Constantly try to get events
		for (;;)
		{
			// If there is an event in the queue, immedietly return that
			synchronized (queue)
			{
				// Can immedietely return an event? Do that
				Event rv = queue.pollFirst();
				if (rv != null)
					return rv;
			}
		
			// Otherwise wait for one to be generated
			synchronized (monitor)
			{
				monitor.wait();
			}
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
		}
		
		// Tell all waiting threads that an event is ready
		Object monitor = this._monitor;
		synchronized (monitor)
		{
			monitor.notifyAll();
		}
	}
	
	/**
	 * Specifies that the given canvas should be repainted.
	 *
	 * @param __c The canvas to have painted.
	 * @param __x The x coordinate.
	 * @param __y The y coordinate.
	 * @param __w The width.
	 * @param __h The height.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/24
	 */
	public final void repaintCanvas(Canvas __c, int __x, int __y, int __w,
		int __h)
		throws NullPointerException
	{
		if (__c == null)
			throw new NullPointerException("NARG");
		
		push(new CanvasRepaintEvent(__c, __x, __y, __w, __h));
	}
}

