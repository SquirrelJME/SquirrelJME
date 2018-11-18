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
			Display main = this._main;
			
			// Used to store event data
			int[] data = new int[NativeDisplayAccess.EVENT_SIZE];
			
			// Event handling loop
			for (;;)
			{
				// Need to know the main display
				if (main == null)
					main = this._main;
				
				// Poll for events and handle them
				switch (EventType.of(NativeDisplayAccess.pollEvent(data)))
				{
					// Command activated
					case COMMAND:
						throw new todo.TODO();

					// Key was pressed.
					case KEY_PRESSED:
						throw new todo.TODO();

					// Key was released.
					case KEY_RELEASED:
						throw new todo.TODO();

					// Key was repeated.
					case KEY_REPEATED:
						throw new todo.TODO();

					// Pointer Dragged.
					case POINTER_DRAGGED:
						throw new todo.TODO();

					// Pointer Pressed.
					case POINTER_PRESSED:
						throw new todo.TODO();

					// Pointer Released.
					case POINTER_RELEASED:
						throw new todo.TODO();

					// Repaint the current display
					case REPAINT:
						throw new todo.TODO();
						
					default:
						throw new RuntimeException("OOPS");
				}
			}
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
}

