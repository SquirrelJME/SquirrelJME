// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.mp;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import javax.microedition.io.InputConnection;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import org.intellij.lang.annotations.Language;

/**
 * Main entry point for the media player.
 *
 * @since 2025/12/26
 */
@SquirrelJMEVendorApi
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
