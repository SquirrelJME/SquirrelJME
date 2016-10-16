// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import net.multiphasicapps.squirrelquarrel.ui.line.LineUI;
import net.multiphasicapps.squirrelquarrel.ui.GameUI;

/**
 * This is the main entry point for the game which uses the LUI interface to
 * interact and display the game.
 *
 * @since 2016/08/30
 */
public class ConsoleMain
	extends MIDlet
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The game thread. */
	private volatile Thread _main;
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/30
	 */
	@Override
	protected void destroyApp(boolean __uc)
		throws MIDletStateChangeException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/30
	 */
	@Override
	protected void startApp()
		throws MIDletStateChangeException
	{
		// Lock
		synchronized (this.lock)
		{
			// Create a new main thread?
			Thread main = this._main;
			if (main == null)
			{
				main = new Thread(new MainThread("-c"));
				this._main = main;
				
				// Start it
				main.start();
			}
		}
	}
}

