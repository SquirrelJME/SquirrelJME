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
		// What is the starting URI?
		@Language("http-url-reference")
		String uri = "x-squirreljme-volumes:";
		
		// Use this main display
		Display display = Display.getDisplay(this);
		
		// Setup both browser and player
		Binder binder = new Binder(display);
		BasicBrowser browser = binder._browser;
		MediaPlayer player = binder._player;
		
		// Set this binder globally
		synchronized (MidletMain.class)
		{
			if (MidletMain.binder == null)
				MidletMain.binder = binder;
		}
		
		// Start browsing or playing specific media
		Displayable show;
		try (Connection conn = Connector.open(uri, Connector.READ))
		{
			// If browsing a directory, browse the contents
			if ((conn instanceof FileConnection) &&
				((FileConnection)conn).isDirectory())
				show = browser.browse((FileConnection)conn);
			
			// Otherwise view the content
			else if (conn instanceof InputConnection)
				show = player.play((InputConnection)conn);
			
			// Unsupported
			else
				throw new UnsupportedOperationException(uri);
		}
		catch (IOException __e)
		{
			throw new RuntimeException(__e.getMessage(), __e);
		}
		
		// Show on the display!
		display.setCurrent(show);
	}
}
