// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.lcduidemo;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.TabbedPane;
import javax.microedition.lcdui.Ticker;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * This is the main entry point for the graphics demo which initializes the
 * canvas for rendering.
 *
 * @since 2018/03/23
 */
public final class Main
	extends MIDlet
{
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	protected void destroyApp(boolean __uc)
		throws MIDletStateChangeException
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	protected void startApp()
		throws MIDletStateChangeException
	{
		Canvas hello = new DemoCanvas();
		
		Ticker ticky = new Ticker("Hello from SquirrelJME! " +
			"Squirrels are super cuties! <3");
		hello.setTicker(ticky);
		
		Display.getDisplay(this).setCurrent(hello);
	}
}

