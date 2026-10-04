// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.mp;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * Main entry point for the media player.
 *
 * @since 2025/12/26
 */
public class MidletMain
	extends MIDlet
{
	/** The binder used. */
	public static volatile Binder binder;
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/26
	 */
	@Override
	protected void destroyApp(boolean __uc)
		throws MIDletStateChangeException
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/26
	 */
	@Override
	protected void startApp()
		throws MIDletStateChangeException
	{
		// Use this main display
		Display display = Display.getDisplay(this);
		
		// Setup both browser and player
		Binder binder = new Binder(display);
		
		// Set this binder globally
		synchronized (MidletMain.class)
		{
			if (MidletMain.binder == null)
				MidletMain.binder = binder;
		}
		
		// Implicit refresh
		try
		{
			binder.refresh();
		}
		
		// Failed to open the initial browser
		catch (Throwable __e)
		{
			__e.printStackTrace();
			
			// Cannot really recover from this
			System.exit(1);
		}
	}
}
