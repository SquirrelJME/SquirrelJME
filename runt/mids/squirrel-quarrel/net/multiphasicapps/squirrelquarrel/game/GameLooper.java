// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.game;

import java.io.InputStream;
import java.io.OutputStream;
import net.multiphasicapps.squirrelquarrel.ui.FrameSync;
import net.multiphasicapps.squirrelquarrel.util.ReplayInputStream;
import net.multiphasicapps.squirrelquarrel.util.ReplayOutputStream;

/**
 * This manages and runs the game loop.
 *
 * @since 2018/03/19
 */
public final class GameLooper
{
	/** The output for replay recordings. */
	protected final ReplayOutputStream record;
	
	/** The game to loop for. */
	protected final Game game;
	
	/** The speed the game runs at. */
	private volatile GameSpeed _speed =
		GameSpeed.NORMAL;
	
	/**
	 * Initializes the game looper with the default settings.
	 *
	 * @param __out The stream to write replay data to.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/18
	 */
	public GameLooper(OutputStream __out)
		throws NullPointerException
	{
		this(__out, new InitialSettingsBuilder().build());
	}
	
	/**
	 * Initializes the game looper.
	 *
	 * @param __out The stream to write replay data to.
	 * @param __i The initial settings for the game.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/18
	 */
	public GameLooper(OutputStream __out, InitialSettings __i)
		throws NullPointerException
	{
		if (__out == null || __i == null)
			throw new NullPointerException("NARG");
		
		// Setup replay output for consistent game recording
		ReplayOutputStream record = new ReplayOutputStream(__out);
		this.record = record;
		__i.demoRecord(record);
		
		// Initialize the game with basic settings
		this.game = new Game(__i);
	}
	
	/**
	 * Returns the game this provides a loop for.
	 *
	 * @return The game being looped.
	 * @since 2018/03/18
	 */
	public final Game game()
	{
		return this.game;
	}
	
	/**
	 * Runs multiple game frames.
	 *
	 * @param __fs Callback for when game frames are updated.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/10
	 */
	public void run(FrameSync __fs)
		throws NullPointerException
	{
		if (__fs == null)
			throw new NullPointerException("NARG");
		
		Game game = this.game;
		for (;;)
		{
			// Get the current game speed and entry time
			GameSpeed speed = this._speed;
			long enter = System.nanoTime();
			
			// Run a single game cycle
			int nowframe = game.frameCount();
			game.run();
			
			// Request a repaint if there is enough time to draw
			long exit = System.nanoTime();
			if ((exit - enter) < speed.nanoFrameTime())
				__fs.frameRepaintRequest(nowframe);
			
			// Delay thread for the next frame
			exit = System.nanoTime();
			long durr = (speed.nanoFrameTime() - (exit - enter)) / 1_000_000L;
			if (durr > 0)
				try
				{
					Thread.sleep(durr);
				}
				catch (InterruptedException e)
				{
				}
		}
	}
	
	/**
	 * Runs a single game frame.
	 *
	 * @since 2018/03/19
	 */
	public void runFrame()
	{
	}
	
	/**
	 * Initialize a game loop which either resumes the game from the given
	 * point or plays it back in a replay.
	 *
	 * @param __out The output for replay data.
	 * @param __rm How will the game be resumed?
	 * @param __in The input stream for replay/save data.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/19
	 */
	public static final GameLooper resume(OutputStream __out,
		ResumeMode __rm, InputStream __in)
		throws NullPointerException
	{
		if (__out == null || __rm == null || __in == null)
			throw new NullPointerException("NARG");
		
		// Setup input replay strema
		ReplayInputStream replay = new ReplayInputStream(__in);
		
		// Read initial settings from the replay and initialize the game
		InitialSettings init = InitialSettings.demoReplay(replay);
		GameLooper rv = new GameLooper(__out, init);
		
		if (true)
			throw new todo.TODO();
		
		return rv;
	}
}

