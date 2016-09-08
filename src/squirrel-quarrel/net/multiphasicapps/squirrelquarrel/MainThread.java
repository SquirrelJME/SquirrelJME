// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel;

import net.multiphasicapps.squirrelquarrel.game.GameSpeed;

/**
 * This is the main thread of the game which handles game creation and
 * providing a menu as needed.
 *
 * @since 2016/09/07
 */
public class MainThread
	implements Runnable
{
	/**
	 * Initializes the main thread.
	 *
	 * @param __args The program arguments.
	 * @since 2016/09/07
	 */
	public MainThread(String... __args)
	{
		// Must exist
		if (__args == null)
			__args = new String[0];
	}
	
	/**
	 * Returns the current speed to run the main loop at. This matches the
	 * game speed if one is playing since the main loop calls into the game
	 * logic.
	 *
	 * @return The current game speed.
	 * @since 2016/09/07
	 */
	public GameSpeed currentSpeed()
	{
		// In the menu, always go at the slowest speed
		return GameSpeed.SLOWEST;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/07
	 */
	@Override
	public void run()
	{
		// Infinite loop
		for (int loop = 0;; loop++)
		{
			// Get the enter time and the run speed
			long enter = System.nanoTime();
			long fdur = currentSpeed().nanosPerFrame();
			
			// Run the game logic
			System.err.printf("DEBUG -- Logic %d%n", loop);
			
			// Is there enough time to render the game?
			long logend = System.nanoTime();
			long logdur = logend - enter;
			if (logdur < fdur)
			{
				System.err.printf("DEBUG -- Render %d%n", loop);
			}
			
			// Sleep for the remaining amount of time, if possible
			try
			{
				long end = System.nanoTime();
				long dur = fdur - (end - enter);
				if (fdur > 0)
					Thread.sleep((int)(fdur / 1_000_000L));
				
				// If not sleep, yield every so often so the system does not
				// freeze running the game
				else if ((loop & 7) == 0)
					Thread.yield();
			}
			
			// Yield instead to allow other programs to run
			catch (InterruptedException e)
			{
				Thread.yield();
			}
		}
	}
}

