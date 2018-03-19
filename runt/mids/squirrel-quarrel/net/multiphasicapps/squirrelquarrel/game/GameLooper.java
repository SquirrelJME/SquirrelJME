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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This manages and runs the game loop.
 *
 * @since 2018/03/19
 */
public final class GameLooper
	implements Runnable
{
	/** The game to loop for. */
	protected final Game game;
	
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
		
		this.game = new Game(__i);
		
		throw new todo.TODO();
	}
	
	/**
	 * Initializes the game looper which decodes game state from the specified
	 * stream.
	 *
	 * @param __out The stream to write replay data to.
	 * @param __rm The resumption mode of the game.
	 * @param __in The stream of serialized game data to read from.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/19
	 */
	public GameLooper(OutputStream __out, ResumeMode __rm, InputStream __in)
		throws IOException, NullPointerException
	{
		if (__out == null || __rm == null || __in == null)
			throw new todo.TODO();
		
		// Wrap in a data input stream
		DataInputStream input = ((__in instanceof DataInputStream) ?
			(DataInputStream)__in : new DataInputStream(__in));
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public void run()
	{
		throw new todo.TODO();
		/*
		Game game = this.game;
		GameInputHandler inputhandler = this.inputhandler;
		for (;;)
		{
			// Get the current game speed and entry time
			GameSpeed speed = this._speed;
			long enter = System.nanoTime();
			
			// Run a single game cycle
			game.run();
			
			// Perform local client event handling (commands and such)
			inputhandler.run();
			
			// Request a repaint if there is enough time to draw
			long exit = System.nanoTime();
			if ((exit - enter) < speed.nanoFrameTime() && !this._inpaint)
			{
				int gameframe = game.frameCount(),
					paintframe = this._renderframe;
				
				// Renderer seems to be a bit slow, skip the request
				if (gameframe < paintframe - 1)
					this._renderframe = gameframe;
				
				// Request repaint
				else
					repaint();
			}
			
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
		*/
	}
}

