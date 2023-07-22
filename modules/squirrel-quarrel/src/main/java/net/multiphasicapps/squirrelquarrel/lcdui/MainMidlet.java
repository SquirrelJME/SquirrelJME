// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.lcdui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import net.multiphasicapps.squirrelquarrel.game.GameLooper;
import net.multiphasicapps.squirrelquarrel.game.ResumeMode;

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
		
		// For initial testing purposes, this will setup and play a game
		// then once it finishes it will reload it and allow watching the
		// game which was just played
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream())
		{
			// Setup game with the loop
			GameLooper looper = new GameLooper(baos, null);
			
			// Setup game canvas with an initial game
			GameInterface gi = new GameInterface(looper);
			disp.setCurrent(gi);
			
			// Run the game itself until it terminates
			looper.run(gi);
			
			// When the game finishes just play it back and hope it worked!
			baos.flush();
			try (ByteArrayInputStream bais = new ByteArrayInputStream(
				baos.toByteArray()))
			{
				// Setup new loop resuming from the save game but using it
				// as a replay
				looper = GameLooper.resume(null, ResumeMode.REPLAY, bais);
				
				// Setup new canvas to show that game instead
				gi = new GameInterface(looper);
				disp.setCurrent(gi);
				
				// Run the game logic in that
				looper.run(gi);
			}
		}
		
		/* {@squirreljme.error BE02 Failed to rea/write something.} */
		catch (IOException e)
		{
			throw new RuntimeException("BE02", e);
		}
	}
}

