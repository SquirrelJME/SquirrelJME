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
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * Main entry point for the media player.
 *
 * @since 2025/12/26
 */
@SquirrelJMEVendorApi
public class MidletMain
	extends MIDlet
{
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
		// Start browsing at the SquirrelJME specific root
		BasicBrowser browser;
		try (Connection conn = Connector.open(
			"x-squirreljme-volumes:"))
		{
			browser = new BasicBrowser();
			browser.browse((FileConnection)conn);
		}
		catch (ClassCastException|IOException __e)
		{
			throw new RuntimeException(__e.getMessage(), __e);
		}
		
		// Show on the display!
		Display.getDisplay(this).setCurrent(browser);
	}
}
