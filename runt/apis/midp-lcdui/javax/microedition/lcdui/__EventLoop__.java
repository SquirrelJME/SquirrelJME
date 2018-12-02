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

import cc.squirreljme.runtime.cldc.asm.NativeDisplayAccess;
import cc.squirreljme.runtime.lcdui.event.EventType;

/**
 * This is the single event loop which handles all events, since they must all
 * be serialized within each other the bulk of the code is called from this
 * event loop as such.
 *
 * @since 2018/11/17
 */
final class __EventLoop__
	implements Runnable
{
	/** The primary display. */
	volatile Display _main;
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/17
	 */
	@Override
	public void run()
	{
		// This is run constantly in a loop waiting for events to happen
		try
		{
			this.__loop();
		}
		
		// Event loop died
		catch (Throwable t)
		{
			t.printStackTrace();
		}
		
		// In the event this loop dies or terminates, if this is the active
		// event loop always make sure it no longer is the active loop.
		// Otherwise events will just suddenly stop working.
		finally
		{
			if (this == Display._EVENT_LOOP)
				Display._EVENT_LOOP = null;
		}
	}
	
	/**
	 * Contains the event loop.
	 *
	 * @since 2018/11/18
	 */
	private final void __loop()
	{
		Display main = this._main;
		
		// Used to store event data
		short[] data = new short[NativeDisplayAccess.EVENT_SIZE];
		
		// Event handling loop
		for (;;)
		{
			// Need to know the main display
			if (main == null)
				main = this._main;
			
			// Poll for events and handle them
			EventType t;
			switch ((t = EventType.of(
				NativeDisplayAccess.pollEvent(data))))
			{
					// Command activated
				case COMMAND:
					throw new todo.TODO();

					// Key was pressed, repeated, or released
				case KEY_PRESSED:
				case KEY_REPEATED:
				case KEY_RELEASED:
					Display.__mapDisplay(0).__doKeyAction(t, data[0], data[1]);
					break;
					
					// Pointer Dragged, pressed, or released.
				case POINTER_DRAGGED:
				case POINTER_PRESSED:
				case POINTER_RELEASED:
					Display.__mapDisplay(0).__doPointerAction(t,
						data[0], data[1], data[2]);
					break;

					// Repaint the current display
				case DISPLAY_REPAINT:
					Display.__mapDisplay(data[0]).__doRepaint(
						data[1], data[2], data[3], data[4]);
					break;
					
					// Display had its size changed
				case DISPLAY_SIZE_CHANGED:
					Display.__mapDisplay(data[0]).__doDisplaySizeChanged(
						data[1], data[2]);
					break;
					
					// Exit requested
				case EXIT_REQUEST:
					Display.__mapDisplay(data[0]).__doExitRequest();
					break;
					
					// Display shown
				case DISPLAY_SHOWN:
					Display.__mapDisplay(data[0]).__doDisplayShown(true);
					break;
					
					// Display hidden
				case DISPLAY_HIDDEN:
					Display.__mapDisplay(data[0]).__doDisplayShown(false);
					break;
					
				default:
					throw new todo.OOPS("" + t);
			}
		}
	}
}

