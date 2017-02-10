// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * This class contains the state for a single game.
 *
 * @since 2017/02/08
 */
public class Game
	implements Runnable
{
	/** Random number generator for games. */
	protected final GameRandom random;
	
	/** The level data. */
	protected final Level level;
	
	/**
	 * Initializes a game with the default initialization rules.
	 *
	 * @since 2017/02/08
	 */
	public Game()
	{
		this(new InitialSettingsBuilder().build());
	}
	
	/**
	 * Initializes the game with the given initial settings.
	 *
	 * @param __is The settings to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/09
	 */
	public Game(InitialSettings __is)
		throws NullPointerException
	{
		// Check
		if (__is == null)
			throw new NullPointerException("NARG");
		
		// Setup random number generator
		this.random = new GameRandom(__is.seed());
		
		// Initialize the level using the initial settings
		Level level = new Level(this, __is);
		this.level = level;
	}
	
	/**
	 * Initializes a game from a previous game serialization such as a saved
	 * game or replay.
	 *
	 * @param __is The input stream to read the game from.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/10
	 */
	public Game(InputStream __is)
		throws IOException, NullPointerException
	{
		// Check
		if (__is == null)
			throw new NullPointerException("NARG");
		
		// Wrap in a data input stream
		DataInputStream input = ((__is instanceof DataInputStream) ?
			(DataInputStream)__is : new DataInputStream(__is));
		
		// Re-initialize the random number generator
		GameRandom random = new GameRandom(0);
		random.setRawSeed((((long)input.readInt()) << 32L) |
			input.readInt());
		
		// De-serialize the level
		Level level = new Level(this, input);
		this.level = level;
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns the level.
	 *
	 * @since 2017/02/10
	 */
	public Level level()
	{
		return this.level;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public void run()
	{
	}
}

