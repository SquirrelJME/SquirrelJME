// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import net.multiphasicapps.squirreljme.lcdui.DisplayManager;
import net.multiphasicapps.squirreljme.lcdui.event.EventQueue;

/**
 * This class manages the event loop for the LCDUI sub-system, all events and
 * callbacks are performed by this class.
 *
 * @since 2017/10/24
 */
final class __EventLoop__
	implements Runnable
{
	/**
	 * This runs the event queue.
	 *
	 * @since 2017/10/24
	 */
	public void run()
	{
		// Locks are performed on the event queue
		EventQueue queue = DisplayManager.DISPLAY_MANAGER.getEventQueue();
		synchronized (queue)
		{
			for (;;)
			{
				// Wait until the event queue is notified, it will be notified
				// whenever an event is added to the queue which may then
				// cause new events to be pushed
				try
				{
					queue.wait();
				}
				catch (InterruptedException e)
				{
				}
				
				// Process events regardless of interruption, this way
				// interrupts forces all events to be flushed
				System.err.println("DEBUG -- process events.");
			}
		}
	}
}

