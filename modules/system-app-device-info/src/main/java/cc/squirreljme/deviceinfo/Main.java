// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.deviceinfo;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * Main entry class for the device information class.
 *
 * @since 2025/12/06
 */
@SquirrelJMEVendorApi
public class Main
	extends MIDlet
{
	/**
	 * {@inheritDoc}
	 * @since 2025/12/06
	 */
	@Override
	protected void destroyApp(boolean __uc)
		throws MIDletStateChangeException
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/06
	 */
	@Override
	protected void startApp()
		throws MIDletStateChangeException
	{
		// Setup group list
		Display display = Display.getDisplay(this);
		display.setCurrent(new GroupList(display, SpecificGroup.values()));
	}
}
