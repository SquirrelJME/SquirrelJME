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
import net.multiphasicapps.squirreljme.lcdui.event.CanvasRepaintEvent;
import net.multiphasicapps.squirreljme.lcdui.event.Event;
import net.multiphasicapps.squirreljme.lcdui.event.EventQueue;
import net.multiphasicapps.squirreljme.lcdui.event.EventType;

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
				// Wait for the next event
				Event e;
				try
				{
					e = queue.next();
				}
				catch (InterruptedException x)
				{
					continue;
				}
				
				// Handling any event could fail
				try
				{
					EventType type = e.type();
					
					// Debug
					System.err.printf("DEBUG -- Event %s%n", type);
					
					switch (type)
					{
							// Repaint this canvas
						case CANVAS_REPAINT:
							CanvasRepaintEvent cre = (CanvasRepaintEvent)e;
							cre.canvas.__doRepaint(cre.x, cre.y,
								cre.width, cre.height);
							break;
						
							// {@squirreljme.error EB0v Unknown event.
							// (The type of event this is)}
						default:
							throw new RuntimeException(
								String.format("EB0v %s", type));
					}
				}
				
				// Catch all exceptions and print, but do not let the event
				// thread die because an exception was thrown
				catch (Throwable t)
				{
					t.printStackTrace();
				}
			}
		}
	}
}

