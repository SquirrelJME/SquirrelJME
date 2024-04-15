// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.game;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import net.multiphasicapps.squirrelquarrel.player.Player;
import net.multiphasicapps.squirrelquarrel.player.PlayerColor;
import net.multiphasicapps.squirrelquarrel.player.Players;
import net.multiphasicapps.squirrelquarrel.units.SpawnPlacementType;
import net.multiphasicapps.squirrelquarrel.units.Unit;
import net.multiphasicapps.squirrelquarrel.units.UnitType;
import net.multiphasicapps.squirrelquarrel.units.Units;
import net.multiphasicapps.squirrelquarrel.util.GameRandom;
import net.multiphasicapps.squirrelquarrel.world.World;

/**
 * This class contains the state for a single game.
 *
 * @since 2017/02/08
 */
public class Game
{
	/** The player manager. */
	protected final Players players;
	
	/** The unit manager. */
	protected final Units units;
	
	/** Random number generator for games. */
	protected final GameRandom random;
	
	/** The level information. */
	protected final World world;
	
	/** The current game frame. */
	private volatile int _framenum;
	
	/** Has the initial game setup been booted? */
	private boolean _booted;
	
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
		this.world = new World(__is);
		
		// Initialize players
		this.players = new Players(__is);
		
		// Initialize units
		this.units = new Units(__is);
	}
	
	/**
	 * Returns the current game frame.
	 *
	 * @return The game frame.
	 * @since 2017/02/10
	 */
	public final int frameCount()
	{
		return this._framenum;
	}
	
	/**
	 * Returns the player manager.
	 *
	 * @return The player manager.
	 * @since 2018/03/19
	 */
	public final Players players()
	{
		return this.players;
	}
	
	/**
	 * Runs a single game frame.
	 *
	 * @since 2017/02/10
	 */
	public final void run()
	{
		// Get current frame
		int framenum = this._framenum;
		
		// On the first frame if the game is not booted, spawn everything
		// as needed so it is all done within the loop
		if (!this._booted)
		{
			this.__boot();
			this._booted = true;
		}
		
		// Run all the sub-logic
		this.world.run(framenum);
		this.units.run(framenum);
		this.players.run(framenum);
		
		// Increase the game frame
		this._framenum = framenum + 1;
	}
	
	/**
	 * Returns the world.
	 *
	 * @return The world.
	 * @since 2017/02/10
	 */
	public World world()
	{
		return this.world;
	}
	
	/**
	 * Boots the game spawning everything as needed.
	 *
	 * @since 2019/03/24
	 */
	private void __boot()
	{
		Units units = this.units;
		Players players = this.players;
		
		// Create initial game units for players who are playing
		for (int i = 0, n = PlayerColor.NUM_COLORS; i < n; i++)
		{
			// Only if the player is playing
			Player p = players.get(i);
			if (p.isPlaying())
			{
				// Create building
				Unit base = units.createUnit(SpawnPlacementType.BUILDING,
					UnitType.CHLOROPHID_GARDEN, (Unit)null,
					64 + (128 * i), 64);
				
				// Create workers from the building
				Debugging.todoNote("Spawn workers!", new Object[] {});
			}
		}
	}
}

