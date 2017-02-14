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
	
	/** Players in the game. */
	private final Player[] _players =
		new Player[PlayerColor.NUM_PLAYERS];
	
	/** The current game frame. */
	private volatile int _framenum;
	
	/**
	 * Shared initialization.
	 *
	 * @since 2017/02/14
	 */
	{
		// Initialize players
		Player[] players = this._players;
		for (int i = 0, n = players.length; i < n; i++)
			players[i] = new Player(this, PlayerColor.of(i));
	}
	
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
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns the current game frame.
	 *
	 * @return The game frame.
	 * @since 2017/02/10
	 */
	public int frameCount()
	{
		return this._framenum;
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
	 * Returns the player by the given index.
	 *
	 * @param __i The index of the player to get.
	 * @return The player for the given index.
	 * @throws IllegalArgumentException If the index is not within bounds.
	 * @since 2017/02/14
	 */
	public Player player(int __i)
		throws IllegalArgumentException
	{
		// {@squirreljme.error BE07 Invalid player index. (The index)}
		if (__i < 0 || __i >= PlayerColor.NUM_PLAYERS)
			throw new IllegalArgumentException(String.format("BE07 %d", __i));
		
		return this._players[__i];
	}
	
	/**
	 * Returns the player by the given color.
	 *
	 * @param __p The color to get the player of.
	 * @return The player for the given color.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/14
	 */
	public Player player(PlayerColor __p)
		throws NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		return this._players[__p.ordinal()];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public void run()
	{
		// Get current frame
		int framenum = this._framenum;
		
		// Run the level logic
		this.level.__run(framenum);
		
		// Run the player logic for each player
		Player[] players = this._players;
		for (int i = 0, n = players.length; i < n; i++)
			players[i].__run(framenum);
		
		// Increase the game frame
		this._framenum = framenum + 1;
	}
}

