// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreldigger.gui.lcdui;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import net.multiphasicapps.squirreldigger.game.Game;
import net.multiphasicapps.squirreldigger.game.player.Controller;
import net.multiphasicapps.squirreldigger.gui.GUI;

/**
 * This implements the GUI on top of the LCD UI.
 *
 * @since 2016/10/08
 */
public class LCDUIGUI
	extends GUI
{
	/** The LCD display being used. */
	protected final Display display;
	
	/**
	 * Initializes the LCD UI interface.
	 *
	 * @param __g The game to draw over.
	 * @param __mid The optional midlet that is used to obtain a display,
	 * may be {@code null}.
	 * @since 2016/10/08
	 */
	public LCDUIGUI(Game __g, MIDlet __mid)
	{
		super(__g);
		
		// If a midlet was specified use that to get the display
		Display d = null;
		if (__mid != null)
			d = Display.getDisplay(__mid);
		
		// Otherwise search for one
		if (d == null)
			d = __getDefaultDisplay();
		
		// Set
		this.display = d;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/08
	 */
	@Override
	public Controller localController(int __id)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error BA03 Request for a negative controller.}
		if (__id < 0)
			throw new IndexOutOfBoundsException("BA03");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/08
	 */
	@Override
	public void run()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Find a default LCDUI based display to use.
	 *
	 * @return The display to use.
	 * @throws RuntimeException If one was not found.
	 * @since 2016/10/08
	 */
	private static final Display __getDefaultDisplay()
		throws RuntimeException
	{
		// Just use any display with one that supports input since only a
		// canvas will be used as a screen for the most part
		for (Display d : Display.getDisplays(Display.SUPPORTS_INPUT_EVENTS))
			return d;
		
		// {@squirreljme.error BA04 Could not find a LCDUI display.}
		throw new RuntimeException("BA04");
	}
}

