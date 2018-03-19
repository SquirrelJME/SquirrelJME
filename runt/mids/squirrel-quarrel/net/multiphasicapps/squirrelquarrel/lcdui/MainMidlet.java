// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.lcdui;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import net.multiphasicapps.squirrelquarrel.game.Game;
import net.multiphasicapps.squirrelquarrel.game.GameLooper;

/**
 * This is the main midlet entry point for Squirrel Quarrel.
 *
 * @since 2017/02/08
 */
public class MainMidlet
	extends MIDlet
{
	/**
	 * {@inheritDoc}
	 * @since 2017/02/08
	 */
	@Override
	protected void destroyApp(boolean __uc)
		throws MIDletStateChangeException
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/08
	 */
	@Override
	protected void startApp()
		throws MIDletStateChangeException
	{
		// Get the display for this MIDlet
		Display disp = Display.getDisplay(this);
		
		// Setup game with the loop
		GameLooper looper = new GameLooper(new Game());
		
		// Setup game canvas with an initial game
		GameInterface gi = new GameInterface(looper);
		disp.setCurrent(gi);
		
		// Run the game loop in the background
		Thread t = new Thread(looper, "game-loop");
		t.start();
	}
}

