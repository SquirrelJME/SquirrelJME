// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.midp.lcdui;

/**
 * This is a class which implements the display server used by the LCDUI
 * interface. The client implementation communicates with this server.
 *
 * @since 2016/10/11
 */
public abstract class DisplayServer
	implements Runnable
{
	/** The display server thread. */
	protected final Thread thread;
	
	/**
	 * Initializes the base display server.
	 *
	 * @since 2016/10/11
	 */
	public DisplayServer()
	{
		// Setup display server thread
		Thread thread;
		this.thread = (thread = new Thread(this, "SquirrelJMEDisplayServer"));
		
		// Server specific thread modification?
		modifyThread(thread);
		
		// Start it
		thread.start();
	}
	
	/**
	 * This may be used by an implementation of the display server to modify
	 * the thread behavior.
	 *
	 * The default implementation does nothing.
	 *
	 * @param __t The thread to modify.
	 * @since 2016/10/11
	 */
	protected void modifyThread(Thread __t)
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/11
	 */
	@Override
	public final void run()
	{
		// Infinite loop
		for (;;)
		{
			throw new Error("TODO");
		}
	}
}

