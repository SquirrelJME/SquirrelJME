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

import net.multiphasicapps.squirrelquarrel.player.PlayerColor;
import net.multiphasicapps.squirrelquarrel.world.MegaTile;

/**
 * This is used to build the initial settings for the game.
 *
 * This class is not thread safe.
 *
 * @since 2017/02/09
 */
public class InitialSettingsBuilder
{
	/** How the teams are laid out. */
	final int[] _teams =
		new int[PlayerColor.MAX_PLAYERS];
	
	/** The time these settings were created. */
	volatile long _timestamp =
		System.currentTimeMillis();
	
	/** The width of the map in tiles. */
	volatile int _mapwidth =
		64;
	
	/** The height of the map in tiles. */
	volatile int _mapheight =
		64;
	
	/** The players playing in the game. */
	volatile int _players =
		2;
	
	/** The seed to use. */
	volatile long _seed =
		System.currentTimeMillis();
	
	/**
	 * Initializes some more complex settings.
	 *
	 * @since 2018/03/19
	 */
	{
		// Start off all players on their own team (FFA)
		int[] teams = this._teams;
		for (int i = 0, n = teams.length; i < n; i++)
			teams[i] = i;
	}
	
	/**
	 * Builds the settings.
	 *
	 * @return The resulting settings.
	 * @since 2017/02/09
	 */
	public InitialSettings build()
	{
		return new InitialSettings(this);
	}
	
	/**
	 * Sets the map size in tiles. If an input dimension is not valid then it
	 * will be corrected.
	 *
	 * @param __w The width of the map.
	 * @param __h The height of the map.
	 * @since 2017/02/10
	 */
	public void mapSize(int __w, int __h)
	{
		this._mapwidth = Math.max(MegaTile.TILES_PER_MEGA_TILE,
			(__w - (__w % MegaTile.TILES_PER_MEGA_TILE)));
		this._mapheight = Math.max(MegaTile.TILES_PER_MEGA_TILE,
			(__h - (__w % MegaTile.TILES_PER_MEGA_TILE)));
	}
	
	/**
	 * Sets the number of players that are playing in the game.
	 *
	 * @param __p The players that are playing in the game.
	 * @since 2018/03/19
	 */
	public void players(int __p)
	{
		// Allow one player in the event one wants to play alone for any given
		// reason
		this._players = Math.max(1, Math.min(PlayerColor.MAX_PLAYERS, __p));
	}
	
	/**
	 * Sets the seed to use for random generation.
	 *
	 * @param __s The seed to use.
	 * @since 2017/02/10
	 */
	public void seed(long __s)
	{
		this._seed = __s;
	}
	
	/**
	 * Sets the timestamp of the game start time.
	 *
	 * @param __t The time the game started.
	 * @since 2018/03/19
	 */
	public void startTimeMillis(long __t)
	{
		this._timestamp = __t;
	}
	
	/**
	 * Sets how the teams are to be laid out.
	 *
	 * @param __t Array containing the teams to be laid out.
	 * @since 2018/03/19
	 */
	public void teams(int... __t)
	{
		if (__t == null)
			__t = new int[0];
		
		int[] teams = this._teams;
		int n = __t.length;
		for (int i = 0; i < n; i++)
			teams[i] = __t[i];
		for (int i = n; i < PlayerColor.MAX_PLAYERS; i++)
			teams[i] = i;
	}
}

