// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelscavenger.game;

import net.multiphasicapps.squirrelscavenger.gui.GUI;

/**
 * This class is used to run the main game loop which runs and then draws
 * the game.
 *
 * @since 2016/10/06
 */
public class FrameTimer
	implements Runnable
{
	/** The game to play. */
	protected final Game game;
	
	/** The renderer to draw with. */
	protected final GUI draw;
	
	/**
	 * Initializes the frame timer.
	 *
	 * @param __game The game to play.
	 * @param __draw The draw runner.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/06
	 */
	public FrameTimer(Game __game, GUI __draw)
		throws NullPointerException
	{
		// Check
		if (__game == null || __draw == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.game = __game;
		this.draw = __draw;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/06
	 */
	@Override
	public void run()
	{
		// Cache
		Game game = this.game;
		GUI draw = this.draw;
		
		// Perform some calculations
		int framerate = Game.FRAME_RATE;
		int nanosperframe = 1_000_000_000 / framerate;
		
		// Never ends!
		for (;;)
		{
			// Run the game loop
			long gs = System.nanoTime();
			game.run();
			
			// Determine if the game should be drawn
			long ge = System.nanoTime();
			int gd = nanosperframe - ((int)(ge - gs));
			
			// Run the render loop if there is enough time
			if (gd > 0)
				draw.run();
			
			// If not all of the time was spent rendering then sleep for the
			// rest of it
			long xe = System.nanoTime();
			int sd = nanosperframe - ((int)(xe - gs));
			if (sd > 0)
				try
				{
					Thread.sleep(sd);
				}
				
				// Ignore
				catch (InterruptedException e)
				{
				}
			
			// Just yield 
			else
				Thread.yield();
		}
	}
}

